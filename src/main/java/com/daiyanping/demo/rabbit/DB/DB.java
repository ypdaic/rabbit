package com.daiyanping.demo.rabbit.DB;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DB {

    DBTypeEnum value() default DBTypeEnum.DB1;
}
