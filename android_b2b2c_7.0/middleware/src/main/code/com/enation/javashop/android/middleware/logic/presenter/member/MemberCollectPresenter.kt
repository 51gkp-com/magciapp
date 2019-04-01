package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CategoryApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberCollectContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.ParentCategoryViewModel
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/4 下午3:29
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员收藏逻辑控制器
 */
class MemberCollectPresenter @Inject constructor() :RxPresenter<MemberCollectContract.View>(),MemberCollectContract.Presenter{


    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var memberApi : MemberApi

    private val observer = object:ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            val result =  data as ArrayList<*>
            when (result.getOrNull(0)) {
                is GoodsItemViewModel -> {
                    providerView().renderGoodsUI(result as ArrayList<GoodsItemViewModel>)
                }
                is ShopViewModel -> {
                    providerView().renderShopUI(result as ArrayList<ShopViewModel>)
                }
                1 ->{
                    providerView().renderShopUI(ArrayList())
                }
                2 ->{
                    providerView().renderGoodsUI(ArrayList())
                }
            }
            providerView().complete()
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
     * @From   MemberCollectPresenter
     * @Date   2018/5/4 下午3:29
     * @Note   加载数据
     * @param  page 分页数据
     * @param  state 0为商品，1为店铺
     */
    override fun loadData(page: Int, state: Int) {
        if (state == 0){

            memberApi.getCollectionList(page).map { responseBody ->

                val jsonResult = responseBody.getJsonString()
                val jsonObject = JSONObject(jsonResult)
                val result = ArrayList<Any>()
                val json = jsonObject.get("data").toString()
                val jsonArray = JSONArray(json)
                for (i in 0..(jsonArray.length() - 1)){
                    val jsonObject = jsonArray.getJSONObject(i)
                    result.add(GoodsItemViewModel.map(jsonObject))
                }
                if(result.count() == 0){
                    result.add(2)
                }
                return@map result
            }
            .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)



        }else{

            memberApi.getCollectionShopList(page).map { responseBody ->

                val result = ArrayList<Any>()
                val jsonResult = responseBody.getJsonString()
                val jsonObject = JSONObject(jsonResult)
                val json = jsonObject.get("data").toString()
                val jsonArray = JSONArray(json)
                for (i in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    result.add(ShopViewModel.map(jsonObject))
                }
                if(result.count() == 0){
                    result.add(1)
                }
                return@map result
            }
            .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

        }
    }

    /**
     * @author LDD
     * @From   MemberCollectPresenter
     * @Date   2018/5/4 下午3:39
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}