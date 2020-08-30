package com.joy.rpc.client;

import com.joy.rpc.client.connect.ConnectionManager;
import com.joy.rpc.client.discovery.DiscoveryService;
import com.joy.rpc.client.discovery.impl.ZooKeeperDiscoveryServiceImpl;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.domain.context.FutureManager;
import com.joy.rpc.client.loadbalance.RpcLoadBalance;
import com.joy.rpc.client.loadbalance.impl.RpcLoadBalanceRandom;
import com.joy.rpc.client.proxy.RpcProxy;
import com.joy.rpc.client.proxy.RpcService;
import com.joy.rpc.common.domain.Request;
import io.netty.channel.*;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-22.
 */
public class RpcClient {

    private DiscoveryService discoveryService;

    private RpcLoadBalance rpcLoadBalance = new RpcLoadBalanceRandom();

    public RpcClient(String registryAddress) {
        this.discoveryService = new ZooKeeperDiscoveryServiceImpl(registryAddress);
    }

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static <T> T createService(Class<T> interfaceClass) {
        return RpcProxy.create(interfaceClass);
    }

    public static <T> T createService(Class<T> interfaceClass, String version) {
        return RpcProxy.create(interfaceClass, version);
    }

    public static <T> RpcService createAsyncService(Class<T> interfaceClass, String version) {
        return new RpcProxy<T>(interfaceClass, version);
    }



}
