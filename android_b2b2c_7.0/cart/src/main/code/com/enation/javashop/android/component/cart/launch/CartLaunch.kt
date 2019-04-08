package com.enation.javashop.android.component.cart.launch

import com.enation.javashop.android.component.cart.di.CartComponent
import com.enation.javashop.android.component.cart.di.DaggerCartComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/1/22 下午2:44
 * @From   com.enation.javashop.android.component.cart.launch
 * @Note   Cart模块启动类 代替Application 在壳工程Application中反射调用
 */
@Router(path = "/cart/launch")
class CartLaunch :BaseLaunch(){
    companion object {
        lateinit var component: CartComponent
    }

    override fun moduleInit() {
        component = DaggerCartComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }
}