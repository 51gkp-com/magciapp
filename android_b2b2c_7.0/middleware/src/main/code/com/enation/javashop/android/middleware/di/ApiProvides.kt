package com.enation.javashop.android.middleware.di

import com.enation.javashop.android.middleware.api.*
import com.enation.javashop.net.engine.core.NetEngineFactory
import dagger.Module
import dagger.Provides

/**
 * @author  LDD
 * @Data   2017/12/26 上午7:30
 * @From   com.enation.javashop.android.lib.di
 * @Note   MainModlue模块提供者
 */
@Module
class ApiProvides {

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   额外ApI
     * @return 额外API
     */
    @Provides
    fun provideExtraApi(): ExtraApi{
        return ApiManager.EXTRA_API
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   额外ApI
     * @return 额外API
     */
    @Provides
    fun provideOtherApi(): OtherApi{
        return ApiManager.OTHER_API
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   售后ApI
     * @return 售后API
     */
    @Provides
    fun provideAfterSaleApi(): AfterSaleApi{
        return ApiManager.API_AFTERSALE
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   购物车ApI
     * @return 购物车API
     */
    @Provides
    fun provideCartApi(): CartApi{
        return ApiManager.API_CART
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   分类ApI
     * @return 分类API
     */
    @Provides
    fun provideCategoryApi(): CategoryApi{
        return ApiManager.API_CATEGORY
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   商品ApI
     * @return 商品API
     */
    @Provides
    fun provideGoodsApi(): GoodsApi{
        return ApiManager.API_GOODS
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   会员ApI
     * @return 会员API
     */
    @Provides
    fun provideMemberApi(): MemberApi{
        return ApiManager.API_MEMBER
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   订单ApI
     * @return 订单API
     */
    @Provides
    fun provideOrderApi(): OrderApi{
        return ApiManager.API_ORDER
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   支付ApI
     * @return 支付API
     */
    @Provides
    fun providePaymentApi(): PaymentApi{
        return ApiManager.API_PAYMENT
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   促销ApI
     * @return 促销API
     */
    @Provides
    fun providePromotionApi(): PromotionApi{
        return ApiManager.API_PROMOTION
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   店铺ApI
     * @return 店铺API
     */
    @Provides
    fun provideShopApi(): ShopApi{
        return ApiManager.API_SHOP
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   交易ApI
     * @return 交易API
     */
    @Provides
    fun provideTradeApi(): TradeApi{
        return ApiManager.API_TRADE
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.middleware.di
     * @Data   2017/12/26 上午7:30
     * @Note   广告ApI
     * @return 广告ApI
     */
    @Provides
    fun provideAdApi():AdApi{
        return ApiManager.API_AD
    }

    /**
     * @author LDD
     * @From   ApplicationComponent
     * @Date   2018/8/10 下午12:59
     * @Note   基础Api
     */
    @Provides
    fun provideBaseApi():BaseApi{
        return  ApiManager.BASE_API
    }

    /**
     * @author LDD
     * @From   ApplicationComponent
     * @Date   2018/8/10 下午12:59
     * @Note   护照Api
     */
    @Provides
    fun providePassportApi():PassportApi{
        return  ApiManager.PASSPORT_API
    }

}
