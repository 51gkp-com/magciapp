package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.lib.utils.UUID
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.*

/**
 * @author LDD
 * @Date   2018/8/13 上午11:00
 * @From   com.enation.javashop.android.middleware.api
 * @Note   护照
 */
interface PassportApi {
    /**
     * @author LDD
     * @From   MemberApi
     * @Date   2018/8/9 下午4:35
     * @Note   登录API
     * @param  username 用户名
     * @param  password 密码
     * @param  captcha  验证码
     * @param  uuid     UUID
     */
    @GET("passport/login")
    fun login(@Query("username") username :String, @Query("password") password :String,
              @Query("captcha") captcha :String, @Query("uuid") uuid :String = UUID.uuid): Observable<ResponseBody>

    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 下午3:21
     * @Note   手机登录
     * @param  mobile  手机登录
     * @param  smsCode 短信
     * @param  uuid    uuid
     */
    @GET("passport/login/{mobile}")
    fun mobileLogin(@Path("mobile") mobile : String , @Query("sms_code") smsCode: String, @Query("uuid") uuid :String = UUID.uuid) : Observable<ResponseBody>

    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 上午10:27
     * @Note   发送找回密码短信
     * @param  uuid 客户端唯一标示
     * @param  captcha 验证码
     */
    @POST("passport/find-pwd/send")
    fun sendMessageFindPassword(@Query("uuid") uuid :String = UUID.uuid , @Query("captcha") captcha :String): Observable<ResponseBody>

    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 上午10:29
     * @Note   发送手机注册短信
     * @param  uuid    客户端唯一标示
     * @param  captcha 验证码
     * @param  mobile  手机号
     */
    @POST("passport/register/smscode/{mobile}")
    fun sendMessageRegister(@Path("mobile") mobile : String , @Query("uuid") uuid :String = UUID.uuid ,@Query("captcha") captcha :String ): Observable<ResponseBody>

    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 上午10:29
     * @Note   发送手机登录短信
     * @param  uuid    客户端唯一标示
     * @param  captcha 验证码
     * @param  mobile  手机号
     */
    @POST("passport/login/smscode/{mobile}")
    fun sendMessageLogin(@Path("mobile") mobile : String , @Query("uuid") uuid :String = UUID.uuid ,@Query("captcha") captcha :String): Observable<ResponseBody>


    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 上午10:29
     * @Note   发送手机登录短信
     * @param  smsCode 手机号验证码
     * @param  scene   场景
     * @param  mobile  手机号
     */
    @GET("passport/smscode/{mobile}")
    fun checkMobileMessage(@Path("mobile") mobile : String , @Query("scene") scene :String ,@Query("sms_code") smsCode :String): Observable<ResponseBody>

    /**
     * @author LDD
     * @From   PassportApi
     * @Date   2018/8/14 下午12:19
     * @Note   注册
     * @param  mobile 手机号
     * @param  password 密码
     */
    @POST("passport/register/wap")
    fun register(@Query("mobile") mobile :String , @Query("password") password :String): Observable<ResponseBody>

    @GET("passport/find-pwd/valid")
    fun validFindCode(@Query("sms_code") code :String,@Query("uuid") uuid :String = UUID.uuid): Observable<ResponseBody>

    @PUT("passport/find-pwd/update-password")
    fun findPassword(@Query("uuid") uuid :String , @Query("password") password :String): Observable<ResponseBody>

    @GET("passport/login-binder/ali/info")
    fun getAlipayAuthInfo() :Observable<ResponseBody>

    @GET("passport/connect/app/{type}/openid")
    fun authLogin(@Path("type")type:String,@Query("openid") openid:String) :Observable<ResponseBody>

    @POST("passport/sms-binder/app")
    fun authMobileBind(@Query("sms_code") smsCode :String,
                       @Query("openid") openid :String,
                       @Query("type") type :String,
                       @Query("mobile") mobile :String,
                       @Query("uuid") uuid :String = UUID.uuid):Observable<ResponseBody>

    @POST("passport/login-binder/app")
    fun authNormalBind(@Query("openid") openid :String,
                       @Query("type") type :String,
                       @Query("username") username :String,
                       @Query("password") password: String,
                       @Query("captcha") captcha :String,
                       @Query("uuid") uuid :String = UUID.uuid):Observable<ResponseBody>
}