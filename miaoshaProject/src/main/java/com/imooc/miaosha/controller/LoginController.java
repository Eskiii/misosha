package com.imooc.miaosha.controller;

import com.imooc.miaosha.vo.LoginUser;
import com.imooc.miaosha.service.LoginUserService;
import com.imooc.miaosha.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Classname LoginController
 * @Description 登录 service
 * @Date 2022/4/28 11:28
 * @Created by Eskii
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginUserService loginUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginUser loginUser) {
        logger.info("用户 --> " + loginUser.toString());
        return Result.success(loginUserService.login(response, loginUser));
    }
}
