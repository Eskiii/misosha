package com.imooc.miaosha.vo;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaUser;
import lombok.Data;

/**
 * @Classname GoodsDetailVo
 * @Description TODO
 * @Date 2022/5/10 20:20
 * @Created by Eskii
 */
@Data
public class GoodsDetailVo {
    private GoodsVo goodsVo;
    private MiaoshaUser miaoshaUser;
    private int miaoshaStatus;
    private int remainSeconds;
}
