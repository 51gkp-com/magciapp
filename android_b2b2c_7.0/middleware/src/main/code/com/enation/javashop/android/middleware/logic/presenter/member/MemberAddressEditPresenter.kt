package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.getJsonString
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.api.BaseApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberAddressEditContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.districtselectorview.widget.DistrictSelectorView
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/8 上午10:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员地址更改逻辑控制器
 */
class MemberAddressEditPresenter @Inject constructor() :RxPresenter<MemberAddressEditContract.View>() ,MemberAddressEditContract.Presenter {



    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var memberApi : MemberApi

    /**
     * @Name  baseApi
     * @Type  BaseApi
     * @Note  基础API
     */
    @Inject
    protected lateinit var baseApi : BaseApi

    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is MemberAddressViewModel -> {
                    providerView().complete("操作完成",1)
                }
                is ArrayList<*> -> {
                    providerView().renderRegion(data as ArrayList<RegionViewModel>)
                    providerView().complete()
                }
                else ->{
                    providerView().complete("操作完成",1)
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
     * @From   MemberAddressEditPresenter
     * @Date   2018/5/8 上午10:46
     * @Note   添加地址
     * @param  data 地址
     */
    override fun addAddress(data: MemberAddressViewModel) {
        val defAddr = if(data.isDefault)1 else 0
        memberApi.addAddress(data.nameObser.get(),data.addressDetailObser.get(),data.phoneNumObser.get(),data.phoneNumObser.get(),
                defAddr,data.tag ?: "" ,
                (data.townId == -1).judge(data.counId!!,data.townId!!))
                .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditPresenter
     * @Date   2018/5/8 上午10:47
     * @Note   删除地址
     * @param  id 地址索引
     */
    override fun deleteAddress(id: Int) {
        memberApi.deleteAddress(id).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditPresenter
     * @Date   2018/5/8 上午10:47
     * @Note   更改地址
     * @param  data 更改后的信息
     */
    override fun editAddress(data: MemberAddressViewModel) {
        val defAddr = if(data.isDefault)1 else 0
        memberApi.editAddress(data.id,data.nameObser.get(),data.addressDetailObser.get(),if(data.phoneNumObser.get().contains("*")){data.phoneNum}else{data.phoneNumObser.get()},if(data.phoneNumObser.get().contains("*")){data.phoneNum}else{data.phoneNumObser.get()},
                defAddr,data.tag ?: "" ,
                (data.townId == -1).judge(data.counId!!,data.townId!!))
                .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditPresenter
     * @Date   2018/5/9 上午9:21
     * @Note   获取地区数据
     * @param  id 地区父id 初始化时为0
     */
    override fun getRegion(id: Int) {

        baseApi.loadRegion(id).map { responseBody ->
            var list = ArrayList<RegionViewModel>()

            val jsonResult = responseBody.getJsonString()

            val jsonArray = JSONArray(jsonResult)

            for (i in 0..(jsonArray.length() - 1)){
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(RegionViewModel.map(jsonObject))
            }

            return@map list

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    /**
     * 设置默认地址
     */
    override fun setDefault(id: Int) {
        memberApi.setAddressDefault(id).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }


    /**
     * @author LDD
     * @From   MemberAddressEditPresenter
     * @Date   2018/5/8 上午10:45
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}