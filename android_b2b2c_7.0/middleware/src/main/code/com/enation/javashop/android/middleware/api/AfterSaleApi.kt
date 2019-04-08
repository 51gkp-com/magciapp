package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author LDD
 * @Date   2018/8/15 上午10:30
 * @From   com.enation.javashop.android.middleware.api
 * @Note   售后相关API
 */
interface AfterSaleApi  {

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   买家申请退款
     * @param   orderSn    订单编号
     * @param   skuId    货品id
     * @param   returnNum    退货数量
     * @param   refundReason    退款原因
     * @param   accountType    账号类型 支付宝:ALIPAY, 微信:WEIXINPAY, 银行转账:BANKTRANSFER
     * @param   returnAccount    退款账号
     * @param   customerRemark    客户备注
     * @param   bankName    银行名称
     * @param   bankAccountNumber    银行账号
     * @param   bankAccountName    银行开户名
     * @param   bankDepositName    银行开户行
     */
    @POST("after-sales/refunds/apply")
    fun applyRefunds(@QueryMap map :HashMap<String,Any>):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   买家查看退款(货)详细
     * @param   sn    退款(货)编号
     */
    @GET("after-sales/refund/{sn}")
    fun getAfterSaleDetail(@Path("sn") sn :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   退款申请数据获取
     * @param   orderSn    订单号
     */
    @GET("after-sales/refunds/apply/{order_sn}")
    fun getRefundData(@Path("order_sn") orderSn :String ,@QueryMap map : HashMap<String,Any>):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   买家申请退货
     * @param   orderSn    订单编号
     * @param   skuId    货品id
     * @param   returnNum    退货数量
     * @param   refundReason    退款原因
     * @param   accountType    账号类型 支付宝:ALIPAY, 微信:WEIXINPAY, 银行转账:BANKTRANSFER
     * @param   returnAccount    退款账号
     * @param   customerRemark    客户备注
     * @param   bankName    银行名称
     * @param   bankAccountNumber    银行账号
     * @param   bankAccountName    银行开户名
     * @param   bankDepositName    银行开户行
     */
    @POST("after-sales/return-goods/apply")
    fun applyReturnGoods(@QueryMap map :HashMap<String,Any>):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   买家查看退款(货)列表
     * @param   pageNo    页码
     * @param   pageSize    分页数
     */
    @GET("after-sales/refunds")
    fun getRefundsList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int): Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:30:41
     * @Note   买家对已付款的订单取消操作
     * @param   orderSn    订单编号
     * @param   refundReason    退款原因
     * @param   accountType    账号类型 支付宝:ALIPAY, 微信:WEIXINPAY, 银行转账:BANKTRANSFER
     * @param   returnAccount    退款账号
     * @param   customerRemark    客户备注
     * @param   bankName    银行名称
     * @param   bankAccountNumber    银行账号
     * @param   bankAccountName    银行开户名
     * @param   bankDepositName    银行开户行
     */
    @POST("after-sales/refunds/cancel-order")
    fun cancelPaidOrder(@Query("order_sn") orderSn :String, @Query("refund_reason") refundReason :String, @Query("account_type") accountType :String, @Query("return_account") returnAccount :String, @Query("customer_remark") customerRemark :String, @Query("bank_name") bankName :String, @Query("bank_account_number") bankAccountNumber :String, @Query("bank_account_name") bankAccountName :String, @Query("bank_deposit_name") bankDepositName :String):Observable<ResponseBody>


}