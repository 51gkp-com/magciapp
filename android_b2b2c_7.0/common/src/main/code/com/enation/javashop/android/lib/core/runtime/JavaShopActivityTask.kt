package com.enation.javashop.android.lib.core.runtime

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.ArrayList

 /**
  * @author  LDD
  * @Data   2017/12/26 上午9:00
  * @From   com.enation.javashop.android.lib.core.runtime
  * @Note   Activity栈
  */
 class JavaShopActivityTask {

     /**
      * @Name  activityList
      * @Type  java.util.ArrayList
      * @Note  Activity序列
      */
    private var activityList = ArrayList<WeakReference<Activity>>()

     /**
      * 伴生对象
      */
    companion object {
         /**
          * @Name  com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask.Companion.instance
          * @Type  com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
          * @Note  单例
          */
         val instance = JavaShopActivityTask()
    }

     /**
      * 构造方法私有化
      */
     private constructor()

     /**
      * @author  LDD
      * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
      * @Data   2017/12/26 上午9:08
      * @Note   获取当前应用Activity栈
      * @return 当前应用Activity栈
      */
    fun getActivityArray(): ArrayList<WeakReference<Activity>>{
        return activityList
    }

     /**
      * @author  LDD
      * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
      * @Data   2017/12/26 上午9:09
      * @Note   获取当前栈顶Activity
      * @return 当前栈顶Activity
      */
    fun peekTopActivity(): Activity? {
        if (activityList != null && activityList.size > 0) {
            val ref = activityList.get(activityList.size - 1)
            if (ref != null && ref!!.get() != null) {
                return ref!!.get()
            }
        }
        return null
    }

     /**
      * @author  LDD
      * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
      * @Data   2017/12/26 上午9:13
      * @Note   Activity创建时调用 添加activity到栈中
      * @param  activity 新创建的Activity
      */
    fun pushToActivityStack(activity: Activity) {
        activityList.add(WeakReference(activity))
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
     * @Data   2017/12/26 上午9:14
     * @Note   activity销毁时调用 在栈中移除销毁的Activity
     * @param  activity 销毁的Activity
     */
    fun popFromActivityStack(activity: Activity) {
        for (x in activityList.indices) {
            val ref = activityList.get(x)
            if (ref != null && ref!!.get() != null && ref!!.get() === activity) {
                activityList.remove(ref)
                return
            }
        }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
     * @Data   2017/12/26 上午9:16
     * @Note   清除当前Activity栈
     */
    fun clearActivityStack() {
        try {
            for (ref in activityList) {
                if (ref != null && ref!!.get() != null && !ref!!.get()!!.isFinishing()) {
                    ref!!.get()!!.finish()
                }
            }
        } catch (e: Throwable) {

        } finally {
            activityList.clear()
        }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
     * @Data   2017/12/26 上午9:17
     * @Note   判断当前Activity栈是否为空
     * @return Boolean 是否为空
     */
    fun isActivityStackEmpty(): Boolean {
        return sizeOfActivityStack() == 0
    }

     /**
      * @author  LDD
      * @From   com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
      * @Data   2017/12/26 上午9:18
      * @Note   获取当前Activity栈大小
      * @return 当前Activity栈大小
      */
    fun sizeOfActivityStack(): Int {
        return activityList.size
    }
}