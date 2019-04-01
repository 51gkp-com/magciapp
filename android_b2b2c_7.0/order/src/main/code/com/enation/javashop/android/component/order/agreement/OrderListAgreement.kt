package com.enation.javashop.android.component.order.agreement

import com.enation.javashop.android.middleware.model.OrderPayModel

/**
 * Created by LDD on 2018/11/1.
 */
interface OrderListAgreement {

    fun comment(sn :String)

    fun cancel(sn :String)

    fun applyServerCancel(sn :String)

    fun express(sn :String)

    fun pay(data : OrderPayModel)

    fun rog(sn :String)

}