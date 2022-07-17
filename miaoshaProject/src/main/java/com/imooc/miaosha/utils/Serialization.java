package com.imooc.miaosha.utils;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.entity.MiaoshaOrder;

/**
 * @Classname Serialization
 * @Description 序列化工具类
 * @Date 2022/4/27 11:42
 * @Created by Eskii
 */
public class Serialization {
    /**
     * 将一个 obj 对象序列化为字符串
     * @param value
     * @param <T>
     * @return obj 对应的字符串序列，可用于网络传输
     */
    public static <T> String objToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将字符串反序列化到 obj 对象
     * @param str
     * @param <T>
     * @return
     */
    public static  <T> T stringToObj(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static void main(String[] args) {
        MiaoshaOrder miaoshaOrder1 = new MiaoshaOrder();
        miaoshaOrder1.setUserId(13000000003L);
        miaoshaOrder1.setOrderId(1L);
        miaoshaOrder1.setGoodsId(1L);
        String s = objToString(miaoshaOrder1);
        System.out.println(s);
        MiaoshaOrder miaoshaOrder = stringToObj(s, MiaoshaOrder.class);
        System.out.println(miaoshaOrder);
    }
}
