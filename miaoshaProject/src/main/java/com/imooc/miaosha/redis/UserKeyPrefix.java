package com.imooc.miaosha.redis;

/**
 * @Classname UserKeyPrefix
 * @Description TODO
 * @Date 2022/4/26 21:53
 * @Created by Eskii
 */
public class UserKeyPrefix extends BaseKeyPrefix{

    private UserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKeyPrefix getById = new UserKeyPrefix(30, "id");
    public static UserKeyPrefix getByName = new UserKeyPrefix(30, "name");
}
