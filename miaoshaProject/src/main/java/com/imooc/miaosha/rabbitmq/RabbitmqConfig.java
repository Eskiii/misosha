package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname RabbitmqConfig
 * @Description Rabbitmq 配置， 包括创建消息队列
 * @Date 2022/5/8 19:48
 * @Created by Eskii
 */
@Configuration
public class RabbitmqConfig {

    public static final String QUEUE_NAME = "myQueue";
    public static final String MIAOSHA_QUEUE = "miaoshaQueue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue miaoshaQue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }
}
