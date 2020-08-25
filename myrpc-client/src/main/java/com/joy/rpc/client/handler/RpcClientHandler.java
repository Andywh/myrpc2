package com.joy.rpc.client.handler;

import com.joy.rpc.client.connect.ChannelManager;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.domain.context.FutureManager;
import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

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
        ChannelManager.putChannel("getUserName", ctx.channel());
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


}
