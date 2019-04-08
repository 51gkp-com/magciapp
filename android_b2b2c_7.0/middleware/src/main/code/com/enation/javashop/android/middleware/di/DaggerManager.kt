package com.enation.javashop.android.middleware.di

/**
 * @author LDD
 * @Date   2018/1/15 下午2:48
 * @From   com.enation.javashop.android.middleware.di
 * @Note   提供
 */
object DaggerManager {

    /**
     * @Name  applicationComponent
     * @Type  com.enation.javashop.android.lib.di.ApplicationComponent
     * @Note  基础Application注入模块
     */
    val APPLICATION_COMPONENT: ApplicationComponent by lazy { DaggerApplicationComponent.builder().apiProvides(ApiProvides()).build()}
}