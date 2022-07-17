package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.utils.Result;
import com.imooc.miaosha.vo.GoodsVo;

/**
 * @Classname MiaoshaService
 * @Description TODO
 * @Date 2022/5/3 20:12
 * @Created by Eskii
 */
public interface MiaoshaService {

    OrderInfo doMiaosha(MiaoshaUser user, GoodsVo goodsVo);

    long getMiaoshaResult(Long userId, Long goodsId);
}
