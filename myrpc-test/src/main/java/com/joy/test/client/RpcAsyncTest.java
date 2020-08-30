package com.joy.test.client;

import com.joy.rpc.client.RpcClient;
import com.joy.rpc.client.domain.AsyCallback;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.proxy.RpcService;
import com.joy.test.service.HelloService;
import com.joy.test.service.Person;

import java.util.concurrent.ExecutionException;

/**
 * Created by Ai Lun on 2020-08-30.
 */
public class RpcAsyncTest {

    public static void main(String[] args) throws Exception {
        //final
        final RpcClient rpcClient = new RpcClient("127.0.0.1:2181");
        Thread.sleep(3000);
        //
        //HelloService helloService = RpcClient.createService(HelloService.class, "1.0");
        //String result = helloService.hello("Song ");

        RpcService rpcService = RpcClient.createAsyncService(HelloService.class, "1.0");
        //Future future = rpcService.call("hello", "Song");
        Future future = rpcService.call("hello", new Person("fisrt", "second"));

        future.addCallBack(new AsyCallback() {
            @Override
            public void success(Object result) {
                System.out.println("success...");
            }

            @Override
            public void fail(Exception e) {
                System.out.println("fail...");
            }
        });
        System.out.println("continue...");

        System.out.println("result: " + future.get());


    }
}
