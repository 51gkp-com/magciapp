package com.enation.javashop.android.lib.core.framework

import android.content.Context

/**
 * @author LDD
 * @Date   2018/1/17 上午10:26
 * @From   com.enation.javashop.android.middleware.resource
 * @Note   加载未安装apk的资源文件
 */
class UnInstallResourceHelper {

    /**
     * @Name  resource
     * @Type  ResourceManager.LoadedResource
     * @Note  加载完成的资源
     */
    private lateinit var resource : ResourceManager.LoadedResource

    /**
     * @Name  packageName
     * @Type  String
     * @Note  资源包名
     */
    private lateinit var packageName : String

    /**
     * @author LDD
     * @Date   2018/1/17 上午10:23
     * @From   com.enation.javashop.android.middleware.resource
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.lib.core.framework.UnInstallResourceHelper.Companion.instance
         * @Type  com.enation.javashop.android.lib.core.framework.UnInstallResourceHelper
         * @Note  单例
         */
        private val instance by lazy { UnInstallResourceHelper() }

        /**
         * @author LDD
         * @From   Companion
         * @Date   2018/1/17 上午10:25
         * @Note   获取单例对象
         * @return 未安装资源
         */
        fun get(): UnInstallResourceHelper {
            return instance
        }
    }

    /**
     * @author LDD
     * @From   UnInstallResourceHelper
     * @Date   2018/1/17 上午10:22
     * @Note   初始化
     * @param  context 用户上下文
     * @param  path    外部资源路径
     */
    fun init(context: Context,path:String){
        ResourceManager.init(context)
        resource = ResourceManager.unInstalled().loadResource(path)
        packageName = resource.packageName
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取mipmap资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getMipmapId(name : String):Int{
        return ResourceManager.unInstalled().getResourceID(packageName,"mipmap",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取drawable资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getDrawableId(name : String):Int{
        return ResourceManager.unInstalled().getResourceID(packageName,"drawble",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取color资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getColorId(name : String):Int{

        return ResourceManager.unInstalled().getResourceID(packageName,"color",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取style资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getStyleId(name : String):Int{

        return ResourceManager.unInstalled().getResourceID(packageName,"style",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取string资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getStringId(name : String):Int{
        return ResourceManager.unInstalled().getResourceID(packageName,"string",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取anim资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getAnimId(name : String):Int{
        return ResourceManager.unInstalled().getResourceID(packageName,"anim",name)
    }
}