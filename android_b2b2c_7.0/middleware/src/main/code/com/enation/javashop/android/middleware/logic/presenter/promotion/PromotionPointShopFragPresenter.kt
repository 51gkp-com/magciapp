package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionPointShopFragContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/22 上午11:37
 * @From   com.enation.javashop.android.middleware.logic.presenter.promotion
 * @Note   积分商城子页面
 */
class PromotionPointShopFragPresenter @Inject constructor() :RxPresenter<PromotionPointShopFragContract.View>(),PromotionPointShopFragContract.Presenter {

    @Inject lateinit var promotionApi: PromotionApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<GoodsItemViewModel>>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<GoodsItemViewModel>, connectionQuality: ConnectionQuality) {
            providerView().complete()
            providerView().renderPointShop(result)
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
     * @From   PromotionPointShopFragPresenter
     * @Date   2018/5/22 上午11:40
     * @Note   加载积分商城数据
     * @param  id 分类ID
     * @param  page 分页查询
     */
    override fun loadPointShop(id: Int, page: Int) {
        var paramMap = HashMap<String,Any>()
        paramMap.put("page_size",10)
        paramMap.put("page_no",page)
        if (id != -1){
            paramMap.put("cat_id",id)
        }
        promotionApi.getExchangeGoods(paramMap)
                .map { responseBody ->
                    val result = ArrayList<GoodsItemViewModel>()
                    responseBody.toJsonObject().valueJsonArray("data").arrayObjects().forEach {
                        result.add(GoodsItemViewModel.pointMap(it))
                    }
                    return@map result
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragPresenter
     * @Date   2018/5/22 上午11:39
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}