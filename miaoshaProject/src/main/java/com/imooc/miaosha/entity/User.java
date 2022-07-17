package com.imooc.miaosha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Classname User
 * @Description User实体，对应 user 表的一条记录，一个POJO对象
 * @Date 2022/4/26 10:51
 * @Created by Eskii
 */
@Data
@AllArgsConstructor
public class User {
    private int id;
    private String name;
}
