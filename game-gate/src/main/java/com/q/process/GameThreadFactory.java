package com.q.process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class GameThreadFactory implements ThreadFactory {
    private String threadName;
    private AtomicInteger poolName = new AtomicInteger(0);

    public GameThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        if(threadName == null)
            threadName = String.valueOf(poolName.incrementAndGet());
        Thread thread = new Thread(threadName);
        return thread;
    }
}
