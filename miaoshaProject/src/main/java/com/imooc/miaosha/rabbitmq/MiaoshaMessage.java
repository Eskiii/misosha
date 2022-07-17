package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.entity.MiaoshaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MiaoshaMessage
 * @Description 秒杀消息实体
 * @Date 2022/5/10 21:49
 * @Created by Eskii
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private Long goodsId;
}
