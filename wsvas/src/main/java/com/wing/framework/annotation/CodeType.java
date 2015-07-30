package com.wing.framework.annotation;

import java.lang.annotation.*;

/**
 *
 * 代码反显注解
 *
 * @author: panwb
 *
 * Date: 2014/7/10
 * Time: 15:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CodeType {
    String id();
}