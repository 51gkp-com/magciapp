package com.enation.javashop.android.component.home.di

import com.enation.javashop.android.component.home.activity.HomeActivity
import com.enation.javashop.android.component.home.fragment.CategoryFragment
import com.enation.javashop.android.component.home.fragment.HomeFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/1/22 下午2:43
 * @From   com.enation.javashop.android.component.home.di
 * @Note   依赖注入入口
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface HomeComponent {

    fun inject(activity: HomeActivity)

    fun inject(activity: HomeFragment)

    fun inject(activity: CategoryFragment)
}