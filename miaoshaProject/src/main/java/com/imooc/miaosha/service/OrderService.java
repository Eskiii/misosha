package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2022/5/4 11:05
 * @Created by Eskii
 */
public interface OrderService {
    MiaoshaOrder getMiaoshaOrderByUserIdGoodId(Long userId, Long goodsId);
    OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo);
    OrderInfo getById(Long orderId);
}
