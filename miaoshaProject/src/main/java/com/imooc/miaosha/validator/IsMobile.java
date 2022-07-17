package com.imooc.miaosha.validator;

import com.imooc.miaosha.utils.ValidatorUtil;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * @Classname IsMobile
 * @Description TODO
 * @Date 2022/4/28 15:57
 * @Created by Eskii
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(NotNull.List.class)
@Documented
@Constraint(
        validatedBy = {MobileValidator.class}   //校验类
)
public @interface IsMobile {

    boolean required() default true;    //值是必须有的

    String message() default "手机号码格式不正确！";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
