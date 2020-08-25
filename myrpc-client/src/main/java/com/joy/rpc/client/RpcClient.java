package com.joy.rpc.client;

import com.joy.rpc.client.connect.ChannelManager;
import com.joy.rpc.client.connect.ConnectionManager;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.domain.context.FutureManager;
import com.joy.rpc.client.proxy.RpcProxy;
import com.joy.rpc.common.domain.Request;
import io.netty.channel.*;

import java.util.concurrent.ExecutionException;
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

    //public static <T> T createService(Class<T> interfaceClass) {
    //    return RpcProxy.create(interfaceClass);
    //}

    public Future send(Request request) {
        Future future = new Future(request);
        FutureManager.putFuture(request.getRequestId(), future);
        Channel channel = ChannelManager.getChannel("getUser");
        System.out.println("choose channel: " + channel.remoteAddress().toString());
        try {
            channel.writeAndFlush(request).sync();
        } catch (Exception e) {
            System.out.println("send error");
        }
        return future;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connectServer("127.0.0.1", 8894, "getUser");
        connectionManager.connectServer("127.0.0.1", 8895, "getPoint");

        RpcClient client = new RpcClient();

        Request request = new Request();
        request.setRequestId("2874362");
        request.setClazzName("com.joy.rpc.UserServiceImpl");
        request.setMethodName("query()");

        Thread.sleep(3000);

        Future future = client.send(request);

        System.out.println("continue...");
        System.out.println("异步结果: " + future.get());
        System.out.println("end...");
    }

}
