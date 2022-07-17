package com.imooc.miaosha.validator;

import com.imooc.miaosha.utils.ValidatorUtil;
import com.mysql.cj.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Classname MobileValidator
 * @Description TODO
 * @Date 2022/4/28 20:03
 * @Created by Eskii
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) { //该方法在注解开始校验前运行，获取注解参数
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) { // 如果 value 是必须的，则校验格式
            return ValidatorUtil.isMobile(value);
        } else {    // 如果 value 不是必须的
            if (StringUtils.isEmptyOrWhitespaceOnly(value)) {   // 不是必须的并且 value 是空的，那么直接返回true
                return true;
            } else {
                return ValidatorUtil.isMobile(value);   // 否则校验格式
            }
        }
    }
}
