package com.enation.app.javashop.application

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.enation.app.javashop.R
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.middleware.api.MemberState.Companion.manager
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.connectview.UmengConfig
import com.enation.javashop.net.engine.config.NetEngineConfig
import com.enation.javashop.net.engine.plugin.exception.RestfulExceptionInterceptor
import com.enation.javashop.utils.base.config.BaseConfig
import com.enation.javashop.utils.base.tool.CommonTool
import com.google.gson.Gson
import com.squareup.leakcanary.LeakCanary
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.MsgConstant
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

/**
 * @author LDD
 * @Date   2018/1/11 下午12:48
 * @From   com.enation.javashop.android.application
 * @Note   应用Application
 */
class Application : BaseApplication() {


    /**
     * @author LDD
     * @From   Application
     * @Date   2018/1/11 下午12:50
     * @Note   应用启动时调用
     */
    override fun onCreate() {
        super.onCreate()
        initRouter()
        initFrame()
        initLeaks()
        initPush()
    }

    /**
     * @author  LDD
     * @From    Application
     * @Date   2018/1/11 下午12:50
     * @Note   初始化路由
     * @return rx观察者
     */
    private fun initRouter() {
        JRouter.init(this)
        JRouter.openDebug()
        JRouter.openLog()
        JRouter.prepare().create("/welcome/launch").seek()
        JRouter.prepare().create("/home/launch").seek()
        JRouter.prepare().create("/member/launch").seek()
        JRouter.prepare().create("/cart/launch").seek()
        JRouter.prepare().create("/setting/launch").seek()
        JRouter.prepare().create("/goods/launch").seek()
        JRouter.prepare().create("/shop/launch").seek()
        JRouter.prepare().create("/order/launch").seek()
        JRouter.prepare().create("/promotion/launch").seek()
        JRouter.prepare().create("/extra/launch").seek()
    }

    /**
     * @author  LDD
     * @From    Application
     * @Date   2018/1/11 下午12:50
     * @Note   初始化内存检测器
     * @return rx观察者
     */
    private fun initLeaks() {
//        if (JavaShopConfigCenter.INSTANCE.APP_DEV) {
//            LeakCanary.install(this)
//        }
    }

    /**
     * @author  LDD
     * @From    Application
     * @Date   2018/1/11 下午12:50
     * @Note   初始化内部框架
     * @return rx观察者
     */
    private fun initFrame() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            BaseConfig.getInstance().addActivity("WelcomeActivity", "HomeActivity")
        } else {
            BaseConfig.getInstance().closeScrollBack()
        }
        NetEngineConfig.init(baseContext)
                .openLogger()
                .addNetInterceptor(RestfulExceptionInterceptor())
        /**初始化友盟分享*/
        //Config.DEBUG = true
        UmengConfig.initWechat(JavaShopConfigCenter.INSTANCE.WECHAT_APP_ID, JavaShopConfigCenter.INSTANCE.WECHAT_SCRECT)
        UmengConfig.initQQ(JavaShopConfigCenter.INSTANCE.QQ_Key, JavaShopConfigCenter.INSTANCE.QQ_SCRECT)
        UmengConfig.initWeiBo(JavaShopConfigCenter.INSTANCE.WEIBO_KEY, JavaShopConfigCenter.INSTANCE.WEIBO_SCRECT, JavaShopConfigCenter.INSTANCE.WEIBO_URL)
        UmengConfig.initAliPay(JavaShopConfigCenter.INSTANCE.ALIPAY_KEY)
    }

    private fun initPush() {
        UMConfigure.init(this, JavaShopConfigCenter.INSTANCE.UMENG_KEY, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, JavaShopConfigCenter.INSTANCE.UMENG_SCRECT)
        UMConfigure.setLogEnabled(true)
        /**初始化推送对象*/
        val mPushAgent = PushAgent.getInstance(this)
        /**开启推送*/
        mPushAgent.onAppStart()
        /**设置后台接受通知*/
        mPushAgent.setNotificaitonOnForeground(true)
        /**设置响铃*/
        mPushAgent.notificationPlaySound = MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE
        /**设置呼吸灯闪烁*/
        mPushAgent.notificationPlayLights = MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE
        /**设置是否显示*/
        mPushAgent.notificationPlayVibrate = MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE
        /**设置推送信息监听器*/
        mPushAgent.messageHandler = object : UmengMessageHandler() {

            override fun getNotification(context: Context?, uMessage: UMessage?): Notification {
                //Log.e("getNotification", Gson().toJson(uMessage))
                val builder = Notification.Builder(context)
                /**自定义通知样式*/
                val myNotificationView = RemoteViews(context!!.getPackageName(), R.layout.notification_lay)
                myNotificationView.setTextViewText(R.id.notification_time, CommonTool.DataMilltoString(System.currentTimeMillis(), "YYYY-MM-DD"))
                myNotificationView.setTextViewText(R.id.notification_content, uMessage!!.text)
                builder.setContent(myNotificationView)
                        .setSmallIcon(getSmallIconId(context, uMessage))
                        .setTicker(uMessage!!.ticker)
                        .setAutoCancel(true)
                return builder.notification
                return super.getNotification(context, uMessage)
            }

            override fun dealWithNotificationMessage(p0: Context?, p1: UMessage?) {
                super.dealWithNotificationMessage(p0, p1)
            }


        }

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {

            override fun onSuccess(deviceToken: String) {
                //注册成功会返回device token
                errorLog("PushRegisterSuccess", "$deviceToken")
            }

            override fun onFailure(s: String, s1: String) {
                errorLog("PushRegisterError", "$s  $s1")
            }
        })
    }

}