package com.q;

import com.q.redis.JedisTemplate;
import com.q.redis.RedisManager;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
@Configurable()
public class App 
{
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private JedisTemplate jedisTemplate;
    public static void main( String[] args )
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        JedisTemplate jedisTemplate = (JedisTemplate) context.getBean("jedisTemplate");
        jedisTemplate.add("key","value");
        System.out.println( "Hello World!" );

    }
}
