package com.enation.javashop.android.middleware.logic.presenter.order

import android.databinding.ObservableField
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateCouponContract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/23 下午6:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单创建优惠券逻辑控制器
 */
class OrderCreateCouponPresenter @Inject constructor() :RxPresenter<OrderCreateCouponContract.View>() , OrderCreateCouponContract.Presenter{

    @Inject
    protected lateinit var tradeApi: TradeApi

    @Inject
    protected lateinit var cartApi: CartApi

    private val observer = object : ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {

            if (result is ArrayList<*>){
                providerView().renderCoupon(result as ArrayList<CouponViewModel>)
            }

            if (result is String){
                providerView().complete(result)
            }else{
                providerView().complete("")
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
     * @From   OrderCreateCouponPresenter
     * @Date   2018/5/23 下午6:46
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponPresenter
     * @Date   2018/5/23 下午6:48
     * @Note   使用优惠券
     * @param  id 优惠券索引
     */
    override fun useCoupon(id: Int, couponId: Int) {
        tradeApi.useCoupon(id,couponId).map { "使用成功" }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponPresenter
     * @Date   2018/5/23 下午7:01
     * @Note   加载优惠券
     */
    override fun loadCoupon(ids :String) {
        cartApi.getCartData("checked")
                .map { responseBody ->
                    var list = ArrayList<CouponViewModel>()
                    responseBody.toJsonObject().valueJsonArray("cart_list").arrayObjects().forEach { jsonObject ->
                       jsonObject.valueJsonArray("coupon_list").arrayObjects().forEach { couponJson ->
                           if (couponJson.valueInt("enable") == 1){
                               list.add(CouponViewModel(
                                       couponJson.valueDouble("amount"),
                                       couponJson.valueDouble("coupon_threshold_price"),
                                       jsonObject.valueString("seller_name"),
                                       false,
                                       false,
                                       false,
                                       couponJson.valueInt("member_coupon_id"),
                                       couponJson.valueDate("end_time")+"之前可用",
                                       couponJson.valueInt("seller_id"),
                                       couponJson.valueString("title"),
                                       isGet = false,
                                       isSelect = ObservableField(couponJson.optInt("selected",0))
                               ))
                           }
                       }
                    }
                    return@map list
                }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }
}