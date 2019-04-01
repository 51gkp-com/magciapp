package com.enation.javashop.android.lib.jni

/**
 * @author  LDD
 * @Data   2017/12/22 下午5:29
 * @From   com.enation.javashop.android.lib.jni
 * @Note   JavaShop通用JNI入口
 */
class CommonJNI {
    init {
        System.loadLibrary("JavaShopCommonNDK")
    }

    external fun sayHello(): String
}