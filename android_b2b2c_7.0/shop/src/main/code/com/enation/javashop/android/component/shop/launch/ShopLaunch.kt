package com.enation.javashop.android.component.shop.launch

import com.enation.javashop.android.component.shop.di.ShopComponent
import com.enation.javashop.android.component.shop.di.DaggerShopComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/4/8 下午2:04
 * @From   com.enation.javashop.android.component.shop.launch
 * @Note   商品模块启动入口
 */
@Router(path = "/shop/launch")
class ShopLaunch : BaseLaunch() {

    companion object {
        lateinit var component :ShopComponent
    }

    /**
     * @author LDD
     * @From   ShopLaunch
     * @Date   2018/4/8 下午2:05
     * @Note   模块初始化
     */
    override fun moduleInit() {
        component = DaggerShopComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }

}