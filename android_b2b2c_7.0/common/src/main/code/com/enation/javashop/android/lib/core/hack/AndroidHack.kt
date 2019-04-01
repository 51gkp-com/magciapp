package com.enation.javashop.android.lib.core.hack

import android.app.Instrumentation
import android.util.Log
import com.enation.javashop.android.lib.core.framework.JavaShopInstrumentationHook

/**
 * @author  LDD
 * @Data   2017/12/26 上午8:49
 * @From   com.enation.javashop.android.lib.core.hack
 * @Note   JavaShop Hack入口
 */
class AndroidHack {

    /**
     * 伴生对象
     */
    companion object {

            /**
             * @author  LDD
             * @From   com.enation.javashop.android.lib.core.hack.AndroidHack Companion
             * @Data   2017/12/26 上午8:50
             * @Note   替换系统 hookInstrumentation 为null时自动进行JavaShopInstrumentationHook
             * @param  instrumentationHook 需要进行hook的自定义Instrumentation
             */
            fun hookInstrumentation(instrumentationHook: Instrumentation?){

                try {
                    val mMainThreadClass = Class.forName("android.app.ActivityThread")

                    //获取主线程
                    val getMainThread = mMainThreadClass.getDeclaredMethod("currentActivityThread")
                    //私有属性 设置访问权限
                    getMainThread.isAccessible = true
                    val currentActivityThread = getMainThread.invoke(null)

                    // 获取mInstrumentation对象
                    val mInstrumentationField = mMainThreadClass.getDeclaredField("mInstrumentation")
                    //私有属性 设置访问权限
                    mInstrumentationField.isAccessible = true
                    //获取系统Instrumentation
                    val instrumentation = mInstrumentationField.get(currentActivityThread) as Instrumentation
                    // Hook Instrumentation 替换成我们自定义Instrumentation
                    if (instrumentationHook==null){
                        mInstrumentationField.set(currentActivityThread, JavaShopInstrumentationHook(instrumentation))
                    }else{
                        mInstrumentationField.set(currentActivityThread,instrumentationHook)
                    }

                    Log.i("InstrumentationHook", "JavaShop hook instrumentation success!")
                } catch (ex: Exception) {
                    Log.e("InstrumentationHook", "JavaShop hook instrumentation failed! [" + ex.message + "]")
                }

            }

            /**
             * @author  LDD
             * @From   com.enation.javashop.android.lib.core.hack.AndroidHack Companion
             * @Data   2017/12/26 上午8:53
             * @Note   执行1参hookInstrumentation方法 直接使用JavaShopInstrumentationHook
             */
            fun hookInstrumentation(){
                hookInstrumentation(null)
            }
        }
}