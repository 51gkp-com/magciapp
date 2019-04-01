package com.enation.javashop.android.component.order.di

import com.enation.javashop.android.component.order.activity.*
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/4/16 下午2:45
 * @From   com.enation.javashop.android.component.order.di
 * @Note   订单模块依赖注入
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface OrderComponent {
    fun inject(activity :OrderListActivity)

    fun inject(activity :OrderDetailActivity)

    fun inject(activity :OrderPayActivity)

    fun inject(activity :OrderAfterSaleActivity)

    fun inject(activity :OrderCreateActivity)

    fun inject(activity: OrderCreateCouponActivity)

    fun inject(activity: OrderCreateGoodsActivity)

    fun inject(activity: OrderCreatePayShipActivity)

    fun inject(activity: OrderCreateReceiptActivity)

    fun inject(activity: AftersaleListActivity)

    fun inject(activity: AftersaleDetailActivity)

    fun inject(activity: ExpressActivity)

}