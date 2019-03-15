package com.q.cache;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheSession {
    public static Map<Object, Channel> sessions = new ConcurrentHashMap<>();
}
