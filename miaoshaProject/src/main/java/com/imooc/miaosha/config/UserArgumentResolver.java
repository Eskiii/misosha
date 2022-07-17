package com.imooc.miaosha.config;

import com.imooc.miaosha.entity.MiaoshaUser;
import com.imooc.miaosha.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname UserArgumentResolver
 * @Description 处理请求参数中包含 MiaoshaUser 的解析器
 * @Date 2022/4/30 15:42
 * @Created by Eskii
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    LoginUserService userService;

    /**
     * 所支持的方法参数类型（即controll中的方法参数），即如果参数包含 MiaoshaUser.class，才启用当前的解析器
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterClazz = parameter.getParameterType();
        return parameterClazz == MiaoshaUser.class;
    }

    /**
     * 真正的解析方法
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return  根据 cookie/token 返回 MiaoshaUser
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeResponse = webRequest.getNativeResponse(HttpServletResponse.class);
        // 获取请求参数中的 cookie
        String rpCookieValue = nativeRequest.getParameter(LoginUserService.COOKIE_NAME_TOKEN);
        // 获取独立放置的 cookie
        Cookie[] cookies = nativeRequest.getCookies();
        String cookieValue = "";
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (LoginUserService.COOKIE_NAME_TOKEN.equals(cookie.getName())) {  // 寻找名字为 “token” 的 cookie 值
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        if(StringUtils.isEmpty(rpCookieValue) && StringUtils.isEmpty(cookieValue)) {  // 没有 cookie 则
            return null;
        }
        String token = StringUtils.isEmpty(rpCookieValue) ? cookieValue : rpCookieValue;
        return userService.getByToken(nativeResponse, token);   //去缓存中查找相应的用户信息
    }
}
