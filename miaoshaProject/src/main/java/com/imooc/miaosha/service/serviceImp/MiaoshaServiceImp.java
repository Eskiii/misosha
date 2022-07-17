package com.imooc.miaosha.service.serviceImp;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname MiaoshaServiceImp
 * @Description TODO
 * @Date 2022/5/3 20:12
 * @Created by Eskii
 */

@Service
public class MiaoshaServiceImp implements MiaoshaService {

    private final Logger logger = LoggerFactory.getLogger(MiaoshaServiceImp.class);

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    @Override
    @Transactional  //该方法必须是原子的，失败则回滚
    public OrderInfo doMiaosha(MiaoshaUser user, GoodsVo goodsVo) {
        if(goodsService.reduceStock(goodsVo.getId())) {  //减库存
            OrderInfo order = orderService.createOrder(user, goodsVo);//创建并写入订单表 order_info
            if(order == null) {
                logger.info(CodeMsg.MIAOSHA_FAIL.getMsg());
                return null;
            }
            return order;
        }
        logger.info(CodeMsg.MIAO_SHA_OVER.getMsg());
        return null;
    }

    /**
     * 获取某个给用户秒杀某个商品的秒杀结果
     * -1：秒杀失败
     * 0：还在排队中
     * 订单id：秒杀成功
     * @param userId
     * @param goodsId
     * @return
     */
    @Override
    public long getMiaoshaResult(Long userId, Long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodId(userId, goodsId);
//        Integer stock1 = redisService.get(GoodsKey.MiaoshaGoodsStock, "_" + 1, Integer.class);
//        System.out.println("stock1");
        if(miaoshaOrder == null) {
//            redisService.set(GoodsKey.MiaoshaGoodsStock, "_" + goodsVo.getId(), goodsVo.getStockCount());
            Integer stock = redisService.get(GoodsKey.MiaoshaGoodsStock, "_" + goodsId, Integer.class);

            if(stock != null) {
                logger.info("当前库存为空");
                int miaoshaGoodsStock = stock;  //查看当前秒杀是否结束
                if (miaoshaGoodsStock <= 0) {
                    return -1;
                }
                return 0;
            }
            System.out.println("stock ---->" + stock);
            return -1;
        } else {
            return miaoshaOrder.getOrderId();
        }
    }
}
