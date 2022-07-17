package com.imooc.miaosha.utils;

import java.util.UUID;

/**
 * @Classname UUIDUtil
 * @Description TODO
 * @Date 2022/4/28 22:01
 * @Created by Eskii
 */
public class UUIDUtil {

    /**
     * 获取 UUID
     * @return 一个随机生成的 UUID
     */
    public static String getUUID() {
        // 将 UUID 原字符串中的 - 字符去除
        return UUID.randomUUID().toString().replace("-", "");
    }
}
