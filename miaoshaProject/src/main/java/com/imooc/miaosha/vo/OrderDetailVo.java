package com.imooc.miaosha.vo;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.OrderInfo;
import lombok.Data;

/**
 * @Classname OrderDetailVo
 * @Description TODO
 * @Date 2022/5/11 9:23
 * @Created by Eskii
 */
@Data
public class OrderDetailVo {
    private Goods goods;
    private OrderInfo orderInfo;
}
