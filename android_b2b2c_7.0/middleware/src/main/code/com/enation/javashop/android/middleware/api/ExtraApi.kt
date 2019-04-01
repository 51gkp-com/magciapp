package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author LDD
 * @Date   2018/8/16 下午3:03
 * @From   com.enation.javashop.android.middleware.api
 * @Note   额外API
 */
interface ExtraApi {

    /**
     * @author LDD
     * @From   ExtraApi
     * @Date   2018/8/16 下午3:05
     * @Note   获取关键字
     * @param  num 获取数量
     */
    @GET("pages/hot-keywords")
    fun getKeyword(@Query("num") num :Int) :Observable<ResponseBody>

    @GET("focus-pictures")
    fun getBanner(@Query("client_type") type : String = "WAP"):Observable<ResponseBody>

    @GET("pages/site-navigations")
    fun getMenu(@Query("client_type") type : String = "MOBILE"):Observable<ResponseBody>

    /**
     * @author LDD
     * @From   ExtraApi
     * @Date   2018/8/16 下午3:08
     * @Note   获取楼层数据
     * @param  clientType
     */
    @GET("pages/{client_type}/{page_type}")
    fun getFloor(@Path("client_type") clientType :String = "WAP", @Path("page_type") pageType :String = "INDEX") :Observable<ResponseBody>
}