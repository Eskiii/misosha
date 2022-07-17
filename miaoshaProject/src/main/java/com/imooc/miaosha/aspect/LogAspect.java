package com.imooc.miaosha.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname LogAspect
 * @Description TODO
 * @Date 2022/5/8 20:43
 * @Created by Eskii
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Pointcut("execution(* com.imooc.miaosha.controller..*.*(..))")
    public void pointCut() {
    }

    @Pointcut("execution(* com.imooc.miaosha.exception..*.*(..))")
    public void exceptionPointCut() {
    }

    @Before(value = "pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[请求接口] - {} : {} : {}", ip, method, uri);
        if (log.isDebugEnabled()) {
            Object[] parameterValues = joinPoint.getArgs();
            int parameterValuesLength = parameterValues.length;
            if (parameterValuesLength == 0) {
                log.debug("[请求参数] - 无");
                return;
            }
            Signature signature = joinPoint.getSignature();
            if (Objects.isNull(signature)) {
                return;
            }
            String[] parameterNames = ((CodeSignature) signature).getParameterNames();
            if (Objects.isNull(parameterNames) || parameterNames.length != parameterValuesLength) {
                return;
            }
            Map<String, Object> params = new HashMap<>(4);
            for (int i = 0; i < parameterNames.length; i++) {
                params.put(parameterNames[i], parameterValues[i]);
            }
            log.debug("[请求参数] - {}", JSON.toJSONString(params, SerializerFeature.WriteMapNullValue));
        }
    }

    @AfterReturning(returning = "result", pointcut = "pointCut() || exceptionPointCut()")
    public void doAfterReturning(Object result) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            return;
        }
        HttpServletResponse response = servletRequestAttributes.getResponse();
        if (Objects.isNull(response)) {
            return;
        }
        int status = response.getStatus();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[响应结果] - {} : {} : {}", status, method, uri);
        if (log.isDebugEnabled()) {
            log.debug("[响应内容] - {}", JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        }
    }
}
