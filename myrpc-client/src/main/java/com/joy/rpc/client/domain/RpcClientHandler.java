package com.joy.rpc.client.domain;

import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.domain.User;
import com.joy.rpc.common.util.SerializationUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
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

    private Future future;

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
        // 这里的两个 RpcClientHandler 不一样，所以 future 报了 npe
        this.future.done(in);
    }

    public Future sendRequest(User user) {
        Future future = new Future(new Request());
        this.future = future;
        try {
            this.send(user);
        } catch (Exception e) {

        }
        return future;
    }

    public void send(User user) {
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
                    pipeline.addLast(new RpcEncoder(User.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(User.class)); // 解码 RPC 响应
                    pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8894).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            //channel.writeAndFlush(new User("andy", 19));
            channel.writeAndFlush(user).sync();
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
