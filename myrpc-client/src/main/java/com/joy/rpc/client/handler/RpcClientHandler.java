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

    public Future sendRequest(Request request) {
        Future future = new Future(request);
        FutureManager.putFuture(request.getRequestId(), future);
        try {
            this.send(request, future);
        } catch (Exception e) {
            System.out.println("error: " + e.toString());
        }
        return future;
    }

    public void send(Request request, Future future) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.remoteAddress(new InetSocketAddress("127.0.0.1", 8894));
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(Request.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(Response.class)); // 解码 RPC 响应
                    pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future2 = bootstrap.connect("127.0.0.1", 8894).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future2.channel();
            //channel.writeAndFlush(new User("andy", 19));
            channel.writeAndFlush(request).sync();
            // fixme 下面一句注释了就变异步了,加上不要关闭连接（也就是注释掉 finally 里的代码)
            //channel.closeFuture().sync();
            // 返回 RPC 响应对象
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        //} finally {
        //    System.out.println("client netty shut down");
        //    group.shutdownGracefully();
        //}
    }

}
