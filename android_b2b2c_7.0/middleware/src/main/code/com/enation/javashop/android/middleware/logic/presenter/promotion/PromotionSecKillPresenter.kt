package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.toJsonArray
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionSecKillContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.SecKillListViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.channels.produce
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/21 下午3:59
 * @From
 * @Note   秒杀页面逻辑控制
 */
class PromotionSecKillPresenter @Inject constructor() :RxPresenter<PromotionSecKillContract.View>(),PromotionSecKillContract.Presenter {

    @Inject protected lateinit var promotionApi: PromotionApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<SecKillListViewModel>>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<SecKillListViewModel>, connectionQuality: ConnectionQuality) {
            providerView().complete()
            providerView().renderSecKill(result)
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
     * @From   PromotionSecKillPresenter
     * @Date   2018/5/21 下午4:04
     * @Note   加载秒杀数据
     */
    override fun loadSecKill() {
        promotionApi.getSeckillTimeLine().map { responseBody ->
            val result = ArrayList<SecKillListViewModel>()
            responseBody.toJsonArray().arrayObjects().forEach {
                result.add(SecKillListViewModel.map(it))
            }
            return@map result
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillPresenter
     * @Date   2018/5/21 下午4:04
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}