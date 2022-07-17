package com.imooc.miaosha.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Classname MiaoshaGoods
 * @Description TODO
 * @Date 2022/5/3 10:05
 * @Created by Eskii
 */

@Data
public class MiaoshaGoods {
    private Long id;
    private Long goodsId;
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
