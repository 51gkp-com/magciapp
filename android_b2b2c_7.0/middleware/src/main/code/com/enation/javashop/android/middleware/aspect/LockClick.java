package com.enation.javashop.android.middleware.aspect;

import com.enation.javashop.android.jrouter.JRouter;
import com.enation.javashop.android.jrouter.external.annotation.Router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author  LDD
 * @Data   2018/1/5 上午10:56
 * @From   com.enation.javashop.android.lib.aspect
 * @Note   当onClick事件使用方法注入时 使用该注解可以防暴力测试
 */

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface LockClick {

}
