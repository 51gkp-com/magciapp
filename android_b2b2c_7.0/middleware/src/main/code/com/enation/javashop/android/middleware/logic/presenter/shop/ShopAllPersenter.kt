package com.enation.javashop.android.middleware.logic.presenter.shop

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.getJsonString
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.Promotion
import com.enation.javashop.android.middleware.logic.contract.shop.ShopAllContract
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.PromotionViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/9 下午5:51
 * @From   com.enation.javashop.android.middleware.logic.presenter.shop
 * @Note   店铺全部商品
 */
class ShopAllPersenter @Inject constructor() : RxPresenter<ShopAllContract.View>(), ShopAllContract.Presenter {


    /**
     * @Name  goodsApi
     * @Type  GoodsApi
     * @Note  店铺API
     */
    @Inject
    protected lateinit var goodsApi: GoodsApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {

            when (data) {
                is ArrayList<*> -> {
                    providerView().complete()
                    providerView().initGoods(data as ArrayList<GoodsItemViewModel>)
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
     * @From   ShopAllPersenter
     * @Date   2018/4/24 下午3:35
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopAllPersenter
     * @Date   2018/4/24 下午3:36
     * @Note   加载商品
     * @param  filter 筛选条件
     */
    override fun loadGoods(filter: HashMap<String, Any>,page :Int) {


        goodsApi.searchGoodsList(page,10,filter).map { responseBody ->

            var jsonPage = JSONObject(responseBody.getJsonString())

            var jsonArray = JSONArray(jsonPage.get("data").toString())

            var goodsItemList = ArrayList<GoodsItemViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                goodsItemList.add(GoodsItemViewModel.goodssearchMap(jsonObject))
            }

            return@map goodsItemList

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }
}