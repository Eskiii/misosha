package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.utils.Serialization;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Classname RabbitmqReceiver
 * @Description TODO
 * @Date 2022/5/8 19:57
 * @Created by Eskii
 */
@Service
public class RabbitmqReceiver {

    private final Logger logger = LoggerFactory.getLogger(RabbitmqReceiver.class);

    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitmqConfig.QUEUE_NAME)
    public void receive(String msg) {
        logger.info("RabbitmqReceiver.receive: " + msg);
    }

    @RabbitListener(queues = RabbitmqConfig.MIAOSHA_QUEUE)
    public void miaoshaReceiver(String msg) {
        MiaoshaMessage message = Serialization.stringToObj(msg, MiaoshaMessage.class);
        MiaoshaUser miaoshaUser = message.getMiaoshaUser();
        Long goodsId = message.getGoodsId();
        GoodsVo goodsVo = goodsService.getById(goodsId);    //访问一次数据库
        if (goodsVo.getStockCount() <= 0) return;   //什么也不做，等用户轮询的时候发现已经卖完了 - . -
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodId(miaoshaUser.getId(), goodsId);
        if(miaoshaOrder != null) return;    //什么也不做，等到用户轮询的时候发现秒杀失败
        try {
            miaoshaService.doMiaosha(miaoshaUser, goodsVo); //前面判断完后，开始执行减库存下订单操作
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }
}
