package com.enation.javashop.android.lib.core.split

import java.io.File

/**
 * @author  LDD
 * @Date   2018/1/15 下午4:10
 * @From   com.enation.javashop.android.lib.core.split
 * @Note   组件控制器单例
 */
class ComponentManagerImpl : ComponentManager{


    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
     * @Date   2017/12/26 上午8:35
     * @Note   Moudle安装
     * @param  pakeageName    模块包名
     * @param  moudleLocation 模块绝对路径
     */
    override fun installMoudle(pakeageName:String, moudleLocation: File){

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
     * @Date   2017/12/26 上午8:36
     * @Note   Moudle卸载
     * @param  pakeageName 根据包名进行卸载
     */
    override fun unstallMoudle(pakeageName:String){

    }
}