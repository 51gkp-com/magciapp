package com.enation.javashop.android.component.cart.di

import com.enation.javashop.android.component.cart.fragment.CartFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/1/30 下午2:35
 * @From   com.enation.javashop.android.component.cart.di
 * @Note   Cart模块依赖注入
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface CartComponent {
    fun inject(fragment : CartFragment)
}