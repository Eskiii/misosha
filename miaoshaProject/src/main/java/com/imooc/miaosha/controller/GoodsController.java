package com.imooc.miaosha.controller;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.LoginUserService;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.utils.Result;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Classname GoodsController
 * @Description TODO
 * @Date 2022/4/30 14:35
 * @Created by Eskii
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    LoginUserService userService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    RedisService redisService;
    @Autowired
    ServletContext servletContext;

//    /**
//     * 压测参数：5000 * 10 请求
//     * 压测性能：3,367.003 qps
//     *          load 29
//     *
//     * 注意方法参数中的 miaoshaUser 是使用了自定义解析器 UserArgumentResolver 处理后得到的
//     * @param model
//     * @param miaoshaUser
//     * @return
//     */
//    @RequestMapping("/to_list")
//    public String toList(Model model, MiaoshaUser miaoshaUser) {
//        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
//        model.addAttribute("goodsList", goodsVoList);
//        return "goods_list";    //跳转转发到 goods_list 页面
//    }

//    /**
//     * 压测参数：5000 * 10
//     * 压测性能：3,212.955 qps
//     *          load 26
//     *
//     * @param model
//     * @param miaoshaUser
//     * @param goodsId
//     * @return
//     */
//    @RequestMapping("/detail/{goodsId}")
//    public String goodsDetail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId") long goodsId) {
//        GoodsVo good = goodsService.getById(goodsId);
//        TimeZone aDefault = TimeZone.getDefault();
//        logger.info("当前系统的时区是：" + aDefault);
//        logger.info("goodsId = " + goodsId +": " + good);
//        logger.info("goodsImg = " + good.getGoodsImg());
//        logger.info("user = " + miaoshaUser);
//        int remainSeconds = -1;
//        int miaoshaStatus = 2;
//        if(good != null) {
//            long startTime = good.getStartDate().getTime();
//            long endTime = good.getEndDate().getTime();
//            long curTime = System.currentTimeMillis();
//            logger.info("当前系统时间 = " + curTime);
//            logger.info("startTime = " + startTime);
//            logger.info("endTime = " + endTime);
//            if (curTime < startTime) {
//                miaoshaStatus = 0;
//                remainSeconds = (int) (startTime - curTime) / 1000; // milliseconds to seconds
//            } else if (curTime > endTime) {
//                miaoshaStatus = 2;
//                remainSeconds = -1;
//            } else {
//                miaoshaStatus = 1;
//                remainSeconds = 0;
//            }
//        }
//        logger.info("miaoshaStatus = " + miaoshaStatus);
//        logger.info("remainSeconds = " + remainSeconds);
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("user", miaoshaUser);
//        model.addAttribute("goods", good);
//        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";
//    }

    /**
     * 压测参数：5000 * 10
     * 压测性能：7,871.537 qps （第一次压测作为热身）
     *          load 2.4
     *
     * 注意方法参数中的 miaoshaUser 是使用了自定义解析器 UserArgumentResolver 处理后得到的
     * 该方法进行了页面静态缓存优化，优化后的qps是之前的三倍多
     * @param model
     * @param miaoshaUser
     * @return
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         Locale locale,
                         Model model,
                         MiaoshaUser miaoshaUser) {
        // 先查redis中是否缓存了该页面
        String goods_list_html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (goods_list_html != null) {
            return goods_list_html;
        }

        //获取商品列表数据
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsVoList);

        //如果没有缓存则手动渲染
        WebContext webContext = new WebContext(httpServletRequest, httpServletResponse, servletContext, locale, model.asMap());
        goods_list_html = thymeleafViewResolver.getTemplateEngine().process("goods_list", webContext);

        redisService.set(GoodsKey.getGoodsList, "", goods_list_html);   //将渲染网页数据缓存在redis中

        return goods_list_html;    //手动渲染的 goods_list 页面
    }

//    /**
//     * 压测参数：5000 * 10
//     * 压测性能：6,622.517 qps
//     *          load 1.3
//     * 进行了页面静态缓存优化
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @param locale
//     * @param model
//     * @param miaoshaUser
//     * @param goodsId
//     * @return
//     */
//    @RequestMapping(value = "/detail/{goodsId}", produces = "text/html")
//    @ResponseBody
//    public String goodsDetail(HttpServletRequest httpServletRequest,
//                              HttpServletResponse httpServletResponse,
//                              Locale locale,
//                              Model model,
//                              MiaoshaUser miaoshaUser,
//                              @PathVariable("goodsId") long goodsId) {
//        //先查缓存中有没有，如果有则直接返回
//        String goods_detail_html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
//        if(goods_detail_html != null) {
//            return goods_detail_html;
//        }
//        // 获取页面的动态数据
//        GoodsVo good = goodsService.getById(goodsId);
//        int remainSeconds = -1;
//        int miaoshaStatus = 2;
//        if(good != null) {
//            long startTime = good.getStartDate().getTime();
//            long endTime = good.getEndDate().getTime();
//            long curTime = System.currentTimeMillis();
//            if (curTime < startTime) {
//                miaoshaStatus = 0;
//                remainSeconds = (int) (startTime - curTime) / 1000; // milliseconds to seconds
//            } else if (curTime > endTime) {
//                miaoshaStatus = 2;
//                remainSeconds = -1;
//            } else {
//                miaoshaStatus = 1;
//                remainSeconds = 0;
//            }
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("user", miaoshaUser);
//        model.addAttribute("goods", good);
//        model.addAttribute("remainSeconds", remainSeconds);
//
//        //手动渲染
//        WebContext webContext = new WebContext(httpServletRequest, httpServletResponse, servletContext, locale, model.asMap());
//        goods_detail_html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
//
//        //缓存页面数据
//        redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, goods_detail_html);
//
//        return goods_detail_html;
//    }

    /**
     *
     * 进行了页面的前后端分离
     * @param httpServletRequest
     * @param httpServletResponse
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> goodsDetail(HttpServletRequest httpServletRequest,
                                             HttpServletResponse httpServletResponse,
                                             MiaoshaUser miaoshaUser,
                                             @PathVariable("goodsId") long goodsId) {
        if(miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 获取页面的动态数据
        GoodsVo good = goodsService.getById(goodsId);
        int remainSeconds = -1;
        int miaoshaStatus = 2;
        if(good != null) {
            long startTime = good.getStartDate().getTime();
            long endTime = good.getEndDate().getTime();
            long curTime = System.currentTimeMillis();
            if (curTime < startTime) {
                miaoshaStatus = 0;
                remainSeconds = (int) (startTime - curTime) / 1000; // milliseconds to seconds
            } else if (curTime > endTime) {
                miaoshaStatus = 2;
                remainSeconds = -1;
            } else {
                miaoshaStatus = 1;
                remainSeconds = 0;
            }
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(good);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setMiaoshaUser(miaoshaUser);
        return Result.success(goodsDetailVo);
    }
}
