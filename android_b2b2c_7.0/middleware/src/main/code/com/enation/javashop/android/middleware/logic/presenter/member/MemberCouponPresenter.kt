package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberCouponConract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/3 下午4:23
 * @From   com.enation.javashop.android.middleware.logic.presenter.home
 * @Note   会员优惠券页面 逻辑控制
 */
class MemberCouponPresenter @Inject constructor() :RxPresenter<MemberCouponConract.View>(),MemberCouponConract.Presenter {


    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var memberApi : MemberApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ArrayList<*> -> {
                    providerView().renderCoupon(data as ArrayList<CouponViewModel>)
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
     * @From   MemberCouponPresenter
     * @Date   2018/5/3 下午4:31
     * @Note   加载优惠券
     * @param  page  分页
     * @param  state 状态 1：未使用,2:已使用,3:已过期
     */
    override fun loadCoupon(page: Int, state: Int) {

        memberApi.getCouponListByAll(state,page).map { responseBody ->

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
                        (state == 3).judge(true,false),
                        jsonObject.valueInt("mc_id"),
                        jsonObject.valueDate("start_time")+"-"+jsonObject.valueDate("end_time"),
                        jsonObject.valueInt("seller_id"),
                        jsonObject.valueString("title")
                        )
                )
            }
            return@map list
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}