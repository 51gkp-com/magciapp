package com.enation.javashop.android.component.welcome.launch

import com.enation.javashop.android.component.welcome.di.WelcomeComponent
import com.enation.javashop.android.component.welcome.di.DaggerWelcomeComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * Created by LDD on 2017/9/4.
 */
@Router(path = "/welcome/launch")
class WelcomeLaunch :BaseLaunch() {
    companion object {
        lateinit var component:WelcomeComponent
    }

    override fun moduleInit() {
        component = DaggerWelcomeComponent
                .builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }
}