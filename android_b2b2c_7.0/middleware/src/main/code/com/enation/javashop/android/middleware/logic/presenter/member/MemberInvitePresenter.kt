package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.OtherApi
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberInviteContract
import com.enation.javashop.android.middleware.model.InviteViewModel
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
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
class MemberInvitePresenter @Inject constructor() :RxPresenter<MemberInviteContract.View>(),MemberInviteContract.Presenter {


    /**
     * @Name  otherApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var otherApi : OtherApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            if (data is String){
                providerView().complete(data)
            }else{
                providerView().renderAddress(data as ArrayList<InviteViewModel>)
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


    /**
     * @author LDD
     * @From   MemderAddressPresenter
     * @Date   2018/5/7 下午4:04
     * @Note   加载地址
     */
    override fun loadInviteList(page :Int, name:String) {

        otherApi.getInviteList(name).map { responseBody ->

            var list = ArrayList<InviteViewModel>()

            var jsonResoult = responseBody.toJsonObject()

            var jsonArray = jsonResoult.getJSONArray("content")

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)


                var item = InviteViewModel.map(jsonObject)
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