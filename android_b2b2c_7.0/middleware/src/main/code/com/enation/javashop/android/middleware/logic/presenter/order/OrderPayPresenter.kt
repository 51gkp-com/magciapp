package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.getJsonString
import com.enation.javashop.android.lib.utils.toJsonArray
import com.enation.javashop.android.middleware.api.PaymentApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderPayContract
import com.enation.javashop.android.middleware.model.OrderPayModel
import com.enation.javashop.android.middleware.model.PayMethodViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/22 上午10:29
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   OrderPayPresenter
 */
class OrderPayPresenter @Inject constructor():RxPresenter<OrderPayContract.View>(),OrderPayContract.Presenter {

    @Inject
    protected lateinit var paymentApi: PaymentApi

    protected val observer = object :ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            providerView().complete("")
            if (result is ArrayList<*>){
                providerView().renderPayMethod(result as ArrayList<PayMethodViewModel>)
            }
            if (result is String){
                providerView().callApp(result)
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
     * @From   OrderPayPresenter
     * @Date   2018/4/24 下午1:28
     * @Note   去支付
     */
    override fun toPay(pay: OrderPayModel) {
        paymentApi.payTrade(pay.sn,pay.tradeType.toString(),pay.payId,pay.paymode,pay.client)
                .map { it.getJsonString() }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)

    }

    /**
     * @author LDD
     * @From   OrderPayPresenter
     * @Date   2018/4/24 下午1:29
     * @Note   加载支付方式
     */
    override fun loadPayMethod() {
        paymentApi.getPayList("NATIVE")
                .map { responseBody ->
                    val result = ArrayList<PayMethodViewModel>()
                    responseBody.toJsonArray().arrayObjects().forEach { item ->
                        val item = PayMethodViewModel.map(item)
                        if(item.name.contains("微信") || item.name.contains("支付宝")){
                            result.add(item)
                        }
                    }
                    return@map result
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   OrderPayPresenter
     * @Date   2018/4/24 下午1:29
     * @Note   依赖注入
     * @param  ...
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}