package com.enation.javashop.android.middleware.api

import android.content.Context
import android.util.Log
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.net.engine.plugin.cookies.PersistentCookieJar
import com.enation.javashop.net.engine.plugin.cookies.cache.SetCookieCache
import com.enation.javashop.net.engine.plugin.cookies.persistence.SharedPrefsCookiePersistor
import com.enation.javashop.utils.base.cache.ACache
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import okhttp3.RequestBody
import okio.Buffer
import java.io.IOException


/**
 * @author LDD
 * @Date   2018/8/10 上午8:28
 * @Note   网络工厂类
 */
class NetFectory {

    companion object {

        /**
         * @author LDD
         * @From    NetFectory
         * @Date   2018/8/13 下午4:48
         * @Note   构建
         * @param  context 上下文
         */
        fun build(context: Context): NetFectory {
            return NetFectory(context)
        }

    }

    /**
     * @Name  DEFAULT_TIMEOUT
     * @Type  Long
     * @Note  默认超时
     */
    private val DEFAULT_TIMEOUT: Long = 10

    /**
     * @Name  context
     * @Type  Context
     * @Note  上下文
     */
    private val context :Context

    /**
     * 构造
     */
    private constructor(context: Context) {
        this.context = context
        this.okHttpClient = OkHttpClient.Builder().
                cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))).
                connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS).
                readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                addInterceptor(getLog()).
                addInterceptor(TokenInterceptor(context)).
                cache(Cache(File(context.cacheDir, "NetCache"), (10 * 1024 * 1024).toLong())).
                build()
    }

    /**
     * OkHttp客户端
     */
    private val okHttpClient :OkHttpClient

    /**
     * GSON 格式化对象
     */
    private val mGsonDateFormat: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create()

    /**
     * 配置Logger
     * @return
     */
    private fun getLog(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.e("Net_JavaShop", message) })
        return logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * @author LDD
     * @From   NetFectory
     * @Date   2018/8/13 下午4:50
     * @Note   创建Api服务
     * @param  serviceClass apiClass
     * @param  url 基础URL
     */
    fun <S> createService(serviceClass: Class<S>, url: String): S {

        //构建服务
        return Retrofit.Builder()
                //配置基础URLs
                .baseUrl(url)
                //配置网络客服端
                .client(okHttpClient)
                //添加Gson转换，json直接转换为相应格式的对象
                .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                //添加Rxjava回调语法
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //构建
                .build().create(serviceClass)
    }
}

/**
 * @author LDD
 * @Date   2018/8/10 上午9:45
 * @From   NetFactory
 * @Note   token全局刷新器
 */
class TokenInterceptor : Interceptor{

    /**
     * token管理器
     */
    private val tokenManager : TokenManager

    /**
     * 构造方法
     */
    constructor(context: Context) {
        tokenManager = TokenManager(context)
    }

    /**
     * @author LDD
     * @From   TokenInterceptor
     * @Date   2018/8/10 上午9:46
     * @Note   开始拦截
     * @param  chain   数据
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        /**保存原始Request*/
        var originalRequest = chain.request()

        /**保存builder*/
        val builder = originalRequest.newBuilder()

        /**设置token*/
        initToken(builder,originalRequest)

        /**全局添加uuid*/
        originalRequest = builder.addHeader("uuid",UUID.uuid).build()

        /**获取请求相应体*/
        val response = chain.proceed(originalRequest)

