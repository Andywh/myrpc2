package com.joy.rpc.server;

import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.domain.User;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * Created by Ai Lun on 2020-08-23.
 */
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
    //@Override
    //protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    //    System.out.println("recv msg: " + msg);
    //    ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    //}

}
