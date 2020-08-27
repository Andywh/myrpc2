package com.joy.rpc.common.util;

import java.util.concurrent.*;

/**
 * @author Andy
 * @date 2020/08/27
 **/
public class ThreadPoolUtil {
    public static ThreadPoolExecutor newServerThreadPool(final String serviceName, int corePoolSzie, int maximumPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSzie,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "Andy-rpc-" + serviceName + "-" + r.hashCode());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }
}
