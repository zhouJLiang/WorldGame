package com.q.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFactory {
    private Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<String, ExecutorService>();

    public enum ExecutorType {
        logic_main,
    }

    public void initThreadPool() {
        for (ExecutorType type : ExecutorType.values()) {
            GameThreadFactory gameThreadFactory = new GameThreadFactory(type.name());
            ExecutorService threadPoolExecutor = Executors.newScheduledThreadPool(1,gameThreadFactory);
            threadPoolMap.put(type.name(),threadPoolExecutor);
        }
    }
}
