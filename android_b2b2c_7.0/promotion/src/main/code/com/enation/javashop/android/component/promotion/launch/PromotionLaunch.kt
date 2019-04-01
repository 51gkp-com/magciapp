package com.enation.javashop.android.component.promotion.launch

import com.enation.javashop.android.component.promotion.di.PromotionComponent
import com.enation.javashop.android.component.promotion.di.DaggerPromotionComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/5/21 下午4:26
 * @From   com.enation.javashop.android.component.promotion.launch
 * @Note   促销模块启动入口
 */
@Router(path = "/promotion/launch")
class PromotionLaunch : BaseLaunch() {

    /**伴生对象*/
    companion object {
        lateinit var component: PromotionComponent
    }

    override fun moduleInit() {
        component = DaggerPromotionComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }

}