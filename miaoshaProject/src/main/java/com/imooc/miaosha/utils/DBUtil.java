package com.imooc.miaosha.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @Classname DBUtil
 * @Description TODO
 * @Date 2022/5/5 17:27
 * @Created by Eskii
 */
@Component
public class DBUtil {
//    @Value("${spring.datasource.url}")
    private static String url = "jdbc:mysql://192.168.10.138:3306/miaosha?&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false";
//    @Value("${spring.datasource.username}")
    private static String username = "root";
//    @Value("${spring.datasource.password}")
    private static String password = "root";
//    @Value("${spring.datasource.driver-class-name}")
    private static String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getConn() throws Exception{
        Class.forName(driver);
        return DriverManager.getConnection(url,username, password);
    }
}
