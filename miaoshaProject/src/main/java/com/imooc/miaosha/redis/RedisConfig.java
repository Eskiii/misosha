package com.imooc.miaosha.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Classname RedisConfig
 * @Description 获取 Redis 配置信息
 * @Date 2022/4/26 20:42
 * @Created by Eskii
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;
}
