package com.enation.javashop.android.middleware.logic.presenter.home

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.ExtraApi
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.home.HomeFragmentContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/1/19 下午5:42
 * @From   com.enation.javashop.android.middleware.logic.presenter.home
 * @Note   HomeFragment逻辑控制器
 */
class HomeFragmentPresenter @Inject constructor() :RxPresenter<HomeFragmentContract.View>(),HomeFragmentContract.Presenter  {

    /**
     * @Name  extraApi
     * @Type  ExtraApi
     * @Note  额外Api
     */
    @Inject
    protected lateinit var extraApi: ExtraApi

    /**
     * @Name  promotionApi
     * @Type  PromotionApi
     * @Note  促销Api
     */
    @Inject
    protected lateinit var promotionApi: PromotionApi

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<Any>>() {
        override fun onStartWithConnection() {
            providerView()
        }

        override fun onNextWithConnection(result: ArrayList<Any>, connectionQuality: ConnectionQuality) {
            providerView().complete()
            providerView().renderFloor(result)
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
     * @From   HomeFragmentPresenter
     * @Date   2018/1/19 下午6:28
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   HomeFragmentPresenter
     * @Date   2018/1/19 下午5:55
     * @Note   加载七巧板数据
     */
    override fun loadFloor() {
        var time : SecKillListViewModel? = null
        var next : SecKillListViewModel? = null
        val floor =  extraApi.getFloor()
                .map { respone ->
                    val result = ArrayList<FloorViewModel>()

                    val jsonArray = JSONArray(JSONObject(respone.getJsonString()).valueString("page_data"))

                    for (i in 0..(jsonArray.length() - 1)){
                        result.add(FloorViewModel.map(jsonArray.getJSONObject(i)))
                    }

                    return@map result
                }
        val banner =  extraApi.getBanner().map { responseBody ->
            var result = ArrayList<BannerModel>()

            responseBody.toJsonArray().arrayObjects().forEach { itemJson ->
                result.add(BannerModel.map(itemJson))
            }
            return@map result
        }

        val menu =  extraApi.getMenu().map { responseBody ->
            var result = ArrayList<FloorMenuModel>()

            responseBody.toJsonArray().arrayObjects().forEach { itemJson ->
                result.add(FloorMenuModel.map(itemJson))
            }
            return@map result
        }

        val secKill = promotionApi.getSeckillTimeLine().map { responseBody ->
            val result = ArrayList<SecKillListViewModel>()
            if (responseBody.toJsonArray().length() >0){
                responseBody.toJsonArray().arrayObjects().forEach {
                    result.add(SecKillListViewModel.map(it))
                }
            }
            return@map result
        }.flatMap {
            if (it.size > 0){
                time = it[0]
                next = it.getOrNull(1)
            }
            return@flatMap promotionApi.getSeckillGoods(if(time == null){-1}else{time!!.text.toInt()},1,5)
        } .map { responseBody ->
            val result = ArrayList<GoodsItemViewModel>()
            responseBody.toJsonObject().valueJsonArray("data").arrayObjects().forEach {
                result.add(GoodsItemViewModel.secMap(it))
            }
            return@map result
        }
        Observable.zip(floor,menu,banner,secKill,io.reactivex.functions.Function4<ArrayList<FloorViewModel>,ArrayList<FloorMenuModel>,ArrayList<BannerModel>,ArrayList<GoodsItemViewModel>,ArrayList<Any>> { t1, t2, t3, t4 ->

            var result = ArrayList<Any>()

            result.add(t3)
            result.add(t2)
            if ( time != null ){
                result.add(time!!)
                if (next == null){
                    result.add("null")
                }else{
                    result.add(next!!)
                }
            }else{
                result.add(SecKillListViewModel())
                result.add("null")
            }
            result.add(t4)
            result.addAll(t1)

            return@Function4  result

        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

}