package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreatePayShipContract
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import okhttp3.Connection
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/23 下午6:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单创建支付方式逻辑控制器
 */
class OrderCreatePayShipPresenter @Inject constructor() : RxPresenter<OrderCreatePayShipContract.View>() , OrderCreatePayShipContract.Presenter {

    @Inject
    protected lateinit var tradeApi:TradeApi

    /**
     * @author LDD
     * @From   OrderCreatePayShipPresenter
     * @Date   2018/5/23 下午6:50
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }


    override fun setData(time: String, payType: String) {

        val timeNet = tradeApi.setTime(time)

        val payNet = tradeApi.setPaymentType(payType)

        Observable.zip(timeNet,payNet, BiFunction<ResponseBody,ResponseBody,String> { t1, t2 ->
            return@BiFunction "修改成功"
        }).compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(object :ConnectionObserver<String>(){
                    override fun onStartWithConnection() {
                        providerView().start()
                    }

                    override fun onNextWithConnection(result: String, connectionQuality: ConnectionQuality) {
                        providerView().complete(result)
                    }

                    override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
                        providerView().onError(error.customMessage)
                    }

                    override fun attachSubscribe(var1: Disposable) {
                        addDisposable(var1)
                    }
                })

    }

}