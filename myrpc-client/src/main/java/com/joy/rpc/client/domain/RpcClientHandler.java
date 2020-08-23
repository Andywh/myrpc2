package com.joy.rpc.client.domain;

import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.domain.User;
import com.joy.rpc.common.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ai Lun on 2020-08-22.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<User> {
    //
    //private ConcurrentHashMap<String, List<Future>> futureContext;
    //
    //private Channel channel;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        //User user = new User("andy", 18);
        //System.out.println("send: " + user);
        //ctx.writeAndFlush(user).sync();
        //System.out.println("send: end");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User in) throws Exception {
        System.out.println("Client recieved: " + in.toString());
    }

}
