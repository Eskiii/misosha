package com.imooc.miaosha.redis;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Classname RedisPoolFactory
 * @Description 获取 Redis 连接池
 * @Date 2022/4/26 20:49
 * @Created by Eskii
 */
@Service
@Slf4j
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig; //获取容器中的 redis 配置类 bean
//    private Logger logger = LoggerFactory.getLogger(Tester.class)

    @Bean
    public JedisPool JedisPoolFactory() {
//        public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port, int timeout, final String password, final int database)
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword(), 0);

        log.info(jedisPool.toString());
        return jedisPool;
    }
}
