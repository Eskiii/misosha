package com.imooc.miaosha.service;

import com.imooc.miaosha.vo.LoginUser;
import com.imooc.miaosha.entity.MiaoshaUser;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname LoginUserService
 * @Description 用户服务
 * @Date 2022/4/28 15:08
 * @Created by Eskii
 */
public interface LoginUserService {
    String COOKIE_NAME_TOKEN = "token";
    String login(HttpServletResponse response, LoginUser loginUser);
    MiaoshaUser getByToken(HttpServletResponse response, String token);
}
