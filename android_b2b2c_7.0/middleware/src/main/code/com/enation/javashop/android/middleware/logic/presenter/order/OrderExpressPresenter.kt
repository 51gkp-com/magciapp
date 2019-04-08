package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.ExpressContract
import com.enation.javashop.android.middleware.model.OrderExpressHeaderModel
import com.enation.javashop.android.middleware.model.OrderExpressModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

class OrderExpressPresenter @Inject constructor() : RxPresenter<ExpressContract.View>() , ExpressContract.Presenter {


    @Inject
    protected lateinit var orderApi :OrderApi

    private val observer = object :ConnectionObserver<ArrayList<Any>>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<Any>, connectionQuality: ConnectionQuality) {
            if(result.size > 0){
                providerView().render(result[0] as OrderExpressHeaderModel, result[1] as ArrayList<OrderExpressModel>)
                providerView().complete()
            }else{
                providerView().complete("暂无物流信息")
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun load(sn: String) {
        orderApi.getOrderDetail(sn)
                .flatMap { responseBody ->
                    val json = responseBody.toJsonObject()
                    val id = json.valueInt("logi_id")
                    val num = json.valueString("ship_no")
                    return@flatMap orderApi.express(id,num)
                }.map { result: Result<ResponseBody> ->
                    val list = ArrayList<Any>()

                    if(!result.isError && result.response()!!.code() == 200){
                        var expressObject = result.response()!!.body()!!.toJsonObject()
                        if (expressObject.length() > 0){
                            val header = OrderExpressHeaderModel(expressObject.valueString("courier_num"),
                                    expressObject.valueString("name"))
                            var dataArray = expressObject.valueJsonArray("data")

                            var infoList = ArrayList<OrderExpressModel>()

                            dataArray.arrayObjects().forEach { item ->
                                infoList.add(OrderExpressModel(item.valueString("context"),
                                        item.valueString("time")))
                            }
                            list.add(header)
                            list.add(infoList)
                        }
                    }

                    return@map list
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}