package com.q.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @Author zjl
 * @Date 2019/3/19 22:47
 */
@Component
public class RedisManager {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis getSharedJedis(){
        return shardedJedisPool.getResource();
    }
}
