package com.enation.javashop.android.middleware.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by LDD on 2018/1/11.
 */
interface CategoryApi{


    /**
     * @author Snow
     * @From   MemberApi
     * @Date   2018-08-14 15:43:03
     * @Note   首页等商品分类数据
     * @param   parentId    分类id，顶级为0
     */
    @GET("goods/categories/{parent_id}/children")
    fun getCat(@Path("parent_id") parentId :Int): Observable<ResponseBody>


}