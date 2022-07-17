package com.imooc.miaosha.vo;

import com.imooc.miaosha.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Classname LoginUser
 * @Description 登录用户信息
 * @Date 2022/4/28 14:24
 * @Created by Eskii
 */
@Data
public class LoginUser {
    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
