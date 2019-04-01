package com.enation.javashop.android.lib.base

import android.content.Context
import com.enation.javashop.android.jrouter.logic.template.BaseProvider

/**
 * @author  LDD
 * @Data   2017/12/26 上午11:59
 * @From   com.enation.javashop.android.lib.base
 * @Note   模块初始化类
 */
abstract class BaseLaunch :BaseProvider {

    /**
     * @Name  context
     * @Type  android.content.Context
     * @Note  上下文
     */
    protected var context :Context? = null

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseLaunch
     * @Data   2017/12/26 下午12:02
     * @Note   执行初始化操作
     * @param  context 上下文
     */
    override fun init(context: Context) {
        this.context = context
        moduleInit()
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseLaunch
     * @Data   2017/12/26 下午12:03
     * @Note   子类初始化操作
     */
    abstract fun moduleInit()

}