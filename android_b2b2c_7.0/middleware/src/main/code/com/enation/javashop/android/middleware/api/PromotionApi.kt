package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by LDD on 2018/1/11.
 */
interface PromotionApi {


    /** ============================== 优惠券相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询商家优惠券列表
     * @param   sellerId    商家ID
     */
    @GET("promotions/coupons")
    fun getSellerCoupon(@Query("seller_id") sellerId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询所有优惠券
     * @param   pageNo    页码
     * @param   pageSize    条数
     * @param   sellerId    商家ID
     */
    @GET("promotions/coupons/all")
    fun getAllCoupon(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE):Observable<ResponseBody>


    /** ============================== 积分商品相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询积分分类集合
     */
    @GET("promotions/exchange/cats")
    fun getExchangeCats():Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询积分商品
     * @param   catId    积分分类id
     * @param   pageNo    页码
     * @param   pageSize    条数
     */
    @GET("promotions/exchange/goods")
    fun getExchangeGoods(@QueryMap map :HashMap<String,Any>):Observable<ResponseBody>



    /** ============================== 团购相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询团购活动的信息
     * @param   activeId    团购活动主键
     */
    @GET("promotions/group-buy/active")
    fun getGroupBuy(@Query("active_id") activeId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询团购商品
     * @param   catId    团购分类id
     * @param   pageNo    页码
     * @param   pageSize    条数
     */
    @GET("promotions/group-buy/goods")
    fun getGroupBuyGoods(@QueryMap map :HashMap<String,Any>):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   查询团购分类的所有标签
     */
    @GET("promotions/group-buy/cats")
    fun getGroupBuyCat():Observable<ResponseBody>


    /** ============================== 促销活动相关API  =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   根据商品读取参与的所有活动
     * @param   goodsId    商品ID
     */
    @GET("promotions/{goods_id}")
    fun getPromotion(@Path("goods_id") goodsId :Int):Observable<ResponseBody>


    /** ============================== 限时抢购相关API  =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   根据参数读取限时抢购的商品列表
     * @param   rangeTime    时刻
     * @param   pageNo    页码
     * @param   pageSize    条数
     */
    @GET("promotions/seckill/goods-list")
    fun getSeckillGoods(@Query("range_time") rangeTime :Int,@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:07:24
     * @Note   读取秒杀时刻
     */
    @GET("promotions/seckill/time-line")
    fun getSeckillTimeLine(): Observable<ResponseBody>


    @POST("trade/promotion")
    fun changeActivity(@Query("seller_id") sellerId: Int,
                       @Query("sku_id") skuId :Int,
                       @Query("activity_id") actId :Int,
                       @Query("promotion_type") promotionType :String): Observable<ResponseBody>

    @DELETE("trade/promotion")
    fun deleteActivity(@Query("seller_id") sellerId: Int,
                       @Query("sku_id") skuId :Int): Observable<ResponseBody>
}