//package com.joy.rpc.client;
//
//import com.joy.rpc.client.domain.RpcClientHandler;
//import com.joy.rpc.client.service.UserService;
//import com.joy.rpc.common.codec.RpcDecoder;
//import com.joy.rpc.common.codec.RpcEncoder;
//import com.joy.rpc.common.domain.User;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.util.CharsetUtil;
//
//import java.net.InetSocketAddress;
//
///**
// * Created by Ai Lun on 2020-08-20.
// */
//public class Test {
//
//    public static void main(String[] args) throws InterruptedException {
//        //RpcClient rpcClient = new RpcClient();
//        //
//        //UserService userService =
//
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            // 创建并初始化 Netty 客户端 Bootstrap 对象
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group);
//            bootstrap.remoteAddress(new InetSocketAddress("127.0.0.1", 8894));
//            bootstrap.channel(NioSocketChannel.class);
//            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel channel) throws Exception {
//                    ChannelPipeline pipeline = channel.pipeline();
//                    pipeline.addLast(new RpcEncoder(User.class)); // 编码 RPC 请求
//                    pipeline.addLast(new RpcDecoder(User.class)); // 解码 RPC 响应
//                    //pipeline.addLast(new RpcClientHandler()); // 处理 RPC 响应
//
//                }
//            });
//            bootstrap.option(ChannelOption.TCP_NODELAY, true);
//            // 连接 RPC 服务器
//            ChannelFuture future = bootstrap.connect("127.0.0.1", 8894).sync();
//            // 写入 RPC 请求数据并关闭连接
//            Channel channel = future.channel();
//            //channel.writeAndFlush(new User("andy", 19));
//            channel.closeFuture().sync();
//            // 返回 RPC 响应对象
//        } finally {
//            group.shutdownGracefully();
//        }
//
//    }
//}
