package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.middleware.config.JavaShopConfigCenter

/**
 * @author LDD
 * @Date   2018/8/21 下午4:38
 * @From   com.enation.javashop.android.middleware.api
 * @Note   Url控制器接口
 */
interface UrlManager {

    /// host地址
    val base  : String

    /// 买家
    val buyer : String

    /// 基础Api
    val basic : String

    /// 其他
    val api : String

    /**
     * @author LDD
     * @Date   2018/8/21 下午4:46
     * @From   UrlManager
     * @Note   伴生对象
     */
    companion object {

        /**
         * @author LDD
         * @From   UrlManager
         * @Date   2018/8/21 下午4:47
         * @Note   构架Url控制器
         */
        fun build() : UrlManager{
            return if (JavaShopConfigCenter.INSTANCE.APP_DEV){
                return DevUrlManager()
            }else{
                return ProUrlManager()
            }
        }

    }

}

/**
 * @author LDD
 * @Date   2018/8/21 下午4:38
 * @From   com.enation.javashop.android.middleware.api
 * @Note   开发模式Url控制器
 */
private class DevUrlManager : UrlManager{

    override val base: String
        get() = "http://javashop7.s1.natapp.cc/"

    override val buyer: String
        get() = "${base}buyer-api/"

    override val basic: String
        get() = "${base}base-api/"

    override val api: String
        get() = ""
}

/**
 * @author LDD
 * @Date   2018/8/21 下午4:38
 * @From   com.enation.javashop.android.middleware.api
 * @Note   生产模式Url控制器
 */
private class ProUrlManager : UrlManager{

    override val base: String
        get() = "http://%s.51gkp.com/"

    override val buyer: String
        get() = String.format(base,"buyerapi")

    override val basic: String
        get() = String.format(base,"baseapi")

    override val api: String
        get() = String.format(base, "api")
}