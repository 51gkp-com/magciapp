package com.enation.javashop.android.lib.core.framework

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
import com.enation.javashop.android.lib.utils.AutoClearHelper

/**
 * @author  LDD
 * @Data   2017/12/26 上午7:45
 * @From   com.enation.javashop.android.lib.core.framework
 * @Note   Activity生命周期回调
 */
class ActivityLifeController :Application.ActivityLifecycleCallbacks {

    /**
     * @Name  autoClearHelper
     * @Type  com.enation.javashop.android.lib.utils.AutoClearHelper
     * @Note  变量清理辅助类
     */
    private var isOpenAutoClear = false

    /**
     * @author  LDD
     * @From    ActivityLifeController
     * @Data   2017/12/26 上午7:48
     * @Note   开启
     */
    fun withAutoValueHelper():ActivityLifeController{
        isOpenAutoClear = true
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework ActivityLifeController
     * @Data   2017/12/26 上午8:22
     * @Note   Activity处于不显示或者半透明状态
     * @return Activity
     */
    override fun onActivityPaused(p0: Activity?) {

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework ActivityLifeController
     * @Data   2017/12/26 上午8:22
     * @Note   Activity处于完全显示状态
     * @return Activity
     */
    override fun onActivityResumed(p0: Activity?) {

    }

    override fun onActivityStarted(p0: Activity?) {

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework ActivityLifeController
     * @Data   2017/12/26 上午8:20
     * @Note   Activity销毁方法
     * @return 销毁的Activity
     */
    override fun onActivityDestroyed(p0: Activity?) {
        if (p0 != null && isOpenAutoClear) {
            AutoClearHelper.intance.destory(p0.localClassName)
        }
        if (p0 != null) {
            JavaShopActivityTask.instance.popFromActivityStack(p0)
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {

    }

    override fun onActivityStopped(p0: Activity?) {

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework ActivityLifeController
     * @Data   2017/12/26 上午8:25
     * @Note   Activity创建回调
     * @param  p0 创建的Activity
     */
    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        if (p0 != null) {
            JavaShopActivityTask.instance.pushToActivityStack(p0)
        }
    }
}