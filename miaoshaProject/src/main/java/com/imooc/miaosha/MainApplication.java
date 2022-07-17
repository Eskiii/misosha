package com.imooc.miaosha;

import com.imooc.miaosha.redis.RedisConfig;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Classname MainApplication
 * @Description TODO
 * @Date 2022/4/25 19:21
 * @Created by Eskii
 */
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
//        String[] beanNamesForType = run.getBeanNamesForType(DirectExchange.class);
//        for(String bean : beanNamesForType) {
//            System.out.println(bean);
//        }
//        String[] beanNamesForType1 = run.getBeanNamesForType(Queue.class);
//        for(String bean : beanNamesForType1) {
//            System.out.println(bean);
//        }
    }
}
