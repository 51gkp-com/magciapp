package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.bindingParams
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateReceiptContract
import com.enation.javashop.android.middleware.model.ReceiptContentViewModel
import com.enation.javashop.android.middleware.model.ReceiptViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/23 下午6:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单创建发票逻辑控制器
 */
class OrderCreateReceiptPresenter @Inject constructor() : RxPresenter<OrderCreateReceiptContract.View>() , OrderCreateReceiptContract.Presenter {

    @Inject
    protected lateinit var tradeApi: TradeApi

    private val observer = object : ConnectionObserver<String>(){
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
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptPresenter
     * @Date   2018/5/23 下午6:52
     * @Note   加载历史发票
     */
    override fun loadReceiptList() {
        providerView().renderReceiptList(ArrayList<ReceiptViewModel>().then {
            self ->
            for (i in 1..5){
                self.add(ReceiptViewModel(1,"90213913919239Y".bindingParams(),"商品详细","易族智汇(北京)科技有限公司".bindingParams(),"公司"))
            }
        })
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptPresenter
     * @Date   2018/5/23 下午6:51
     * @Note   新增发票
     * @param  data 发票
     */
    override fun addReceipt(data: ReceiptViewModel) {

    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptPresenter
     * @Date   2018/5/23 下午6:54
     * @Note   加载发票内容
     */
    override fun loadReceiptContent() {
        providerView().renderReceiptContent(ArrayList<ReceiptContentViewModel>().then {
            self ->
            self.add(ReceiptContentViewModel(1,"食品"))
            self.add(ReceiptContentViewModel(2,"耗材"))
            self.add(ReceiptContentViewModel(3,"办公用品"))
            self.add(ReceiptContentViewModel(4,"商品详细"))
        })
    }

    override fun setReceipt(receiptViewModel: ReceiptViewModel) {
        var observable :Observable<String>
        if (receiptViewModel.receipt_content == ""){
            observable = tradeApi.deleteReceipt().map { "操作成功" }
        }else{
            observable = tradeApi.setReceipt((receiptViewModel.receipt_type =="person").judge("0","1") ,(receiptViewModel.receipt_title.get() == "").judge("个人",receiptViewModel.receipt_title.get()),receiptViewModel.receipt_content,receiptViewModel.duty_invoice.get()).map { "操作成功" }
        }
        observable.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptPresenter
     * @Date   2018/5/23 下午6:51
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}