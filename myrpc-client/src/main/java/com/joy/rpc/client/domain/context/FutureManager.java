package com.joy.rpc.client.domain.context;

import com.joy.rpc.client.domain.Future;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ai Lun on 2020-08-25.
 */
public class FutureManager {

    private static ConcurrentHashMap<String, Future> pendingRPC = new ConcurrentHashMap<>();

    public static Future getFuture(String requestId) {
        return pendingRPC.get(requestId);
    }

    public static void putFuture(String requestId, Future future) {
        pendingRPC.put(requestId, future);
    }

}
