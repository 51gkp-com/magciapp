package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.CouponHallContract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.GroupPointViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by LDD on 2018/10/29.
 */
class CouponHallPresenter @Inject constructor() :RxPresenter<CouponHallContract.View>(),CouponHallContract.Presenter {

    @Inject lateinit var promotionApi: PromotionApi

    @Inject lateinit var memberApi: MemberApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if (result is String){
                providerView().complete(result)
            }else{
                providerView().complete()
                providerView().render(result as ArrayList<CouponViewModel>)
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun load(page: Int) {
        promotionApi.getAllCoupon(page).map { responseBody ->
            var list = ArrayList<CouponViewModel>()

            var jsonResult = responseBody.getJsonString()

            var json = JSONObject(jsonResult).get("data").toString()

            var jsonArray = JSONArray(json)

            //遍历优惠券数据
            for (i in 0..(jsonArray.length() - 1)){
                val jsonObject = jsonArray.getJSONObject(i)

                //是否已使用 0为未使用,1为使用
                var usedStatus = (jsonObject.valueInt("used_status") == 0).judge(false,true)

                list.add(CouponViewModel(jsonObject.valueDouble("coupon_price"),
                        jsonObject.valueDouble("coupon_threshold_price"),
                        jsonObject.valueString("seller_name"),
                        false,
                        usedStatus,
                        false,
                        jsonObject.valueInt("coupon_id"),
                        jsonObject.valueDate("start_time")+"-"+jsonObject.valueDate("end_time"),
                        jsonObject.valueInt("seller_id"),
                        jsonObject.valueString("title")
                )
                )
            }
            return@map list
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun getCoupon(id: Int) {
        memberApi.receiveCoupon("$id").map { "领取成功" }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)

    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}