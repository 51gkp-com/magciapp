package com.enation.javashop.android.component.setting.di

import com.enation.javashop.android.component.setting.activity.SettingActivity
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/2/24 下午3:54
 * @From   com.enation.javashop.android.component.setting.di
 * @Note   设置模块依赖注入入口
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface SettingComponent {
    fun inject(fragment: SettingActivity)
}