package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by LDD on 2018/1/11.
 */
interface GoodsApi {

    /** ============================== 商品相关API   =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   浏览商品的详情,静态部分使用
     * @param   goodsId    分类id，顶级为0
     */
    @GET("goods/{goods_id}")
    fun getGoodsDetail(@Path("goods_id") goodsId :Int):Observable<ResponseBody>

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:14:05
     * @Note   查询会员是否收藏商品
     * @param   id    商品id
     */
    @GET("members/collection/goods/{id}")
    fun getCollectionState(@Path("id") id :Int):Observable<Result<ResponseBody>>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询某商品的咨询
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     * @param   goodsId    商品ID
     */
    @GET("goods/{goods_id}/asks")
    fun getAsksList (@Path("goods_id") goodsId :Int,@Query("page_no") pageNo :Int,
                 @Query("page_size") pageSize :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   获取sku信息，商品详情页动态部分
     * @param   goodsId    分类id，顶级为0
     */
    @GET("goods/{goods_id}/skus")
    fun getGoodsSku(@Path("goods_id") goodsId :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询某商品的评论
     * @param   grade    好中差评
     * @param   haveImage    是否有图
     * @param   pageNo    页码
     * @param   pageSize    每页显示数量
     * @param   goodsId    商品ID
     */
    @GET("members/comments/goods/{goods_id}")
    fun listComment(@Path("goods_id") goodsId :Int,@Query("grade") grade :String?,
                    @Query("have_image") haveImage :Boolean? , @Query("page_no") pageNo :Int,
                    @Query("page_size") pageSize :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   记录浏览器商品次数
     * @param   goodsId    商品ID
     */
    @GET("goods/{goods_id}/visit")
    fun visitGoods(@Path("goods_id") goodsId :Int):Observable<ResponseBody>

    /** ============================== 商品检索相关API  =============================== */


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询商品选择器
     * @param   pageNo    页码
     * @param   pageSize    每页数量
     * @param   keyword    关键字
     * @param   category    分类
     * @param   brand    品牌
     * @param   price    价格
     * @param   sort    排序:关键字_排序
     * @param   prop    属性:参数名_参数值@参数名_参数值
     * @param   sellerId    卖家id，搜索店铺商品的时候使用
     */
//    @GET("goods/search/selector")
//    fun searchGoodsSelector(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int,
//                            @Query("keyword") keyword :String,@Query("category") category :Int,
//                            @Query("brand") brand :Int,@Query("price") price :String,@Query("sort") sort :String,
//                            @Query("prop") prop :String,@Query("seller_id") sellerId :Int):Observable<ResponseBody>

    @GET("goods/search/selector")
    fun searchGoodsSelector(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE,
                            @QueryMap() map : HashMap<String,Any>):Observable<ResponseBody>



    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询商品列表
     * @param   pageNo    页码
     * @param   pageSize    每页数量
     * @param   keyword    关键字
     * @param   category    分类
     * @param   brand    品牌
     * @param   price    价格
     * @param   sort    排序:关键字_排序
     * @param   prop    属性:参数名_参数值@参数名_参数值
     * @param   sellerId    卖家id，搜索店铺商品的时候使用
     */
//    @GET("goods/search")
//    fun searchGoodsList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE,
//                        @Query("keyword") keyword :String? = null,
//                        @Query("sort") sort :String? = null,@Query("prop") prop :String? = null,
//                        @Query("category") category :Int? = null,@Query("price") price :String? = null,
//                        @Query("brand") brand :Int? = null, @Query("seller_id") sellerId :Int? = null):Observable<ResponseBody>
    @GET("goods/search")
    fun searchGoodsList(@Query("page_no") pageNo :Int,@Query("page_size") pageSize :Int = ApiManager.PAGE_SIZE,
                        @QueryMap() map : HashMap<String,Any>):Observable<ResponseBody>
    /** ============================== 标签商品相关API  =============================== */

    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询标签商品列表
     * @param   sellerId    卖家id
     * @param   mark    hot热卖 new新品 recommend推荐
     * @param   num    查询数量
     */
    @GET("goods/tags/{mark}/goods")
    fun getTagGoodsList(@Path("mark") mark :String, @Query("seller_id") sellerId :Int,
                    @Query("num") num :Int):Observable<ResponseBody>


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   查询商品销量
     * @param   goodsId    商品id
     */
    @GET("goods/tags/count")
    fun getGoodsBuyCount(@Query("goods_id") goodsId :String): Observable<ResponseBody>

    /**
     * 查询店铺商品数量
     * @param shopId 店铺id
     */
    @GET("goods/tags/{shopId}/goods-num")
    fun getGoodsNumForShop(@Path("shopId") shopId :Int):Observable<ResponseBody>

}