package com.enation.javashop.android.middleware.config

import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.AppTool
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.umeng.socialize.sina.helper.MD5
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/18 上午11:15
 * @From   com.enation.javashop.android.middleware.config
 * @Note   JavaShop配置中心
 */
class JavaShopConfigCenter {

    /**
     * @Name  jsonObject
     * @Type  JSONObject
     * @Note  Json对象
     */
    val jsonObject :JSONObject = JSONObject(AppTool.File.readAssetsText(BaseApplication.appContext,"config.javashop"))

    /**
     *  是否处于开发模式
     */
    val APP_DEV = false

    /**
     * 基础URL
     */
    val WAP_BASE_URL = "http://m.51gkp.com/"

    /**
     * 商品页面URL
      */
    val WAP_GOODS_URL = "${WAP_BASE_URL}goods/"

    /**
     * 店铺页面URL
     */
    val WAP_SELLER_URL = "${WAP_BASE_URL}shop/"

    /**
     * WeChat appid 微信支付 微信分享时使用
     */
    val WECHAT_APP_ID = "wx35ab0feca89af616"

    /**
     * 微信Secret 微信分享 微信登录使用
     */
    val WECHAT_SCRECT = "632d25a392d1ca2e191b35fcbd015e28"

    /**
     * 微博Key 微博分享登录时使用
     */
    val WEIBO_KEY = "1068471919"

    /**
     * 微博AuthUrl，微博分享登录时使用
     */
    val WEIBO_URL = "http://www.51gkp.com/"

    /**
     * 微博 Secret，微博分享登录时使用
     */
    val WEIBO_SCRECT = "4f59b6f644412bd5c94d1696784e133b"

    /**
     * QQ screct QQ分享登录时使用
     */
    val QQ_SCRECT = "MEgRE0voAOxhH8OJ"

    /**
     * QQ ID     QQ分享登录时使用
     */
    val QQ_Key = "1105873778"

    /**
     * Alipay分享Id
     */
    val ALIPAY_KEY = "2017101909390405"

    /**
     * umeng推送Key
     */
    val UMENG_KEY = "5ca179db0cafb2285400045a"

    /**
     * umeng推送Secret
     */
    val UMENG_SCRECT = "635fa5657b5fbd9a7d51e5376767011e"

    /**
     * @author LDD
     * @Date   2018/5/18 上午11:18
     * @From   JavaShopConfigCenter
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.middleware.config.JavaShopConfigCenter.Companion.INSTANCE
         * @Type  JavaShopConfigCenter
         * @Note  配置中心单例
         */
        @JvmStatic val INSTANCE by lazy { JavaShopConfigCenter()}

    }

}