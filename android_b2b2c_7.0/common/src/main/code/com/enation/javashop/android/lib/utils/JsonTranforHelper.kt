package com.enation.javashop.android.lib.utils

import android.util.Log
import com.google.gson.Gson

/**
 * @author LDD
 * @Date   2018/5/8 上午11:51
 * @From   com.enation.javashop.android.middleware.router
 * @Note   Json转换辅助类
 */
object JsonTranforHelper {

    /**
     * @Name  com.enation.javashop.android.lib.utils.JsonTranforHelper.gson
     * @Type  Gson
     * @Note  静态Gson单例
     */
    @JvmStatic
    private val gson = Gson()

    /**
     * @author LDD
     * @From   JsonTranforHelper
     * @Date   2018/5/8 上午11:53
     * @Note   对象转Json
     * @param  instance 对象
     */
    fun toJson(instance: Any): String {
        return gson.toJson(instance)
    }

    /**
     * @author LDD
     * @From   JsonTranforHelper
     * @Date   2018/5/8 上午11:53
     * @Note   Json转对象
     * @param  json json字符
     * @param  cls  需要转换的对象的class
     */
    fun <T> toObject(json: String, cls: Class<T>): T {
        return gson.fromJson(json, cls)
    }

}