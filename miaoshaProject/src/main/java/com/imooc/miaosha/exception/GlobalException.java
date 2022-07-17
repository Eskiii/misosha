package com.imooc.miaosha.exception;

import com.imooc.miaosha.utils.CodeMsg;

/**
 * @Classname GlobalException
 * @Description 处理 CodeMsg 中定义的异常
 * @Date 2022/4/28 21:11
 * @Created by Eskii
 */
public class GlobalException extends RuntimeException{
    private CodeMsg codeMsg;
    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
