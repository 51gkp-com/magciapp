package com.enation.javashop.android.component.setting.launch

import com.enation.javashop.android.component.setting.di.SettingComponent
import com.enation.javashop.android.component.setting.di.DaggerSettingComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager
/**
 * @author LDD
 * @Date   2018/3/6 下午2:13
 * @From   com.enation.javashop.android.component.setting.launch
 * @Note   设置模块初始化入口
 */
@Router(path = "/setting/launch")
class SettingLaunch : BaseLaunch() {

    companion object {
        lateinit var component: SettingComponent
    }

    /**
     * @author LDD
     * @From   SettingLaunch
     * @Date   2018/3/9 上午9:40
     * @Note   初始化回调
     */
    override fun moduleInit() {
        component = DaggerSettingComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }
}