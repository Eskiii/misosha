package com.imooc.miaosha.controller;

import com.imooc.miaosha.dao.MiaoshaDao;
import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.entity.MiaoshaOrder;
import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.entity.OrderInfo;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.rabbitmq.RabbitmqSender;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.utils.Result;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname MiaoshaController
 * @Description TODO
 * @Date 2022/5/4 11:06
 * @Created by Eskii
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

    private final Logger logger = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    RabbitmqSender rabbitmqSender;

    //标记商品是否卖完
    ConcurrentHashMap<Long, Boolean> goodsSold = new ConcurrentHashMap<>();

    /**
     * 该方法会在容器将 MiaoshaController 加入后，进行自己的一些初始化操作，优先于该 bean被使用前
     * 初始化操作：将商品id及商品库存加载在到redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        if (goodsVoList == null) return;
        for(GoodsVo goodsVo:goodsVoList) {
            redisService.set(GoodsKey.MiaoshaGoodsStock, "_" + goodsVo.getId(), goodsVo.getStockCount());
            if(goodsVo.getStockCount() > 0) {
                goodsSold.put(goodsVo.getId(), true);
            } else {
                goodsSold.put(goodsVo.getId(), false);
            }
        }
    }

    /**
     * 压测参数：5000 * 10
     * 压测性能：2,723.46 qps
     *          load 23
     *
     * 秒杀方法
     * 主要步骤：（1）检查用户信息是否不为空，即登录是否过期，过期直接跳转回登录页
     *          （2）检查是否在秒杀时间区间内
     *          （3）检查库存是否充足
     *          （4）检查用户是否已经秒杀过该商品
     *          （5）执行秒杀操作：减库存，创建订单
     * @param model
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @RequestMapping("/do_miaosha1")
    public String doMiaosha1(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long goodsId) {     //减库存 下订单

        if(miaoshaUser == null) {   //直接跳转到登录页
            logger.info("获取miaoshaUser为空！");
            return "login";
        }
        //检查秒杀时间是否已经结束
        GoodsVo goodsVo = goodsService.getById(goodsId);
//        long endTime = goodsVo.getEndDate().getTime();
//        long curTime = System.currentTimeMillis();
//        if(curTime > endTime) {
//            logger.info(CodeMsg.MIAO_SHA_OVER.getMsg());
//            model.addAttribute("miaoshaStatus", 2);
//            model.addAttribute("user", miaoshaUser);
//            model.addAttribute("goods", goodsVo);
//            model.addAttribute("remainSeconds", -1);
//            return "goods_detail";
//        }

        // 查库存
        Integer stockCount = goodsVo.getStockCount();
        if(stockCount <= 0) {   //当前商品库存不足，跳转秒杀失败页面
            logger.info(CodeMsg.MIAO_SHA_END.getMsg());
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return "miaosha_fail";
        }
        else {
            logger.info(CodeMsg.REPEATE_MIAOSHA.getMsg());
            MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodId(miaoshaUser.getId(), goodsId);
            if (order != null) {    //判断当前用户是否已经秒杀过该商品，不能重复秒杀
                model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA);
                return "miaosha_fail";
            }
            else {    //当前库存充足并且用户没有秒杀过该商品，则执行减库存下订单操作（原子操作），并跳转订单详情页
                OrderInfo orderInfo = miaoshaService.doMiaosha(miaoshaUser, goodsVo);   //有订单信息返回表示秒杀成功
                if(orderInfo != null) {
                    model.addAttribute("orderInfo", orderInfo);
                    model.addAttribute("goods", goodsVo);
                    logger.info(CodeMsg.MIAO_SHA_SUCCESS.getMsg());
                    return "order_detail";
                }
                //返回订单信息为空则秒杀失败
                model.addAttribute("errmsg", CodeMsg.MIAOSHA_FAIL);
                return "miaosha_fail";
            }
        }
    }

    /**
     * 压测参数：5000 * 10
     * 压测性能：3,839.361 qps
     *          load 1.08
     *
     *
     * 秒杀方法优化：redis预减库存 + rabbitmq消息队列，实现异步秒杀操作。可以看到qps的提升似乎没有多少，但是对服务器的负载减轻了很多
     *
     * 主要步骤：（1）检查redis中是否已经缓存了相应的订单信息，防止用户重复秒杀
     *          （2）先查询redis中的库存，库存不足则直接返回失败
     *          （3）构造秒杀消息实体，加入消息队列中，通知用户 “正在排队中”，但还没有执行真正的下单操作
     *          （4）消息接收者进行后续的秒杀操作，用户会进行轮询操作，即不断地向服务器请求查询是否已经完成了下单操作
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping("/do_miaosha")
    public Result<Integer> doMiaosha(MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long goodsId) {     //减库存 下订单

        if(miaoshaUser == null) {   //直接跳转到登录页
            logger.info("获取miaoshaUser为空！");
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodId(miaoshaUser.getId(), goodsId);  //检查redis是否已经缓存了订单
        if (order != null) {    //判断当前用户是否已经秒杀过该商品，不能重复秒杀
            logger.info(CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        } else {
            Long stock = redisService.decr(GoodsKey.MiaoshaGoodsStock, "_" + goodsId); //redis预减库存
            if(stock < 0) {
                logger.info(CodeMsg.MIAO_SHA_OVER.getMsg());
                return Result.error(CodeMsg.MIAO_SHA_OVER);
            } else {
                //当前库存充足并且用户没有秒杀过该商品，则构造消息实体，插入到消息队列中，将秒杀操作交给消息的接收者
                MiaoshaMessage message = new MiaoshaMessage(miaoshaUser, goodsId);
                rabbitmqSender.miaoshaSender(message);
                return Result.success(0);   // 通知用户排队中，等待完成秒杀的异步执行操作
            }
        }
    }

    /**
     * 用户轮询该接口，以查询秒杀结果
     *
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @ResponseBody
    @GetMapping("/result")
    public Result<Long> getResult(MiaoshaUser miaoshaUser, @RequestParam("goodsId") Long goodsId) {
        logger.info("user = " + miaoshaUser.getId() + "goodsId = " + goodsId);
        long miaoshaResult = miaoshaService.getMiaoshaResult(miaoshaUser.getId(), goodsId);
        logger.info("miaoshaResult : " + miaoshaResult);
        return Result.success(miaoshaResult);
    }
}
