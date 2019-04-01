package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by LDD on 2018/1/11.
 */
interface ShopApi {

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查看会员是否收藏店铺
     * @param   id    要检索的收藏店铺id
     */
    @GET("members/collection/shop/{id}")
    fun getIsCollectionShop(@Path("id") id :Int): Observable<ResponseBody>



    /** ============================== 店铺分组相关API  =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:14:35
     * @Note   查询店铺分组列表
     * @param   shopId    店铺id
     */
    @GET("shops/cats/{shop_id}")
    fun getShopCats(@Path("shop_id") shopId :Int):Observable<ResponseBody>


    /** ============================== 店铺相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:14:35
     * @Note   查询店铺列表
     * @param   pageNo    页码
     * @param   pageSize    分页数
     * @param   name    店铺名称
     * @param   order    按好评率排序
     */
    @GET("shops/list")
    fun getShopList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int,@Query("name") name :String):Observable<ResponseBody>



    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:14:35
     * @Note   获取店铺信息(未登录状态)
     * @param   shopId    店铺id
     */
    @GET("shops/{shop_id}")
    fun getShop(@Path("shop_id") shopId :Int):Observable<ResponseBody>




    /** ============================== 店铺幻灯片相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 16:14:35
     * @Note   查询店铺幻灯片列表
     * @param   shopId    店铺id
     */
    @GET("shops/sildes/{shop_id}")
    fun getSildes(@Path("shop_id") shopId :Int):Observable<ResponseBody>

}