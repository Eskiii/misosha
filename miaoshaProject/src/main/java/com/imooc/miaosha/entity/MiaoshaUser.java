package com.imooc.miaosha.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Classname MiaoshaUser
 * @Description TODO
 * @Date 2022/4/28 15:15
 * @Created by Eskii
 */
@Data
public class MiaoshaUser {
    private Long id;    // mobile 作为 id
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
