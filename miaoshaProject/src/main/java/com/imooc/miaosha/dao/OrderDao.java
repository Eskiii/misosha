package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname OrderDao
 * @Description TODO
 * @Date 2022/5/4 11:31
 * @Created by Eskii
 */

@Mapper
public interface OrderDao {
    MiaoshaOrder getMiaoshaOrderByUserIdGoodId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
    Long insertOrder(OrderInfo orderInfo);   // 返回所插入成功的order id
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
    OrderInfo getById(@Param("orderId") Long orderId);
}
