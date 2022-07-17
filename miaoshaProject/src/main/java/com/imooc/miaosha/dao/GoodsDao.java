package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname GoodsDao
 * @Description TODO
 * @Date 2022/5/3 10:27
 * @Created by Eskii
 */
@Mapper
public interface GoodsDao {
    List<GoodsVo> goodsVoList();
    GoodsVo getById(@Param("id") long id);
    Goods getGoodsById(@Param("id") long id);
    int reduceMiaoshaStockById(@Param("goodsId") Long goodsId);
    int reduceGoodsStockById(@Param("goodsId") Long goodsId);
}
