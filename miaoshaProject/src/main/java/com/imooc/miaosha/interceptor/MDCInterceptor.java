package com.imooc.miaosha.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Classname MDCInterceptor
 * @Description TODO
 * @Date 2022/5/8 20:45
 * @Created by Eskii
 */

@Slf4j
@Component
public class MDCInterceptor implements HandlerInterceptor {
    private final static String TRACE_ID = "trace_id";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        MDC.remove(TRACE_ID);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
