package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.lib.utils.UUID
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by LDD on 2018/8/10.
 */
interface BaseApi {
    @GET("captchas/{uuid}/{scene}")
    fun loadVcode(@Path("uuid") uuid :String = UUID.uuid,@Path("scene") scene :String) : Observable<ResponseBody>

    @GET("regions/{id}/children")
    fun loadRegion(@Path("id") id : Int) :Observable<ResponseBody>

    @Multipart
    @POST("uploaders")
    fun uploader(@Part() file : MultipartBody.Part , @Query("scene") scene :String) :Observable<ResponseBody>
}