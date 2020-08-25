package com.joy.rpc.client.connect;

import com.joy.rpc.client.domain.NetAddress;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class ConnectionManager {

    // 连接管理
    private Map<String, NettyConnection> map;

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

    public void connect(List<NetAddress> addresses) {

    }
}
