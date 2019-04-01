package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.toJsonObject
import com.enation.javashop.android.lib.utils.valueJsonArray
import com.enation.javashop.android.middleware.api.AfterSaleApi
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.AftersaleListContract
import com.enation.javashop.android.middleware.logic.contract.order.OrderAfterSaleContract
import com.enation.javashop.android.middleware.model.AftersaleListModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by LDD on 2018/10/16.
 */
class AftersaleListPresenter  @Inject constructor() : RxPresenter<AftersaleListContract.View>() , AftersaleListContract.Presenter {


    @Inject
    protected lateinit var aftersaleApi : AfterSaleApi

    private val observer = object :ConnectionObserver<ArrayList<AftersaleListModel>>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<AftersaleListModel>, connectionQuality: ConnectionQuality) {
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

    override fun load(page: Int) {
        aftersaleApi.getRefundsList(page,10).map { responseBody ->
          val list = responseBody.toJsonObject().valueJsonArray("data")
          val result = ArrayList<AftersaleListModel>()
          if (list.length() > 0){
              list.arrayObjects().forEach { item ->
                  result.add(AftersaleListModel.map(item))
              }
          }
            return@map result
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}