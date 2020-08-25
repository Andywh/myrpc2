package com.joy.rpc.client.handler;

import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Ai Lun on 2020-08-25.
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        cp.addLast(new RpcEncoder(Request.class)); // 编码 RPC 请求
        cp.addLast(new RpcDecoder(Response.class)); // 解码 RPC 响应
        cp.addLast(RpcClientHandler.getInstance());
    }
}
