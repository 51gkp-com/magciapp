package com.enation.javashop.android.middleware.di

/**
 * @author LDD
 * @Date   2018/1/19 下午6:22
 * @From   com.enation.javashop.android.middleware.di
 * @Note   中间件 依赖注入模块管理类
 */
object MiddlewareDaggerComponent {

    /**
     * @Name  com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent.component
     * @Type  com.enation.javashop.android.middleware.di.PresenterComponent
     * @Note  逻辑控制器依赖注入模块
     */
    val component :PresenterComponent by lazy { DaggerPresenterComponent.builder().applicationComponent(DaggerManager.APPLICATION_COMPONENT).build() }
}