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
interface OtherApi {

    /**
     * @author LDD
     * @From   ExtraApi
     * @Date   2018/8/16 下午3:05
     * @Note   获取关键字
     * @param  num 获取数量
     */
    @GET("MAGIC-VIPMEMBER/vip/{uname}/level")
    fun getInviteList(@Path("uname") sn :String) :Observable<ResponseBody>

}