        /**判断Token是否失效  如果失效 进入该方法 刷新token*/
        if (checkToken(response)) {

            /**拿到刷新Token*/
            var refreshToken = tokenManager.getRefreshToken()

            /**判断是否持有刷新token 不持有的话 抛出未登录异常*/
            if (refreshToken == null){
                throw Throwable("未登录")
            }

            /**刷新token值*/
            val result = ApiManager.refreshToken(refreshToken)

            /**刷新token失效 清空数据 抛异常*/
            if (result.code() != 200) {
                tokenManager.clearToken()
                tokenManager.removeUid()
                MemberState.manager.clearMember()
                /**发送退出登录广播*/
                getEventCenter().post(LogoutEvent())
                throw Throwable("未登录")
            }

            /** 获取到新的token  初始化JsonObject*/
            val jsonResult = JSONObject(result.body()!!.getJsonString())

            /** 拿到权限token */
            val accessToken = jsonResult.getString("accessToken")

            /** 拿到刷新token */
            refreshToken = jsonResult.getString("refreshToken")

            /** 刷新token */
            tokenManager.updateToken(refreshToken,accessToken)

            var requestBody : RequestBody? = null

            if (originalRequest.body() is FormBody){
                /**初始化新的formbody*/
                val newBuilder = FormBody.Builder()

                /**判断原始request的body是否为空 不为空的情况下 将原始请求的body数据 循环设置到新body中*/
                if (originalRequest.body() != null){
                    var originalFormBody = originalRequest.body() as FormBody
                    for (i in 0 until originalFormBody.size()) {
                        newBuilder.add(originalFormBody.name(i), originalFormBody.value(i))
                    }
                }

                /** 初始化body */
                requestBody = newBuilder.add("clintType","android").build()
            }else if (originalRequest.body() is RequestBody){
                val jsonObject: JSONObject
                if (originalRequest.body()!!.contentLength() == 0L) {
                    jsonObject = JSONObject()
                } else {
                    jsonObject = JSONObject(getParamContent(originalRequest.body()!!))
                }
                requestBody = RequestBody.create(originalRequest.body()!!.contentType(), jsonObject.toString())
            }


            /** 构建Request */
            val newRequest = originalRequest.newBuilder()
                    .method(originalRequest.method(),(originalRequest.body() == null).judge(null,requestBody))

            /**设置token*/
            initToken(newRequest,originalRequest)

            /** 放入拦截链路，重新请求 */
            return chain.proceed(newRequest.build())

        } else {

            /**如果进入该方法 代表token未失效*/

            if(originalRequest.url().encodedPath().contains("members/logout") && response.code() == 200){
                tokenManager.removeUid()
                tokenManager.clearToken()
                /**如果该次请求是登录请求 则在登录请求中获取 token和uid 存储或更新到本地*/
            } else if ((originalRequest.url().encodedPath().contains("passport/connect/app")
                            || originalRequest.url().encodedPath().contains("passport/sms-binder/app")
                            || originalRequest.url().encodedPath().contains("passport/login-binder/app")
                            || originalRequest.url().encodedPath().contains("/passport/login")
                            || originalRequest.url().encodedPath().contains("passport/register/wap"))
                    && response.code() == 200){

                /**获取Json字符串*/
                val source = response.body()!!.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer()
                val result = buffer.clone().readString(Charset.forName("UTF-8"))

                /**当json不为空 获取uid与token 并更新*/
                if (result != null &&
                        result != "" &&
                        result.contains("access_token") &&
                        result.contains("uid") &&
                        result.contains("refresh_token")) {

                    /**初始化JSON OBJECT*/
                    val jsonResult = JSONObject(result)

                    if (jsonResult.has("uid") && jsonResult.has("access_token") && jsonResult.has("refresh_token")){
                        /**获取uid*/
                        val uid = jsonResult.getInt("uid")

                        /**获取权限Token*/
                        val accessToken = jsonResult.getString("access_token")

                        /**获取刷新token*/
                        val refreshToken = jsonResult.getString("refresh_token")

                        /**刷新token*/
                        tokenManager.updateToken(refreshToken,accessToken)

                        /**刷新uid*/
                        tokenManager.updateUid(uid)
                    }

                }
            }

            return response

        }
    }

    /**
     * @author LDD
     * @From   TokenInterceptor
     * @Date   2018/8/13 下午5:02
     * @Note   检查token是否失效
     * @param  response 相应体
     */
    private fun checkToken(response :Response) :Boolean{

        if (response.code() == 401 || response.code() == 403){
            return  true
        }

        return false
    }

    /**
     * 获取常规post请求参数
     */
    @Throws(IOException::class)
    private fun getParamContent(body: RequestBody): String {
        val buffer = Buffer()
        body.writeTo(buffer)
        return buffer.readUtf8()
    }

    /**
     * @author LDD
     * @From   TokenInterceptor
     * @Date   2018/8/13 下午5:02
     * @Note   设置请求体中的token参数
     * @param  builder 请求体构建者
     * @param  originalRequest 原始请求体
     */
    private fun initToken(builder :Request.Builder , originalRequest :Request){

        /**拿到权限token*/
        val accessToken = tokenManager.getAccessToken()

        /**拿到uid*/
        val uid = tokenManager.getUid()

        /**判断APP 环境*/
        if (JavaShopConfigCenter.INSTANCE.APP_DEV) {

            /**开发环境时  只设置权限token*/
            if (accessToken != null ){
                builder.removeHeader("Authorization")
                builder.addHeader("Authorization",accessToken)
            }

        }else{

            /** 生产环境时 设置 随机字符串 时间戳 签名 uid */
            if (accessToken != null || uid != null){

                val nonce = ((Math.random()*9+1)*100000).toInt().toString()

                val timestamp = (System.currentTimeMillis()/1000).toString()

                val sign = MD5Util.MD5Encode("$uid$nonce$timestamp$accessToken",null)

                val orgUrl = originalRequest.url()

                builder.url(orgUrl.newBuilder().
                        removeAllQueryParameters("uid").
                        removeAllQueryParameters("nonce").
                        removeAllQueryParameters("timestamp").
                        removeAllQueryParameters("sign").
                        addQueryParameter("uid",uid).
                        addQueryParameter("nonce",nonce).
                        addQueryParameter("timestamp",timestamp).
                        addQueryParameter("sign",sign).build())
            }
        }
    }

}

