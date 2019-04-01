package com.enation.javashop.android.middleware.logic.presenter.shop

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.getJsonString
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopTagContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/9 下午5:58
 * @From   com.enation.javashop.android.lib.base.RxPresenter
 * @Note   店铺标签逻辑控制器
 */
class ShopTagPresenter @Inject constructor() : RxPresenter<ShopTagContract.View>(), ShopTagContract.Presenter {


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
     * @From   ShopTagPresenter
     * @Date   2018/4/9 下午5:58
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopTagPresenter
     * @Date   2018/4/9 下午5:59
     * @Note   加载商品数据
     * @param  tag 标签类型
     * @param  shopId 店铺ID
     */
    override fun loadGoods(tag: String, shopId: Int) {
        var color = "#e83437"
        if (tag == "new"){
            color = "#fbac42"
        }
        if (tag == "hot"){
            color = "#e83437"
        }
        if (tag == "recommend"){
            color = "#000000"
        }

        goodsApi.getTagGoodsList(tag,shopId,10).map { responseBody ->
            var jsonArray = JSONArray(responseBody.getJsonString())

            var goodsList = ArrayList<GoodsItemViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                var item = GoodsItemViewModel.shopGoodsMap(jsonObject,shopId)
                item.priceColor = color
                goodsList.add(item)
            }

            return@map goodsList
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}