package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname MiaoshaUserDao
 * @Description TODO
 * @Date 2022/4/28 16:00
 * @Created by Eskii
 */
@Mapper
public interface MiaoshaUserDao {
    MiaoshaUser getUserById(@Param("id") Long id);
    void update(MiaoshaUser user);
}
