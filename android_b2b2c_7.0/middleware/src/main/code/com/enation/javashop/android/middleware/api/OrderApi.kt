package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by LDD on 2018/1/11.
 */
interface OrderApi {

    /** ============================== 物流查询接口  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   查询物流详细
     * @param   id    物流公司id
     * @param   num    快递单号
     */
    @GET("express")
    fun express(@Query("id") id :Int, @Query("num") num :String): Observable<Result<ResponseBody>>


    /** ============================== 会员订单API  =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   查询单个订单明细
     * @param   orderSn    订单编号
     */
    @GET("trade/orders/{order_sn}")
    fun getOrderDetail(@Path("order_sn") orderSn :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   根据交易编号或者订单编号查询收银台数据
     * @param   tradeSn    交易编号
     * @param   orderSn    订单编号
     */
    @GET("trade/orders/cashier")
    fun getCashier(@Query("trade_sn") tradeSn :String,@Query("order_sn") orderSn :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   根据交易编号查询订单列表
     * @param   tradeSn    交易编号
     */
    @GET("trade/orders/{trade_sn}/list")
    fun getOrderListByTradeSn(@Path("trade_sn") tradeSn :String):Observable<ResponseBody>



    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   确认收货
     * @param   orderSn    订单编号
     */
    @POST("trade/orders/{order_sn}/rog")
    fun rog(@Path("order_sn") orderSn :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   查询会员订单列表
     * @param   goodsName    商品名称关键字
     * @param   orderStatus    订单状态
     * @param   pageNo    页数
     * @param   pageSize    条数
     */
    @GET("trade/orders")
    fun getOrderList(@Query("order_status") orderStatus :String,
                     @Query("page_no") pageNo :Int,
                     @Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   取消订单
     * @param   orderSn    订单编号
     * @param   reason    取消原因
     */
    @POST("trade/orders/{order_sn}/cancel")
    fun cancel(@Path("order_sn") orderSn :String,@Query("reason") reason :String):Observable<ResponseBody>


}