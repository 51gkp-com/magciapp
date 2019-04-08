package com.enation.javashop.android.component.order.launch

import android.support.annotation.IntegerRes
import com.enation.javashop.android.component.order.di.OrderComponent
import com.enation.javashop.android.component.order.di.DaggerOrderComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.lib.utils.ReflexHelper
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.middleware.di.DaggerManager
import com.enation.javashop.android.middleware.model.OrderItemViewModel

/**
 * @author LDD
 * @Date   2018/4/16 下午2:46
 * @From   com.enation.javashop.android.component.order.launch
 * @Note   订单模块启动类
 */
@Router(path = "/order/launch")
class OrderLaunch : BaseLaunch() {

    /**
     * @author LDD
     * @Date   2018/4/16 下午2:48
     * @From   OrderLaunch
     * @Note   伴生对象
     */
    companion object {
        lateinit var component: OrderComponent
    }

    /**
     * @author LDD
     * @From   OrderLaunch
     * @Date   2018/4/16 下午2:48
     * @Note   模块初始化方法
     */
    override fun moduleInit() {
        component = DaggerOrderComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }

}