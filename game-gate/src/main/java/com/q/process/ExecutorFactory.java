package com.q.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFactory {
    private Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<String, ExecutorService>();

    public enum ExecutorType {
        Logic_main,
    }

    public void initThreadPool() {
        for (ExecutorType type : ExecutorType.values()) {
            //Executors.newScheduledThreadPool()
        }
    }
}
