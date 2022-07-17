package com.imooc.miaosha.utils;

import com.mysql.cj.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ValidatorUtil
 * @Description 信息校验工具
 * @Date 2022/4/28 17:09
 * @Created by Eskii
 */
public class ValidatorUtil{

    // 以 1 开头的长度为 10 的字符串就检测为手机号
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    /**
     *  检验 str 符不符合手机号格式
     * @param str 手机号
     * @return
     */
    public static boolean isMobile(String str) {
        if(StringUtils.isNullOrEmpty(str)) {
            return false;
        }
        return mobile_pattern.matcher(str).matches();
    }
}
