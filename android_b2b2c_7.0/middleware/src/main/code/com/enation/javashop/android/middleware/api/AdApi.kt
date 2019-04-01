package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.middleware.model.AdViewModel
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author LDD
 * @Date   2018/1/16 下午12:16
 * @From   com.enation.javashop.android.middleware.api
 * @Note   广告API
 */
interface AdApi {

    /**
     * @author LDD
     * @From   AdApi
     * @Date   2018/1/16 下午12:16
     * @Note   获取广告
     * @return 广告列表
     */
    @GET("TestMode")
    fun getAd(): Observable<ArrayList<AdViewModel>>

}