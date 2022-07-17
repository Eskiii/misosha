package com.imooc.miaosha.config;

import com.imooc.miaosha.interceptor.MDCInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Classname WebConfig
 * @Description 自定义请求配置类
 * @Date 2022/4/30 15:29
 * @Created by Eskii
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentResolver userArgumentResolver;
    @Autowired
    MDCInterceptor mdcInterceptor;

    /**
     * 自定义参数解析器，在接收到请求时自动处理
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    public WebConfig(MDCInterceptor mdcInterceptor) {
        this.mdcInterceptor = mdcInterceptor;
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcInterceptor);
    }
}
