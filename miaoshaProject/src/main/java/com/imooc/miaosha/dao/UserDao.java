package com.imooc.miaosha.dao;

import com.imooc.miaosha.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname UserDao
 * @Description TODO
 * @Date 2022/4/26 15:20
 * @Created by Eskii
 */
@Mapper
public interface UserDao {
    User getById(@Param("id") int id);
    int insert(User user);
}
