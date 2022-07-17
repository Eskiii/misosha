package com.imooc.miaosha.redis;

/**
 * @Classname KeyPrefix
 * @Description Redis 的 key 前缀，区分不同数据
 * @Date 2022/4/26 21:47
 * @Created by Eskii
 */
public interface KeyPrefix {
    int getExpireSeconds();
    String getPrefix();
}
