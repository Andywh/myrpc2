package com.joy.rpc.client;

import com.joy.rpc.client.domain.AsyCallback;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.domain.RpcClientHandler;
import com.joy.rpc.client.proxy.RpcProxy;
import com.joy.rpc.client.proxy.RpcService;
import com.joy.rpc.common.codec.RpcDecoder;
import com.joy.rpc.common.codec.RpcEncoder;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-22.
 */
public class RpcClient {

    private Channel channel;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static <T> T createService(Class<T> interfaceClass) {
        return RpcProxy.create(interfaceClass);
    }

    //public void send(User user) {
    //    EventLoopGroup group = new NioEventLoopGroup();
    //    try {
    //        // 创建并初始化 Netty 客户端 Bootstrap 对象
    //        Bootstrap bootstrap = new Bootstrap();
    //        bootstrap.group(group);
    //        bootstrap.remoteAddress(new InetSocketAddress("127.0.0.1", 8894));
    //        bootstrap.channel(NioSocketChannel.class);
    //        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
    //            @Override
    //            public void initChannel(SocketChannel channel) throws Exception {
    //                ChannelPipeline pipeline = channel.pipeline();
    //                pipeline.addLast(new RpcEncoder(User.class)); // 编码 RPC 请求
    //                pipeline.addLast(new RpcDecoder(User.class)); // 解码 RPC 响应
    //                pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
    //            }
    //        });
    //        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    //        // 连接 RPC 服务器
    //        ChannelFuture future = bootstrap.connect("127.0.0.1", 8894).sync();
    //        // 写入 RPC 请求数据并关闭连接
    //        Channel channel = future.channel();
    //        this.channel = channel;
    //        //channel.writeAndFlush(new User("andy", 19));
    //        channel.writeAndFlush(user).sync();
    //        // fixme 下面一句注释了就变异步了,加上不要关闭连接（也就是注释掉 finally 里的代码)
    //        //channel.closeFuture().sync();
    //        // 返回 RPC 响应对象
    //    } catch (Exception e) {
    //        System.out.println("error: " + e);
    //    }
    //    //} finally {
    //    //    System.out.println("client netty shut down");
    //    //    group.shutdownGracefully();
    //    //}
    //}

    //public Future sendRequest(User user) {
    //    Future future = new Future(new Request());
    //    try {
    //        this.send(user);
    //    } catch (Exception e) {
    //
    //    }
    //    return future;
    //}

    //public void start() {
    //    EventLoopGroup group = new NioEventLoopGroup();
    //    try {
    //        // 创建并初始化 Netty 客户端 Bootstrap 对象
    //        Bootstrap bootstrap = new Bootstrap();
    //        bootstrap.group(group);
    //        bootstrap.remoteAddress(new InetSocketAddress("127.0.0.1", 8894));
    //        bootstrap.channel(NioSocketChannel.class);
    //        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
    //            @Override
    //            public void initChannel(SocketChannel channel) throws Exception {
    //                ChannelPipeline pipeline = channel.pipeline();
    //                pipeline.addLast(new RpcEncoder(User.class)); // 编码 RPC 请求
    //                pipeline.addLast(new RpcDecoder(User.class)); // 解码 RPC 响应
    //                //pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
    //            }
    //        });
    //        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    //        // 连接 RPC 服务器
    //        ChannelFuture future = bootstrap.connect("127.0.0.1", 8894).sync();
    //        // 写入 RPC 请求数据并关闭连接
    //        Channel channel = future.channel();
    //        this.channel = channel;
    //        //channel.writeAndFlush(new User("andy", 19));
    //        channel.writeAndFlush(new User("andy", 30));
    //        channel.closeFuture().sync();
    //        // 返回 RPC 响应对象
    //    } catch (Exception e) {
    //        System.out.println("error: " + e);
    //    } finally {
    //        System.out.println("shut down");
    //        group.shutdownGracefully();
    //    }
    //}

    //public static Future send(Request request) {
    //
    //   return new Future() {
    //   }
    //
    //}

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RpcClientHandler client = new RpcClientHandler();
        //client.send(new User("an", 12));
        //ChannelFuture channelFuture = client.channel.writeAndFlush(new User("And,", 90)).sync();
        //RpcClient client = new RpcClient();
        Request request = new Request();
        request.setRequestId("2874362");
        request.setClazzName("com.joy.rpc.UserServiceImpl");
        request.setMethodName("query()");
        Future future = client.sendRequest(request);
        future.addCallBack(new AsyCallback() {
            @Override
            public void success(Object result) {
                System.out.println("success");
            }

            @Override
            public void fail(Exception e) {
                System.out.println("fail");
            }
        });
        System.out.println("continue...");
        System.out.println("异步结果: " + future.get());
        //System.out.println("end...");
    }
}
