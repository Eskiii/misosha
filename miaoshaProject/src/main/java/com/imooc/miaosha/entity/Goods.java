package com.imooc.miaosha.entity;

import lombok.Data;

/**
 * @Classname Goods
 * @Description TODO
 * @Date 2022/5/3 10:05
 * @Created by Eskii
 */

@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
