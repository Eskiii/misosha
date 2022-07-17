package com.imooc.miaosha.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @Classname MiaoshaOrder
 * @Description TODO
 * @Date 2022/5/3 10:05
 * @Created by Eskii
 */

@Data
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;

    public MiaoshaOrder() {
    }

    public MiaoshaOrder(Long userId, Long orderId, Long goodsId) {
        this.userId = userId;
        this.orderId = orderId;
        this.goodsId = goodsId;
    }
}
