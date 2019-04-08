package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.middleware.model.GoodsViewModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author LDD
 * @Date   2018/2/9 下午12:06
 * @From   com.enation.javashop.android.middleware.api
 * @Note   购物车Api
 */
interface CartApi {

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   设置全部商为选中或不选中
     * @param   checked    是否选中
     */
    @POST("trade/carts/checked")
    fun setAllCheck(@Query("checked") checked :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   更新购物车中的多个产品
     * @param   skuId    产品id数组
     * @param   checked    是否选中
     * @param   num    产品数量
     */
    @POST("trade/carts/sku/{sku_id}")
    fun updateItemState(@Path("sku_id") skuId :String, @Query("checked") checked :Int?, @Query("num") num :Int?):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   批量设置某商家的商品为选中或不选中
     * @param   sellerId    卖家id
     * @param   checked    是否选中
     */
    @POST("trade/carts/seller/{seller_id}")
    fun updateSellerGoods(@Path("seller_id") sellerId :Int,@Query("checked") checked :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   向购物车中添加一个产品
     * @param   skuId    产品ID
     * @param   num    此产品的购买数量
     * @param   activityId    默认参与的活动id
     */
    @POST("trade/carts")
    fun add(@Query("sku_id") skuId :Int,@Query("num") num :Int ,@Query(value = "activity_id") id : Int?):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   获取购物车列表
     * @param   showType    显示方式
     */
    @GET("trade/carts/{show_type}")
    fun getCartData(@Path("show_type") showType :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   清空购物车
     */
    @DELETE("trade/carts")
    fun cleanCart():Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   删除购物车中的一个或多个产品
     * @param   skuIds    产品id，多个产品可以用英文逗号：(,) 隔开
     */
    @DELETE("trade/carts/{sku_ids}/sku")
    fun delete(@Path("sku_ids") skuIds :String):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:44:40
     * @Note   立即购买
     * @param   skuId    产品ID
     * @param   num    此产品的购买数量
     * @param   activityId    默认参与的活动id
     */
    @POST("trade/carts/buy")
    fun buy(@Query("sku_id") skuId :Int,@Query("num") num :Int ,@Query(value = "activity_id") id : Int?):Observable<ResponseBody>

}