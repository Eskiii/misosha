package com.imooc.miaosha.vo;

import com.imooc.miaosha.entity.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @Classname GoodsVo
 * @Description TODO
 * @Date 2022/5/3 10:23
 * @Created by Eskii
 */
@Data
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
