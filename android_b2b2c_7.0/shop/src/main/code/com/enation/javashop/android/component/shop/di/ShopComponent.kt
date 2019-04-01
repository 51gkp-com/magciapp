package com.enation.javashop.android.component.shop.di

import com.enation.javashop.android.component.shop.activity.ShopActivity
import com.enation.javashop.android.component.shop.activity.ShopCategoryActivity
import com.enation.javashop.android.component.shop.activity.ShopInfoActivity
import com.enation.javashop.android.component.shop.activity.ShopListActivity
import com.enation.javashop.android.component.shop.fragment.ShopAllFragment
import com.enation.javashop.android.component.shop.fragment.ShopHomeFragment
import com.enation.javashop.android.component.shop.fragment.ShopTagFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/4/8 下午2:08
 * @From   com.enation.javashop.android.component.shop.di
 * @Note   店铺依赖注入模块
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ShopComponent {
    fun inject(activity:ShopActivity)

    fun inject(activity:ShopCategoryActivity)

    fun inject(activity:ShopInfoActivity)

    fun inject(activity:ShopAllFragment)

    fun inject(activity:ShopHomeFragment)

    fun inject(activity:ShopTagFragment)

    fun inject(activity:ShopListActivity)


}