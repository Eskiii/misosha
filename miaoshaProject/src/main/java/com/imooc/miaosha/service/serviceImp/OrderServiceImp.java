package com.imooc.miaosha.service.serviceImp;

import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Classname OrderServiceImp
 * @Description TODO
 * @Date 2022/5/4 11:06
 * @Created by Eskii
 */

@Service
public class OrderServiceImp implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodId(Long userId, Long goodsId) {
//        return orderDao.getMiaoshaOrderByUserIdGoodId(userId, goodsId);
        return redisService.get(OrderKey.MiaoshaOrderUidGid, userId + "_" + goodsId, MiaoshaOrder.class);
    }

    /**
     * 执行逻辑是：（1）先根据传进来的用户信息和业务秒杀商品信息创建出订单信息实体
     *            （2）将订单信息插入到订单信息表 order_info 中，并且返回插入成功的订单编号 id
     *            （3）将返回的订单编号id 、用户id和商品id创建一个秒杀订单实体
     *            （4）将秒杀订单插入到秒杀订单表miaosha_order 表中
     *
     * 注意：由于miaosha_order表添加了 (user_id, goods_id) 的唯一索引，所以如果同一个用户的两个相同的请求（秒杀同一个商品）
     *      同时到了这个方法，那么在向miaosha_order表中插入订单时就会出现(user_id, goods_id)重复的错误（java.sql.SQLIntegrityConstraintViolationException）
     *      那么这个事务就会进行回滚，从而另外一个秒杀请求就会失败。
     *      于是就解决了用户重复秒杀的问题。
     * @param miaoshaUser
     * @param goodsVo
     * @return
     */
    @Override
    @Transactional  //创建和写入订单是原子操作，失败则回滚
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo(miaoshaUser.getId(), goodsVo.getId(), goodsVo.getGoodsName(), goodsVo.getStockCount(), goodsVo.getMiaoshaPrice(), 0, new Date());
        orderDao.insertOrder(orderInfo);// 插入order信息，orderId返回到了orderInfo中
        logger.info("orderInfo.orderId = " + orderInfo.getId()); //这种方式才能获取到真实的订单编号 id

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder(miaoshaUser.getId(), orderInfo.getId(), goodsVo.getId());
        int success = orderDao.insertMiaoshaOrder(miaoshaOrder);//同时将订单信息插入到秒杀订单表中
        if(success > 0) {
            redisService.set(OrderKey.MiaoshaOrderUidGid, miaoshaUser.getId() + "_" + goodsVo.getId(), miaoshaOrder);  //将订单缓存到redis中
            return orderInfo;
        }
        return null;
    }

    @Override
    public OrderInfo getById(Long orderId) {
        return orderDao.getById(orderId);
    }
}
