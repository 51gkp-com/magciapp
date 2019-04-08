package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by LDD on 2018/1/11.
 */
interface TradeApi {

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   获取结算参数
     */
    @GET("trade/checkout-params")
    fun getParam(): Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   创建交易
     */
    @POST("trade/create")
    fun create(@Query("client")client :String = "APP"): Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   使用优惠券
     * @param   sellerId    店铺ID
     * @param   mcId    优惠券ID
     */
    @POST("trade/promotion/{seller_id}/seller/{mc_id}/coupon")
    fun useCoupon(@Path("seller_id") sellerId :Int, @Path("mc_id") mcId :Int): Observable<ResponseBody>

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   结算页—读取可用的优惠券列表
     * @param   sellerIds    商家ID集合
     */
    @GET("members/coupon/{seller_ids}")
    fun getCouponListbyUsed(@Path("seller_ids") sellerIds :String):Observable<ResponseBody>


    @POST("trade/checkout-params/address-id/{id}")
    fun setAddress(@Path("id") id :Int):Observable<ResponseBody>

    @POST("trade/checkout-params/payment-type")
    fun setPaymentType(@Query("payment_type") payment_type :String):Observable<ResponseBody>

    @POST("trade/checkout-params/receipt")
    fun setReceipt(@Query("type") receiptType :String,
                   @Query("receipt_title") receiptTitle :String,
                   @Query("receipt_content") receiptContent :String,
                   @Query("tax_no") dutyInvoice :String):Observable<ResponseBody>

    @DELETE("trade/checkout-params/receipt")
    fun deleteReceipt():Observable<ResponseBody>

    @POST("trade/checkout-params/receive-time")
    fun setTime(@Query("receive_time") time :String):Observable<ResponseBody>

    @POST("trade/checkout-params/remark")
    fun setRemark(@Query("remark") remark : String ):Observable<ResponseBody>

}