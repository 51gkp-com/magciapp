package com.enation.javashop.android.component.home.launch

import com.enation.javashop.android.component.home.di.DaggerHomeComponent
import com.enation.javashop.android.component.home.di.HomeComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/1/22 下午2:44
 * @From   com.enation.javashop.android.component.home.launch
 * @Note   home模块启动类 代替Application 在壳工程Application中反射调用
 */
@Router(path = "/home/launch")
class HomeLaunch : BaseLaunch() {
    companion object {
        lateinit var component:HomeComponent
    }

    override fun moduleInit() {
        component = DaggerHomeComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }

}