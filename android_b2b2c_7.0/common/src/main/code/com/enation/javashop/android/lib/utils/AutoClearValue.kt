package com.enation.javashop.android.lib.utils

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log

/**
 * @author  LDD
 * @Data   2017/12/21 下午5:53
 * @From   com.enation.javashop.android.lib.utils
 * @Note   自动清理变量
 */
class AutoClearValue<ValueType> {

    /**
     * @Name  value
     * @Type  ValueType
     * @Note  自动处理的V安略
     */
    private var value : ValueType? = null

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils AutoClearValue
     * @Data   2017/12/22 下午3:40
     * @Note   在Fragment中使用的构造方法构造方法
     * @param  fragment 所在的Fragment
     */
    constructor(fragment :Fragment , value: ValueType?) {
        fragment.fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks(){
            override fun onFragmentDestroyed(fm: FragmentManager?, f: Fragment?) {
                if (f == fragment) {
                    this@AutoClearValue.value = null
                    if (fm != null) {
                        fm.unregisterFragmentLifecycleCallbacks(this)
                    }
                }
            }
        },false)
        get()
        this.value = value
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils AutoClearValue
     * @Data   2017/12/22 下午3:41
     * @Note   在Activity中使用的构造方法
     * @param  activity 所在的Activity
     */
    constructor(activity: Activity,value:ValueType?){
        AutoClearHelper.intance.create(activity.localClassName,{
            this.value = null
        })

        this.value = value
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils AutoClearValue
     * @Data   2017/12/21 下午5:53
     * @Note   获取value
     */
    fun get() : ValueType?{
        return value
    }
}