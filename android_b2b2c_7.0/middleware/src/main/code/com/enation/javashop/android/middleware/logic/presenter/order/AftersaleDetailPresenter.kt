package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.toJsonObject
import com.enation.javashop.android.middleware.api.AfterSaleApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.AftersaleDetailContract
import com.enation.javashop.android.middleware.logic.contract.order.AftersaleListContract
import com.enation.javashop.android.middleware.model.AftersaleDetailModel
import com.enation.javashop.android.middleware.model.AftersaleListModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by LDD on 2018/10/16.
 */
class AftersaleDetailPresenter @Inject constructor() : RxPresenter<AftersaleDetailContract.View>() , AftersaleDetailContract.Presenter {


    @Inject
    protected lateinit var aftersaleApi : AfterSaleApi

    private val observer = object : ConnectionObserver<AftersaleDetailModel>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result : AftersaleDetailModel, connectionQuality: ConnectionQuality) {
            providerView().render(result)
            providerView().complete("")
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun load(sn: String) {
        aftersaleApi.getAfterSaleDetail(sn)
                .map {
                    return@map AftersaleDetailModel.map(it.toJsonObject())
                }
                .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}