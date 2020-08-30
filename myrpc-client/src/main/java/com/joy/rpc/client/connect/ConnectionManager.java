package com.joy.rpc.client.connect;

import com.joy.rpc.client.handler.RpcClientInitializer;
import com.joy.rpc.client.loadbalance.RpcLoadBalance;
import com.joy.rpc.client.loadbalance.impl.RpcLoadBalanceRandom;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class ConnectionManager {

    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    private RpcLoadBalance rpcLoadBalance = new RpcLoadBalanceRandom();

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(6);

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 8,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

    private static class SingletonHolder {
        private static final ConnectionManager instance = new ConnectionManager();
    }

    public static ConnectionManager getInstance() {
        return SingletonHolder.instance;
    }

    public void connectServer(Map<String, List<String>> serviceMap) {
        Set<String> addresses = new HashSet<>();
        for (List<String> values : serviceMap.values()) {
            addresses.addAll(values);
        }
        for (String address : addresses) {
            connectServer(address);
        }
    }

    private void connectServer(String address) {
        String[] split = address.split(":");
        String host = split[0];
        int port = Integer.parseInt(split[1]);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(eventLoopGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new RpcClientInitializer());

                    ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                putChannel(address, future.channel());
                                System.out.println("future is success");
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("e " + e.toString());
                }
            }
        });
    }

    public static Channel getChannel(String address) {
        return channelMap.get(address);
    }

    public static void putChannel(String address, Channel channel) {
        channelMap.put(address, channel);
    }


}
