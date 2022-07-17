package com.imooc.miaosha.exception;

import com.imooc.miaosha.utils.CodeMsg;
import com.imooc.miaosha.utils.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @Classname GlobalExceptionHandler
 * @Description 拦截系统中出现的 exception，并统一处理
 * @Date 2022/4/28 20:22
 * @Created by Eskii
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception exception) {
        exception.printStackTrace();
        if(exception instanceof BindException) {    // 参数绑定错误
            BindException bindException = (BindException) exception;
            List<ObjectError> allErrors = bindException.getAllErrors(); //可能不止一个参数绑定错误
            StringBuilder errorInfo = new StringBuilder();
            for (ObjectError oe : allErrors) {  // 将所有错误信息拼接为一个字符串
                errorInfo.append(oe.getDefaultMessage() + "\n");
            }
            errorInfo.deleteCharAt(errorInfo.length() - 1); // 处理最后一个字符
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(errorInfo));
        }
        else if (exception instanceof GlobalException) {  // 其他 CodeMsg 中已定义的异常
            GlobalException globalException = (GlobalException) exception;
            return Result.error(globalException.getCodeMsg());
        }
        else if(exception instanceof SQLIntegrityConstraintViolationException) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        else {    // 其他系统未处理错误
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
