package com.enation.javashop.android.lib.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDexApplication
import com.enation.javashop.android.lib.core.framework.Framework
import com.enation.javashop.android.lib.core.hack.AndroidHack
import com.enation.javashop.android.lib.core.runtime.ClassNotFoundInterceptor
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.utils.logger.LoggerFactory
import com.uuzuche.lib_zxing.activity.ZXingLibrary


/**
 * @author  LDD
 * @Data   2017/12/26 上午9:51
 * @From   com.enation.javashop.android.lib.base
 * @Note   Application基类
 */
open class BaseApplication : MultiDexApplication() {

    /**
     * 伴生对象
     */
    companion object {

        /**
         * @Name  appContext
         * @Type  android.support.multidex.MultiDexApplication()
         * @Note  应用Application对象（单例）
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Application

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseApplication
     * @Data   2017/12/26 上午10:06
     * @Note   Application创建时调用
     * @param  ...
     */
    override fun onCreate() {
        super.onCreate()
        closeAndroidPDialog()
        AppTool.SystemUI.initStatusBarHeight(applicationContext)
        /// 自动适配界面
//        AutoSizeConfig.getInstance()
//                .setCustomFragment(true)
//        AutoSize.initCompatMultiProcess(this)
        /**Logger初始化*/
        LoggerFactory.create(baseContext)
                     .diskCache()
                     .setTag("JavaShopLog")
                     .build()
        /**初始化Application对象*/
        appContext = this
        /**开始Activity生命周期监听，并启动AutoClearHelper*/
        Framework.initActivityLifeController(this)
        /**注入ClassNotFound异常拦截处理器*/
        Framework.classNotFoundInterceptor = object : ClassNotFoundInterceptor {
            override fun call(context: Context, intent: Intent?): Intent? {

                return intent
            }
        }
        /**对Instrumentation进行Hook替换成自定义Instrumentation*/
        AndroidHack.hookInstrumentation()

        /** 初始化Zxing */
        ZXingLibrary.initDisplayOpinion(this)

    }


    private fun closeAndroidPDialog() {
        try {
            val aClass = Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
            declaredConstructor.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}