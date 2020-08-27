package com.joy.rpc.server.handler;

import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Andy
 * @date 2020/08/27
 **/
public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        System.out.println("server receive message");
        //super.channelRead(ctx, msg);
        System.out.println("Server received: " + request.toString());
        Thread.sleep(3000);
        Response response = new Response();
        response.setError("no error");
        response.setRequestId(request.getRequestId());
        response.setResult("this is return message");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
