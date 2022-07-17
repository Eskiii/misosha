package com.imooc.miaosha.service.serviceImp;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.entity.Goods;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Classname GoodsServiceImp
 * @Description TODO
 * @Date 2022/5/3 10:40
 * @Created by Eskii
 */

@Service
public class GoodsServiceImp implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public List<GoodsVo> goodsVoList() {
        return goodsDao.goodsVoList();
    }

    @Override
    public GoodsVo getById(long id) {
        return goodsDao.getById(id);
    }

    @Override
    public Goods getGoodsById(long id) {
        return goodsDao.getGoodsById(id);
    }

    @Override
    @Transactional
    public boolean reduceStock(Long goodsId) {
        //两个表的库存都要一起减
        Goods goods = goodsDao.getGoodsById(goodsId);
        if(goods != null) {
            return goodsDao.reduceMiaoshaStockById(goodsId) > 0 && goodsDao.reduceGoodsStockById(goodsId) > 0;
        }
        return false;
    }

}
