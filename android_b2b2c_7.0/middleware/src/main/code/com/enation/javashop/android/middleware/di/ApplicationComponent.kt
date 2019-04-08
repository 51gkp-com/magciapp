package com.enation.javashop.android.middleware.di

import com.enation.javashop.android.middleware.api.*
import com.umeng.socialize.media.Base
import dagger.Component

/**
 * @author  LDD
 * @Data   2017/12/26 上午7:29
 * @From   com.enation.javashop.android.lib.di
 * @Note   Dogger依赖注入基础模块
 */
@Component(modules = arrayOf(ApiProvides::class))
interface ApplicationComponent {

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   额外Api
     * @return 额外API
     */
    fun provideExtraApi(): ExtraApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   售后ApI
     * @return 售后API
     */
    fun provideAfterSaleApi(): AfterSaleApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   购物车ApI
     * @return 购物车API
     */
    fun provideCartApi(): CartApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   分类ApI
     * @return 分类API
     */
    fun provideCategoryApi():CategoryApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   商品ApI
     * @return 商品API
     */
    fun provideGoodsApi():GoodsApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   会员ApI
     * @return 会员API
     */
    fun provideMemberApi():MemberApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   订单ApI
     * @return 订单API
     */
    fun provideOrderApi():OrderApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   支付ApI
     * @return 支付API
     */
    fun providePaymentApi():PaymentApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   促销ApI
     * @return 促销API
     */
    fun providePromotionApi():PromotionApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   店铺ApI
     * @return 店铺API
     */
    fun provideShopApi():ShopApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   交易ApI
     * @return 交易API
     */
    fun provideTradeApi():TradeApi

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   广告ApI
     * @return 广告ApI
     */
    fun provideAdApi():AdApi

    /**
     * @author LDD
     * @From   ApplicationComponent
     * @Date   2018/8/10 下午12:59
     * @Note   基础Api
     */
    fun provideBaseApi() :BaseApi

    /**
     * @author LDD
     * @From   ApplicationComponent
     * @Date   2018/8/10 下午12:59
     * @Note   护照Api
     */
    fun providePassportApi():PassportApi

}
