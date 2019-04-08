package com.enation.javashop.android.component.order.agreement

/**
 * Created by LDD on 2018/10/24.
 */
interface OrderDetailAgreement {

    fun addCart(skuId :Int)

    fun toAfterSale(skuId :Int)

}