package com.enation.javashop.android.component.goods.di

import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.activity.GoodsSearchActivity
import com.enation.javashop.android.component.goods.activity.SearchActivity
import com.enation.javashop.android.component.goods.fragment.GoodsCommentFragment
import com.enation.javashop.android.component.goods.fragment.GoodsInfoFragment
import com.enation.javashop.android.component.goods.fragment.GoodsIntroduceFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/3/9 上午9:05
 * @From   com.enation.javashop.android.component.goods.di
 * @Note   商品模块依赖注入
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface GoodsComponent {
    fun inject(activity:GoodsSearchActivity)

    fun inject(activity:GoodsActivity)

    fun inject(fragment: GoodsInfoFragment)

    fun inject(fragment: GoodsIntroduceFragment)

    fun inject(fragment: GoodsCommentFragment)

    fun inject(fragment: SearchActivity)

}