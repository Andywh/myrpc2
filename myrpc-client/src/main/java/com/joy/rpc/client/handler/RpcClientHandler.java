package com.joy.rpc.client.handler;

import com.joy.rpc.client.connect.ConnectionManager;
import com.joy.rpc.client.discovery.impl.ZooKeeperDiscoveryServiceImpl;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.domain.context.FutureManager;
import com.joy.rpc.client.loadbalance.impl.RpcLoadBalanceRandom;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.util.ServiceUtil;
import io.netty.channel.*;

import java.util.List;

/**
 * Created by Ai Lun on 2020-08-22.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Response> {

    private static RpcClientHandler clientHandler;

    public static RpcClientHandler getInstance() {
        if (clientHandler == null) {
            clientHandler = new RpcClientHandler();
        }
        return clientHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        System.out.println("channelActive " + ctx.channel().remoteAddress().toString());
        //ChannelManager.putChannel("getUserName", ctx.channel());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        String requestId = response.getRequestId();
        Future future = FutureManager.getFuture(requestId);
        future.done(response);
    }

    public static Future send(Request request) {
        Future future = new Future(request);
        FutureManager.putFuture(request.getRequestId(), future);
        String serviceKey = ServiceUtil.buildServiceKey(request.getClazzName(), request.getVersion());
        List<String> addresses = ZooKeeperDiscoveryServiceImpl.serviceMap.get(serviceKey);
        String address = RpcLoadBalanceRandom.route(addresses);
        Channel channel = ConnectionManager.getChannel(address);
        System.out.println("choose channel: " + channel.remoteAddress().toString());
        try {
            channel.writeAndFlush(request).sync();
        } catch (Exception e) {
            System.out.println("send error");
        }
        return future;
    }

}
