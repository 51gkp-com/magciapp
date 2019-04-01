package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberAddressContract
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/7 下午4:03
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员地址逻辑控制器
 */
class MemberAddressPresenter @Inject constructor() :RxPresenter<MemberAddressContract.View>(),MemberAddressContract.Presenter {


    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var memberApi : MemberApi

    @Inject
    protected lateinit var tradeApi :TradeApi

    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            if (data is String){
                if(data == "删除成功"){
                    providerView().deleteSuccess()
                }else if(data == "设置成功"){
                    providerView().setDefaultSuccess()
                }else{
                    providerView().updateOrderAddress()
                }
                providerView().complete(data)
            }else{
                providerView().renderAddress(data as ArrayList<MemberAddressViewModel>)
                providerView().complete()
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }

    }


    override fun setOrderAddress(id: Int) {
        tradeApi.setAddress(id)
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { "修改成功" }
                .subscribe(observer)
    }


    override fun delete(id: Int) {
        memberApi.deleteAddress(id).map { "删除成功" }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun setDefalut(id: Int) {
        memberApi.setAddressDefault(id).map { "设置成功" }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemderAddressPresenter
     * @Date   2018/5/7 下午4:04
     * @Note   加载地址
     */
    override fun loadAddress(page :Int) {

        memberApi.getAddressList().map { responseBody ->

            var list = ArrayList<MemberAddressViewModel>()

            var jsonResoult = responseBody.getJsonString()

            var jsonArray = JSONArray(jsonResoult)

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)

                var isDefault = (jsonObject.valueInt("def_addr") == 1).judge(true,false)

                var item = MemberAddressViewModel(jsonObject.valueInt("addr_id"),
                        jsonObject.valueString("name"),
                        jsonObject.valueString("mobile"),
                        jsonObject.valueString("addr"),
                        jsonObject.valueInt("province_id"),
                        jsonObject.valueString("province"),
                        jsonObject.valueInt("city_id"),
                        jsonObject.valueString("city"),
                        jsonObject.valueInt("county_id"),
                        jsonObject.valueString("county"),
                        jsonObject.valueInt("town_id"),
                        jsonObject.valueString("town"),
                        jsonObject.valueString("ship_address_name"),
                        isDefault
                )
                list.add(item)
            }

            return@map list

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemderAddressPresenter
     * @Date   2018/5/7 下午4:04
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}