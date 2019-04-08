package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.toJsonArray
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionGroupBuyContract
import com.enation.javashop.android.middleware.model.GroupPointViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/22 上午11:40
 * @From   com.enation.javashop.android.middleware.logic.presenter.promotion
 * @Note   团购商城父页面
 */
class PromotionGroupBuyPresenter @Inject constructor() :RxPresenter<PromotionGroupBuyContract.View>() ,PromotionGroupBuyContract.Presenter {

    @Inject
    protected lateinit var promotionApi: PromotionApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<GroupPointViewModel>>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<GroupPointViewModel>, connectionQuality: ConnectionQuality) {
            providerView().complete()
            providerView().renderGroupBuy(result)
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
     * @From   PromotionGroupBuyPresenter
     * @Date   2018/5/22 上午11:42
     * @Note   加载团购数据
     */
    override fun loadGroupBuy() {
        promotionApi.getGroupBuyCat().map { responseBody ->
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
     * @From   PromotionGroupBuyPresenter
     * @Date   2018/5/22 上午11:41
     * @Note   依赖注入
     */
    override fun bindDagger() {

        MiddlewareDaggerComponent.component.inject(this)

    }

}