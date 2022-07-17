package com.imooc.miaosha.redis;

import lombok.Data;

/**
 * @Classname BaseKeyPrefix
 * @Description TODO
 * @Date 2022/4/26 21:49
 * @Created by Eskii
 */
public abstract class BaseKeyPrefix implements KeyPrefix{
    private int expireSeconds; // 0 代表永不过期
    private String prefix;

    public BaseKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
