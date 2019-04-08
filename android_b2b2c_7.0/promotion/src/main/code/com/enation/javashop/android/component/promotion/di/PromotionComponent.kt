package com.enation.javashop.android.component.promotion.di

import com.enation.javashop.android.component.promotion.activity.*
import com.enation.javashop.android.component.promotion.fragment.PromotionGroupBuyFragment
import com.enation.javashop.android.component.promotion.fragment.PromotionPointShopFragment
import com.enation.javashop.android.component.promotion.fragment.PromotionSecKillFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/5/21 下午4:24
 * @From   com.enation.javashop.android.component.promotion.di
 * @Note   促销模块依赖注入入口
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface PromotionComponent {

    fun inject(fragment: PromotionSecKillFragment)

    fun inject(activity: PromotionSecKillActivity)

    fun inject(activity: PromotionGroupBuyFragment)

    fun inject(activity: PromotionPointShopFragment)

    fun inject(activity: PromotionPointShopActivity)

    fun inject(activity: PromotionGroupBuyActivity)

    fun inject(activity: WebActivity)

    fun inject(activity: CouponHallActivity)


}