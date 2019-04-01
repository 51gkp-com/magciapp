package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by LDD on 2018/1/11.
 */
interface PaymentApi {
    /** ============================== 订单支付API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:22:48
     * @Note   查询支持的支付方式
     * @param   clientType    调用客户端PC,WAP,NATIVE,REACT
     */
    @GET("order/pay/{client_type}")
    fun getPayList(@Path("client_type") clientType :String): Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:22:48
     * @Note   对一个交易发起支付
     * @param   paymentPluginId    支付插件id
     * @param   payMode    支付模式，正常normal， 二维码 ar,枚举类型PaymentPatternEnum
     * @param   clientType    调用客户端PC,WAP,NATIVE,REACT
     * @param   sn    要支付的交易sn
     * @param   tradeType    交易类型
     */
    @GET("order/pay/app/{trade_type}/{sn}")
    fun payTrade(@Path("sn") sn :String, @Path("trade_type") tradeType :String, @Query("payment_plugin_id") paymentPluginId :String, @Query("pay_mode") payMode :String, @Query("client_type") clientType :String):Observable<ResponseBody>



    /** ============================== 订单支付API  =============================== */

}