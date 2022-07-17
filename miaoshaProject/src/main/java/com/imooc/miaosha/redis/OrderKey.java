package com.imooc.miaosha.redis;

/**
 * @Classname OrderKey
 * @Description TODO
 * @Date 2022/5/10 21:05
 * @Created by Eskii
 */
public class OrderKey extends BaseKeyPrefix{
    public OrderKey(String prefix) {
        super(0, prefix);
    }   //订单信息设置在redis中用不过期
    public static OrderKey MiaoshaOrderUidGid = new OrderKey("moug");
}
