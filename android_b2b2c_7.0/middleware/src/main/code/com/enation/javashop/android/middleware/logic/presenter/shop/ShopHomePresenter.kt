package com.enation.javashop.android.middleware.logic.presenter.shop

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.api.ShopApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.Promotion
import com.enation.javashop.android.middleware.logic.contract.shop.ShopHomeContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/9 下午5:54
 * @From   com.enation.javashop.android.lib.base.RxPresenter
 * @Note   店铺首页逻辑控制器
 */
class ShopHomePresenter @Inject constructor() : RxPresenter<ShopHomeContract.View>(), ShopHomeContract.Presenter {


    /**
     * @Name  shopApi
     * @Type  ShopApi
     * @Note  店铺API
     */
    @Inject
    protected lateinit var shopApi: ShopApi

    /**
     * @Name  goodsApi
     * @Type  GoodsApi
     * @Note  店铺API
     */
    @Inject
    protected lateinit var goodsApi: GoodsApi


    /**
     * @Name  promotionApi
     * @Type  PromotionApi
     * @Note  店铺API
     */
    @Inject
    protected lateinit var promotionApi: PromotionApi



    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ShopFirstViewModel -> {
                    providerView().initFirstData(data as ShopFirstViewModel)
                }
                is ArrayList<*> -> {
                    providerView().initCoupon(data as ArrayList<CouponViewModel>)
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }

    }


    /**
     * @author LDD
     * @From   ShopFirstPresenter
     * @Date   2018/4/9 下午5:55
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopFirstPresenter
     * @Date   2018/4/9 下午5:55
     * @Note   加载首页数据
     * @param  shopId 店铺ID
     */
    override fun loadFirstData(shopId: Int) {

        var hot = ArrayList<GoodsItemViewModel>()
        var new = ArrayList<GoodsItemViewModel>()
        var recommend = ArrayList<GoodsItemViewModel>()

        goodsApi.getTagGoodsList("hot",shopId,20).flatMap {
            responseBody ->
            var jsonArray = JSONArray(responseBody.getJsonString())

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                var item = GoodsItemViewModel.shopGoodsMap(jsonObject,shopId)
                hot.add(item)
            }

            return@flatMap goodsApi.getTagGoodsList("new",shopId,20)

        }.flatMap {
            responseBody ->
            var jsonArray = JSONArray(responseBody.getJsonString())

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                var item = GoodsItemViewModel.shopGoodsMap(jsonObject,shopId)
                new.add(item)
            }
            return@flatMap goodsApi.getTagGoodsList("recommend",shopId,20)
        }.map {

            responseBody ->
            var jsonArray = JSONArray(responseBody.getJsonString())

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                var item = GoodsItemViewModel.shopGoodsMap(jsonObject,shopId)
                recommend.add(item)
            }

            return@map ShopFirstViewModel(recommend,new,hot)

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    /**
     * @author LDD
     * @From   ShopFirstPresenter
     * @Date   2018/4/9 下午5:56
     * @Note   加载优惠券
     * @param  shopId  店铺ID
     */
    override fun loadCoupon(shopId: Int) {

        promotionApi.getAllCoupon(10,10).map { responseBody ->

            var jsonArray = JSONArray(responseBody.getJsonString())

            var couponList = ArrayList<CouponViewModel>()
            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                var item = CouponViewModel(
                        jsonObject.valueDouble("coupon_price"),
                        jsonObject.valueDouble("coupon_threshold_price"),
                        jsonObject.valueString("seller_name"),
                        false,
                        false,
                        false,
                        jsonObject.valueInt("coupon_id"),
                        jsonObject.valueDate("start_time")+"-"+jsonObject.valueDate("end_time"),
                        jsonObject.valueInt("seller_id"),
                        jsonObject.valueString("title")
                )

                couponList.add(item)
            }

            return@map couponList

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }
}