package com.joy.rpc.client.connect;

import com.joy.rpc.client.domain.NetAddress;
import com.joy.rpc.client.handler.RpcClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class ConnectionManager {

    // 连接管理
    //private Map<String, NettyConnection> map;

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(6);

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 8,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

    //public void connect(List<NetAddress> addresses) {
    //
    //}

    private static class SingletonHolder {
        private static final ConnectionManager instance = new ConnectionManager();
    }

    private static ConnectionManager getInstance() {
        return SingletonHolder.instance;
    }

    public void connectServer(String host, int port) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(eventLoopGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new RpcClientInitializer());

                    bootstrap.connect(host, port).sync();
                } catch (Exception e) {
                    System.out.println("e " + e.toString());
                }
            }
        });
    }



}
