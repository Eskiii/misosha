package com.imooc.miaosha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.ConstructorArgs;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Classname OrderInfo
 * @Description TODO
 * @Date 2022/5/3 10:05
 * @Created by Eskii
 */

@Data
//@RequiredArgsConstructor    // 构建一个包含private final 字段的有参构造器
@AllArgsConstructor
//@NoArgsConstructor
public class OrderInfo {
    private Long id;
    private final Long userId;
    private final Long goodsId;
    private Long  deliveryAddrId;
    private final String goodsName;
    private final Integer goodsCount;
    private final Double goodsPrice;
    private Integer orderChannel;
    private final Integer status;
    private final Date createDate;
    private Date payDate;

    public OrderInfo(Long userId, Long goodsId, String goodsName, Integer goodsCount, Double goodsPrice, Integer status, Date createDate) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsCount = goodsCount;
        this.goodsPrice = goodsPrice;
        this.status = status;
        this.createDate = createDate;
    }
}
