package com.imooc.miaosha.service;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname GoodsService
 * @Description TODO
 * @Date 2022/5/3 10:40
 * @Created by Eskii
 */
public interface GoodsService {
    List<GoodsVo> goodsVoList();
    GoodsVo getById(long id);
    Goods getGoodsById(long id);
    boolean reduceStock(Long goodsId);
}
