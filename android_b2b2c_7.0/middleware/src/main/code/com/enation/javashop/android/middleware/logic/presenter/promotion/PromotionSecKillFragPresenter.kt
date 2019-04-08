package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionSecKillFragContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/21 下午3:58
 * @From   com.enation.javashop.android.middleware.logic.presenter.promotion
 * @Note   秒杀Frag逻辑控制器
 */
class PromotionSecKillFragPresenter @Inject constructor() :RxPresenter<PromotionSecKillFragContract.View>(),PromotionSecKillFragContract.Presenter {

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
            providerView().renderGoodsList(result)
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
     * @From   PromotionSecKillFragPresenter
     * @Date   2018/5/21 下午4:03
     * @Note   加载秒杀商品
     * @param  time 当前秒杀时间段
     * @param  page 分页页面
     */
    override fun loadSecGoods(time: String, page: Int) {

        promotionApi.getSeckillGoods(time.toInt(),page,10)
                .map { responseBody ->
                    val result = ArrayList<GoodsItemViewModel>()
                    responseBody.toJsonObject().valueJsonArray("data").arrayObjects().forEach {
                        result.add(GoodsItemViewModel.secMap(it))
                    }
                    return@map result
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragPresenter
     * @Date   2018/5/21 下午4:04
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}