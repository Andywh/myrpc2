package com.joy.rpc.client.test;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by Ai Lun on 2020-08-21.
 */
public class Wather {

    public static class Sync extends AbstractQueuedSynchronizer {

        //future status
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

    public static void main(String[] args) {

    }
}
