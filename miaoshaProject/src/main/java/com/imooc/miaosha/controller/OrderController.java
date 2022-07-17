package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.utils.Result;
import com.imooc.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname OrderController
 * @Description TODO
 * @Date 2022/5/11 9:08
 * @Created by Eskii
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    /**
     * 返回订单信息
     * @param miaoshaUser
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(MiaoshaUser miaoshaUser, @RequestParam("orderId") Long orderId) {
        if(miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long goodsId = orderInfo.getGoodsId();
        Goods goods = goodsService.getGoodsById(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderInfo(orderInfo);
        orderDetailVo.setGoods(goods);
        return Result.success(orderDetailVo);
    }
}
