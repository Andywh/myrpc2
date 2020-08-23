package com.joy.rpc.client.domain;

import com.joy.rpc.client.RpcClient;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class Future implements java.util.concurrent.Future<Object> {

    private Sync sync;

    private Request request;

    private Response response;

    private User user;

    private List<AsyCallback> pendingCallbacks = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    public Future(Request request) {
        this.sync = new Sync();
        this.request = request;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return sync.isDone();
    }

    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(1);
        if (this.response != null) {
            return this.response.getResult();
        } else {
            return null;
        }
    }

    public void done(Response response) {
        this.response = response;
        sync.release(1);
        invokeCallbacks();
    }

    public void done(User user) {
        this.user = user;
        sync.release(1);
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }

    }

    private void runCallback(final AsyCallback callback) {
        final Response res = this.response;
        RpcClient.submit(new Runnable() {
            @Override
            public void run() {
                if (!res.isError()) {
                    callback.success(res.getResult());
                } else {
                    callback.fail(new RuntimeException("Response error", new Throwable(res.getError())));
                }
            }
        });

    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success) {
            if (this.user != null) {
                //return this.response.getResult();
                return this.user;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request class name: " + this.request.getClazzName()
                    + ". Request method: " + this.request.getMethodName());

        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        protected boolean isDone() {
            return getState() == done;
        }

    }

}
