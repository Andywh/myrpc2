package com.joy.rpc.server;

import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.server.handler.RpcServerHandler;
import com.joy.rpc.server.registry.impl.ZooKeeperRegistryServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Ai Lun on 2020-08-23.
 */
public class RpcServer {

    public static void main(String[] args) throws InterruptedException {

        ZooKeeperRegistryServiceImpl zk = new ZooKeeperRegistryServiceImpl("127.0.0.1:2181");
        zk.register("getUser", "127.0.0.1:8764");
        zk.register("getUser2", "127.0.0.1:8765");
        zk.register("getUser3", "127.0.0.1:8766");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(Response.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(Request.class)); // 解码 RPC 响应
                    pipeline.addLast(new RpcServerHandler());
                }
            });

            ChannelFuture future = bootstrap.bind("127.0.0.1", 8894).sync();
            // 关闭 RPC 服务器
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
