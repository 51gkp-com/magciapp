package com.enation.javashop.android.middleware.api

import android.annotation.SuppressLint
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.UUID
import okhttp3.*

/**
 * @author  LDD
 * @Date   2018/1/11 下午6:12
 * @From   com.enation.javashop.android.lib.api
 * @Note   API管理器
 */
object ApiManager {

        /**
         * @Name  apiFectory
         * @Type  NetFectory
         * @Note  API构建者
         */
        @SuppressLint("StaticFieldLeak")
        private val apiFectory = NetFectory.build(BaseApplication.appContext)

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.BASE_URL
         * @Type  UrlManager
         * @Note  服务器URL控制器
         */
        private val urlManager = UrlManager.build()

        /** 全局分页条数 */
        val PAGE_SIZE = 10

        /**
         * @Name  com.enation.javashop.android.middleware.api.ApiManager.EXTRA_API
         * @Type  ExtraApi
         * @Note  其他API
         */
        val EXTRA_API :ExtraApi by lazy { apiFectory.createService(ExtraApi::class.java, urlManager.buyer) }


        /**
         * @Name  com.enation.javashop.android.middleware.api.ApiManager.BASE_URL
         * @Type  BaseApi
         * @Note  基础API
         */
        val BASE_API :BaseApi by lazy { apiFectory.createService(BaseApi::class.java, urlManager.basic)}

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_AFTERSALE
         * @Type  AfterSaleApi
         * @Note  售后API
         */
        val API_AFTERSALE : AfterSaleApi by lazy { apiFectory.createService(AfterSaleApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_CART
         * @Type  CartApi
         * @Note  购物车API
         */
        val API_CART: CartApi by lazy { apiFectory.createService(CartApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_CATEGORY
         * @Type  CategoryApi
         * @Note  分类API
         */
        val API_CATEGORY: CategoryApi by lazy { apiFectory.createService(CategoryApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_GOODS
         * @Type  GoodsApi
         * @Note  商品API
         */
        val API_GOODS: GoodsApi by lazy { apiFectory.createService(GoodsApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_MEMBER
         * @Type  MemberApi
         * @Note  会员API
         */
        val API_MEMBER: MemberApi by lazy { apiFectory.createService(MemberApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_ORDER
         * @Type  OrderApi
         * @Note  订单API
         */
        val API_ORDER: OrderApi by lazy { apiFectory.createService(OrderApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_PAYMENT
         * @Type  PaymentApi
         * @Note  支付API
         */
        val API_PAYMENT: PaymentApi by lazy { apiFectory.createService(PaymentApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_PROMOTION
         * @Type  PromotionApi
         * @Note  促销API
         */
        val API_PROMOTION: PromotionApi by lazy { apiFectory.createService(PromotionApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_SHOP
         * @Type  ShopApi
         * @Note  店铺API
         */
        val API_SHOP: ShopApi by lazy { apiFectory.createService(ShopApi::class.java, urlManager.buyer) }

        /**
         * @Name  com.enation.javashop.android.lib.api.ApiManager.Companion.API_TRADE
         * @Type  TradeApi
         * @Note  交易API
         */
        val API_TRADE: TradeApi by lazy { apiFectory.createService(TradeApi::class.java, urlManager.buyer) }

        /**
         * @Name  API_AD
         * @Type  AdApi
         * @Note  广告API
         */
        val API_AD: AdApi by lazy { apiFectory.createService(AdApi::class.java, urlManager.buyer) }

        /**
         * @Name  PASSPORT_API
         * @Type  PassportApi
         * @Note  护照
         */
        val PASSPORT_API :PassportApi by lazy { apiFectory.createService(PassportApi::class.java, urlManager.buyer) }

        /**
         * @author LDD
         * @From   ApiManager
         * @Date   2018/8/13 上午8:19
         * @Note   刷新Token
         * @param  refrehToken 刷新Token
         */
        fun refreshToken(refrehToken :String) : Response{
                val mediaType = MediaType.parse("text/x-markdown; charset=utf-8")
                val requestBody = "refrehToken"
                val okHttpClient = OkHttpClient()
                val getTokenRequest = Request.Builder()
                        .url("${urlManager.buyer}passport/token?refersh_token=$refrehToken")
                        .post(RequestBody.create(mediaType,requestBody))
                        .addHeader("uuid", UUID.uuid)
                        .build()
                val call = okHttpClient.newCall(getTokenRequest)
                return call.execute()
        }
}
