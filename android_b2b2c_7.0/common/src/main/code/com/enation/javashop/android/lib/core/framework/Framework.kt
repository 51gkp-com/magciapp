package com.enation.javashop.android.lib.core.framework

import android.app.Application
import com.enation.javashop.android.lib.core.runtime.ClassNotFoundInterceptor
import com.enation.javashop.android.lib.core.split.ComponentManager
import com.enation.javashop.android.lib.core.split.ComponentManagerImpl
import com.enation.javashop.android.lib.utils.Do
import java.io.File

/**
 * @author  LDD
 * @Data   2017/12/26 上午8:28
 * @From   com.enation.javashop.android.lib.core.framework
 * @Note   JavaShop FrameWork层入口
 */
 class Framework {

    /**
     * @author  LDD
     * @Data   2017/12/26 上午8:29
     * @From   com.enation.javashop.android.lib.core.framework Framework
     * @Note   Framework伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.lib.core.framework.Framework.Companion.classNotFoundInterceptor
         * @Type  com.enation.javashop.android.lib.core.runtime.ClassNotFoundInterceptor
         * @Note  Class拦截器在Class文件查找失败时执行
         */
        var classNotFoundInterceptor : ClassNotFoundInterceptor? = null

        /**
         * @author  LDD
         * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
         * @Data   2017/12/26 上午8:32
         * @Note   初始化Activity生命周期监听器
         * @param  app  应用Application对象
         */
        fun initActivityLifeController(app:Application){
            app.registerActivityLifecycleCallbacks(ActivityLifeController().withAutoValueHelper())
        }

        /**
         * @author  LDD
         * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
         * @Data   2017/12/26 上午8:35
         * @Note   Moudle安装
         * @param  pakeageName    模块包名
         * @param  moudleLocation 模块物理路径
         */
        fun installMoudle(pakeageName:String, moudleLocation:File){
            ComponentManager.get().installMoudle(pakeageName,moudleLocation)
        }

        /**
         * @author  LDD
         * @From   com.enation.javashop.android.lib.core.framework.Framework Companion
         * @Data   2017/12/26 上午8:36
         * @Note   Moudle卸载
         * @param  pakeageName 根据包名进行卸载
         */
        fun unstallMoudle(pakeageName:String){
            ComponentManager.get().unstallMoudle(pakeageName)
        }
    }
}
