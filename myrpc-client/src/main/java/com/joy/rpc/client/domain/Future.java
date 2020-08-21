package com.joy.rpc.client.domain;

import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class Future implements java.util.concurrent.Future<Object> {

    private Request request;

    private Response response;

    private List<AsyCallback> pendingCallbacks = new ArrayList<>();

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }


}
