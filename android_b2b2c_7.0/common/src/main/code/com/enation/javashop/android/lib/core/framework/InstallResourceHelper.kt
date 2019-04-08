package com.enation.javashop.android.lib.core.framework

import android.content.Context


/**
 * @author LDD
 * @Date   2018/1/17 上午10:15
 * @From   com.enation.javashop.android.middleware.resource
 * @Note   加载已安装资源
 */
class InstallResourceHelper {

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:15
     * @Note   私有构造方法 防止外部实例化
     */
    private constructor()

    /**
     * @Name  install
     * @Type  ResourceManager.Installed
     * @Note  已安装资源
     */
    private lateinit var install :ResourceManager.Installed

    /**
     * @Name  CurrentPackageName
     * @Type  String
     * @Note  资源包名
     */
    private lateinit var CurrentPackageName : String

    /**
     * @author LDD
     * @Date   2018/1/17 上午10:16
     * @From   InstallResourceHelper
     * @Note   伴生对象 实现单例
     */
    companion object {
        /**
         * @Name  com.enation.javashop.android.lib.core.framework.InstallResourceHelper.Companion.instance
         * @Type  com.enation.javashop.android.lib.core.framework.InstallResourceHelper
         * @Note  单例对象 懒加载
         */
        private val instance by lazy { InstallResourceHelper() }

        /**
         * @author LDD
         * @From   Companion
         * @Date   2018/1/17 上午10:17
         * @Note   获取单例对象
         * @return 已安装文件资源
         */
        fun get(): InstallResourceHelper {
            return instance
        }
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:18
     * @Note   初始化
     * @param  context 上下文 一般为Application
     * @param  packageName 需要加在资源的包名
     */
    fun initInstalled(context :Context,packageName:String?){
        CurrentPackageName = if (packageName == null) {context.applicationContext.packageName} else {packageName}
        ResourceManager.init(context)
        install = ResourceManager.installed()
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:19
     * @Note   初始化 该方法一般用于加载本地资源 用于资源获取出错时使用
     * @param  context 上下文 一般为Application
     */
    fun initInstalled(context: Context){
        initInstalled(context,null)
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
        return install.getResourceID(CurrentPackageName,"mipmap",name)
    }

    /**
     * @author LDD
     * @From   InstallResourceHelper
     * @Date   2018/1/17 上午10:20
     * @Note   获取drawble资源ID
     * @param  name 资源名
     * @return 资源ID
     */
    fun getDrawableId(name : String):Int{
        return install.getResourceID(CurrentPackageName,"drawble",name)
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

        return install.getResourceID(CurrentPackageName,"color",name)
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

        return install.getResourceID(CurrentPackageName,"style",name)
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
        return install.getResourceID(CurrentPackageName,"string",name)
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
        return install.getResourceID(CurrentPackageName,"anim",name)
    }

}