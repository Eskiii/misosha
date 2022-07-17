package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.User;
import org.springframework.stereotype.Service;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/4/26 15:24
 * @Created by Eskii
 */
public interface UserService {
    User getById(int id);
    boolean transactionTest();
}
