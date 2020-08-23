package com.joy.rpc.client;

import com.joy.rpc.client.proxy.RpcProxy;
import com.joy.rpc.client.proxy.RpcService;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-22.
 */
public class RpcClient {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static <T> T createService(Class<T> interfaceClass) {
        return RpcProxy.create(interfaceClass);
    }

    //public static <T>RpcService createAsyncService(Class<T> interfaceClass) {
    //    return new
    //}


}
