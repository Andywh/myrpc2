package com.joy.rpc.server.core;

import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.server.handler.RpcServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Andy
 * @date 2020/08/27
 **/
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder(Response.class)); // 编码 RPC 请求
        pipeline.addLast(new RpcDecoder(Request.class)); // 解码 RPC 响应
        pipeline.addLast(new RpcServerHandler());
    }
}
