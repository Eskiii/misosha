package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.User;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022/4/26 15:28
 * @Created by Eskii
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getById/{id}")
    public Result<User> getUserById(@PathVariable("id") int id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @RequestMapping("/tx")
    public Result<Boolean> tx() {
        userService.transactionTest();
        return Result.success(true);
    }
}
