package com.enation.javashop.android.middleware.router

import android.content.Context
import android.util.Log
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.jrouter.logic.service.JsonTransforService
import com.enation.javashop.android.lib.utils.JsonTranforHelper
import com.enation.javashop.android.lib.utils.errorLog

/**
 * @author LDD
 * @Date   2018/5/8 上午11:49
 * @From   com.enation.javashop.android.middleware.router
 * @Note   路由传参对象解析器
 */
@Router(path = "/javashop/plugin/json")
class RouterJsonEngine  : JsonTransforService{


    /**
     * @author LDD
     * @From   RouterJsonEngine
     * @Date   2018/5/8 上午11:59
     * @Note   初始化操作
     * @param  context 上下文
     */
    override fun init(context: Context?) {

    }

    /**
     * @author LDD
     * @From   RouterJsonEngine
     * @Date   2018/5/8 上午11:56
     * @Note   json转对象
     * @param  clazz class对象
     * @param  json json字符串
     */
    override fun <T : Any> json2Object(json: String, clazz: Class<T>): T {
        return JsonTranforHelper.toObject(json,clazz)
    }

    /**
     * @author LDD
     * @From   RouterJsonEngine
     * @Date   2018/5/8 上午11:59
     * @Note   对象转json
     * @param  instance 对象
     */
    override fun object2Json(instance: Any): String {
        return JsonTranforHelper.toJson(instance)
    }
}