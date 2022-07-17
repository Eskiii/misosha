package com.imooc.miaosha.redis;

/**
 * @Classname GoodsKey
 * @Description TODO
 * @Date 2022/5/6 15:24
 * @Created by Eskii
 */
public class GoodsKey extends BaseKeyPrefix{

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodsList");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "goodsDetail");
    public static GoodsKey MiaoshaGoodsStock = new GoodsKey(0, "miaoshaGoodsStock");

}
