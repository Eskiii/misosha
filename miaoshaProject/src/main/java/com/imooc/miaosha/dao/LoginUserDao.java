package com.imooc.miaosha.dao;

import com.imooc.miaosha.vo.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname LoginUserDao
 * @Description TODO
 * @Date 2022/4/28 15:57
 * @Created by Eskii
 */
@Mapper
public interface LoginUserDao {
    LoginUser getUserById(@Param("id") Long id);
}
