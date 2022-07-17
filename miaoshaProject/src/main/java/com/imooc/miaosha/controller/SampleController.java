package com.imooc.miaosha.controller;

//import com.imooc.miaosha.rabbitmq.RabbitmqSender;
import com.imooc.miaosha.rabbitmq.RabbitmqSender;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKeyPrefix;
import com.imooc.miaosha.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Classname SampleController
 * @Description TODO
 * @Date 2022/4/25 19:32
 * @Created by Eskii
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    private final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    RabbitmqSender rabbitmqSender;



    @RequestMapping("/mqTest")
    @ResponseBody
    public Result<String> ampqTest(HttpServletRequest httpServletRequest) {
        logger.info(httpServletRequest.toString());
        String msg = "hello, i am rabbit!";
        rabbitmqSender.send(msg);
        return Result.success(msg);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "eskii");
        return "hello";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "eskii");
        return "test";
    }

    @GetMapping("/redis/get")
    public Result<Long> redisGet() {
        Long num = redisService.get("key3", Long.class);
        return Result.success(num);
    }

    @RequestMapping("/redis/set")
    public Result<String> redisSet() {
        redisService.set("key2", "hello world");
        String str = redisService.get("key2", String.class);
        return Result.success(str);
    }
}
