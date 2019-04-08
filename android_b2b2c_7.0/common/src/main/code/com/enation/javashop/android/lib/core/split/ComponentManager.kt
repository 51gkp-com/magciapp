package com.enation.javashop.android.lib.core.split

import java.io.File

/**
 * @author  LDD
 * @Date   2018/1/15 下午4:10
 * @From   com.enation.javashop.android.lib.core.split
 * @Note   组件控制器协议
 */
interface ComponentManager {

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
     * @Data   2017/12/26 上午8:35
     * @Note   Moudle安装
     * @param  pakeageName    模块包名
     * @param  moudleLocation 模块绝对路径
     */
    fun installMoudle(pakeageName:String, moudleLocation: File)

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
     * @Data   2017/12/26 上午8:36
     * @Note   Moudle卸载
     * @param  pakeageName 根据包名进行卸载
     */
    fun unstallMoudle(pakeageName:String)


    /**
     * @author  LDD
     * @Date   2018/1/15 下午4:08
     * @From   ComponentManager
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.lib.core.split.ComponentManagerImpl.Companion.INSTANCE
         * @Type  com.enation.javashop.android.lib.core.split.ComponentManager
         * @Note  组件控制器单例
         */
        private val INSTANCE:ComponentManager by lazy { ComponentManagerImpl() }

        /**
         * @author LDD
         * @From   Companion
         * @Date   2018/1/15 下午4:22
         * @Note   获取单例
         * @return 单例
         */
        fun get() :ComponentManager{
            return INSTANCE
        }
    }
}