package com.enation.javashop.android.component.extra.launch

import com.enation.javashop.android.component.extra.di.DaggerExtraComponent
import com.enation.javashop.android.component.extra.di.ExtraComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 *  扩展模块 启动入口
 */
@Router(path = "/extra/launch")
class ExtraLaunch : BaseLaunch(){

    companion object {
        lateinit var component : ExtraComponent
    }

    override fun moduleInit() {
        component = DaggerExtraComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }
}