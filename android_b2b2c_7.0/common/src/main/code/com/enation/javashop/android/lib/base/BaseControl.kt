package com.enation.javashop.android.lib.base

/**
 * @author LDD
 * @Date   2018/3/29 上午11:32
 * @From   com.enation.javashop.android.lib.base
 * @Note   基础控制器
 */
interface BaseControl {

    /**
     * @author LDD
     * @From   BaseControl
     * @Date   2018/3/29 上午11:33
     * @Note   添加生命周期监听
     * @param  listener 监听
     */
    fun addLifeCycleListener(listener :((state :Int) ->Unit))

}

/**创建*/
const val LIFE_CYCLE_CREATE = 1

/**恢复*/
const val LIFE_CYCLE_RESUME = 2

/**暂停*/
const val LIFE_CYCLE_PAUSE = 3

/**销毁*/
const val LIFE_CYCLE_DESTORY = 4