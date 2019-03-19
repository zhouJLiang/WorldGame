package com.q.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

/**
 * @Author zjl
 * @Date 2019/3/19 22:48
 */
public class JedisTemplate {
    private final Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

    @Autowired
    private  RedisManager  redisManager;

    public void dsiConnect(){

    }
    public void add(String key,String value){
        ShardedJedis jedis = redisManager.getSharedJedis();
        if(jedis== null){
            logger.error("連接redis失敗");
            return;
        }
        jedis.set(key,value);
    }
}
