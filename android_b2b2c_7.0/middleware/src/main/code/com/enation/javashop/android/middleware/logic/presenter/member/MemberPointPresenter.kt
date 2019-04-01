package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberPointContract
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.PointViewModel
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
 * @Date   2018/5/4 下午4:59
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   积分详细页面逻辑控制器
 */
class MemberPointPresenter  @Inject constructor() :RxPresenter<MemberPointContract.View>(),MemberPointContract.Presenter {


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
                    providerView().renderPointUi(data as ArrayList<PointViewModel>)
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
     * @From   MemberPointPresenter
     * @Date   2018/5/4 下午5:02
     * @Note   加载积分数据
     * @param  page 分页
     * @param  state 状态
     */
    override fun loadPointData(page: Int, state: Int) {

        memberApi.getPointList(page).map { responseBody ->

            val list = ArrayList<PointViewModel>()

            val jsonResult = responseBody.getJsonString()

            val jsonData = JSONObject(jsonResult).get("data").toString()

            val jsonArray = JSONArray(jsonData)

            for (i in 0..(jsonArray.length() - 1)){
                val jsonObject = jsonArray.getJSONObject(i)

                var pointType =  (jsonObject.valueInt("consum_point_type")==0).judge("-","+")

                list.add(PointViewModel(jsonObject.valueString("reason"),
                        jsonObject.valueDate("time"),
                        pointType + jsonObject.valueString("consum_point")
                        )
                )
            }

            return@map list

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberPointPresenter
     * @Date   2018/5/4 下午5:02
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}