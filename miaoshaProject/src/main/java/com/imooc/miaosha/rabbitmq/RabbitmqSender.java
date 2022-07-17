package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname RabbitmqSender
 * @Description TODO
 * @Date 2022/5/8 19:56
 * @Created by Eskii
 */
@Service
public class RabbitmqSender {

    private final Logger logger = LoggerFactory.getLogger(RabbitmqSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object obj) {
        String msg = Serialization.objToString(obj);
        logger.info("RabbitmqSender.send: " + msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.QUEUE_NAME, msg);
    }

    public void miaoshaSender(Object obj) {
        String msg = Serialization.objToString(obj);
        logger.info("RabbitmqSender.send: " + msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.MIAOSHA_QUEUE, msg);
    }
}
