package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.toJsonArray
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionPointShopContract
import com.enation.javashop.android.middleware.model.GroupPointViewModel
import com.enation.javashop.android.middleware.model.PointViewModel
import com.enation.javashop.android.middleware.model.SecKillListViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/22 上午11:45
 * @From   com.enation.javashop.android.middleware.logic.presenter.promotion
 * @Note   积分商城逻辑控制器
 */
class PromotionPointShopPresenter @Inject constructor() :RxPresenter<PromotionPointShopContract.View>(),PromotionPointShopContract.Presenter{

    @Inject protected lateinit var promotionApi: PromotionApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<GroupPointViewModel>>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<GroupPointViewModel>, connectionQuality: ConnectionQuality) {
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
     * @From   PromotionPointShopPresenter
     * @Date   2018/5/22 上午11:46
     * @Note   加载积分商城
     */
    override fun loadPointShop() {
        promotionApi.getExchangeCats().map { responseBody ->
            val result = ArrayList<GroupPointViewModel>()
            result.add(GroupPointViewModel(-1,"全部"))
            responseBody.toJsonArray().arrayObjects().forEach {
                result.add(GroupPointViewModel.map(it))
            }
            return@map result
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   PromotionPointShopPresenter
     * @Date   2018/5/22 上午11:46
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}