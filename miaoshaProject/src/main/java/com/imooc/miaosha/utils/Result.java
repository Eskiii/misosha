package com.imooc.miaosha.utils;

/**
 * @Classname Result
 * @Description 网络传输信息实体封装，包括 自定义状态码 code，消息 msg ，数据 data
 * @Date 2022/4/26 9:43
 * @Created by Eskii
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> Result<T> success(T data){   // code = 0
        return new Result<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(CodeMsg codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }


    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