/**
 * @author LDD
 * @Date   2018/8/13 上午10:28
 * @From   NetFectory
 * @Note   token管理器
 */
class TokenManager{

    /**
     * @Name  context
     * @Type  Context
     * @Note  上下文
     */
    private val context :Context

    /**
     * @author LDD
     * @From   TokenManager
     * @Date   2018/8/13 上午10:28
     * @Note   构造方法
     * @param  context 上下文
     */
    constructor(context: Context) {
        this.context = context
    }

    /**
     * 刷新TokenKey
     */
    private val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"

    /**
     * 权限TokenKey
     */
    private val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"

    /**
     * UID Key
     */
    private val UID_KEY = "UID_KEY"

    /**
     * @author LDD
     * @From   TokenManager
     * @Date   2018/8/13 上午10:29
     * @Note   获取刷新Token
     */
    fun getRefreshToken() : String?{
        return ACache.get(context).getAsString(REFRESH_TOKEN_KEY)
    }

    /**
     * @author LDD
     * @From   TokenManager
     * @Date   2018/8/13 上午10:29
     * @Note   获取权限Token
     */
    fun getAccessToken() :String?{
        return ACache.get(context).getAsString(ACCESS_TOKEN_KEY)
    }

    /**
     * 获取UID
     */
    fun getUid() :String ?{
        return ACache.get(context).getAsString(UID_KEY)
    }

    /**
     * 移除UID
     */
    fun removeUid(){
        ACache.get(context).remove(UID_KEY)
    }

    /**
     * 更新UID
     */
    fun updateUid(uid :Int){
        ACache.get(context).put(UID_KEY,uid.toString())
    }

    /**
     * @author LDD
     * @From   TokenManager
     * @Date   2018/8/13 上午10:29
     * @Note   清空Token
     */
    fun clearToken() {
        ACache.get(context).remove(REFRESH_TOKEN_KEY)
        ACache.get(context).remove(ACCESS_TOKEN_KEY)
    }

    /**
     * @author LDD
     * @From   TokenManager
     * @Date   2018/8/13 上午10:29
     * @Note   更新Token
     * @param  refreshToken 刷新Token
     * @param  accessToken  权限Token
     */
    fun updateToken(refreshToken : String , accessToken :String) {
        ACache.get(context).put(REFRESH_TOKEN_KEY,refreshToken)
        ACache.get(context).put(ACCESS_TOKEN_KEY,accessToken)
    }

}


