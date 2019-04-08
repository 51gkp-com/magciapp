package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.AfterSaleApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderAfterSaleContract
import com.enation.javashop.android.middleware.model.AfterSaleViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.GoodsViewModel
import com.enation.javashop.android.middleware.model.ReturnData
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/23 上午10:52
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单售后逻辑控制
 */
class OrderAfterSalePresenter @Inject constructor() :RxPresenter<OrderAfterSaleContract.View>() ,OrderAfterSaleContract.Presenter {

    @Inject protected lateinit var afterSaleApi: AfterSaleApi

    private val observer = object :ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if (result is AfterSaleViewModel){
                providerView().renderAfterSaleUI(result)
                providerView().complete("")
            }
            if (result is String){
                providerView().complete(result)
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
     * @From   OrderAfterSalePresenter
     * @Date   2018/4/23 上午10:52
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePresenter
     * @Date   2018/4/23 上午10:52
     * @Note   加载售后数据
     * @param  orderSn   订单号
     * @param  skuId     单个商品时的SkuId
     */
    override fun loadAfterSaleData(orderSn: String, skuId: Int) {
        val map = HashMap<String,Any>()
        if (skuId > 0){
            map.put("sku_id",skuId)
        }
        afterSaleApi.getRefundData(orderSn,map).map { responseBody ->
            val json = responseBody.toJsonObject()
            val data = AfterSaleViewModel()
            data.originalWay = json.valueString("original_way")
            data.returnCountMoney = json.valueDouble("return_money")
            data.returnPoint = json.valueInt("return_point")
            json.valueJsonArray("sku_list").arrayObjects().forEach {
                data.returnGoodsList.add(GoodsItemViewModel.afterMap(it))
            }
            return@map data
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePresenter
     * @Date   2018/4/23 上午10:53
     * @Note   退货
     * @param  returnData  参数
     */
    override fun commitGoods(returnData: ReturnData) {
        val map = HashMap<String,Any>()
        map["order_sn"] = returnData.orderSn
        if (returnData.skuId > 0){
            map["sku_id"] = returnData.skuId
            map["return_num"] = returnData.returnNum
        }
        map["refund_reason"] = returnData.refundReason
        map["customer_remark"] = returnData.customerRemark
        map["account_type"] = returnData.accountType
        if (returnData.accountType == "BANKTRANSFER"){
            map["bank_name"] = returnData.bankName
            map["bank_account_number"] = returnData.bankAccountNumber
            map["bank_account_name"] = returnData.bankAccountName
            map["bank_deposit_name"] = returnData.bankDepositName
        }else{
            map["return_account"] = returnData.returnAccount
        }
        afterSaleApi.applyReturnGoods(map).map { return@map "退货成功" }
                .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePresenter
     * @Date   2018/4/23 上午10:54
     * @Note   退款
     * @param  returnData 退款
     */
    override fun commitMoney(returnData: ReturnData) {
        val map = HashMap<String,Any>()
        map["order_sn"] = returnData.orderSn
        if (returnData.skuId > 0){
            map["sku_id"] = returnData.skuId
            map["return_num"] = returnData.returnNum
        }
        map["refund_reason"] = returnData.refundReason
        map["customer_remark"] = returnData.customerRemark
        map["account_type"] = returnData.accountType
        if (returnData.accountType == "BANKTRANSFER"){
            map["bank_name"] = returnData.bankName
            map["bank_account_number"] = returnData.bankAccountNumber
            map["bank_account_name"] = returnData.bankAccountName
            map["bank_deposit_name"] = returnData.bankDepositName
        }else{
            map["return_account"] = returnData.returnAccount
        }
        afterSaleApi.applyRefunds(map).map { return@map "退货成功" }
                .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}