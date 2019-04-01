package com.enation.javashop.android.component.order.agreement

import com.enation.javashop.android.middleware.model.AccountInfo

/**
 * Created by LDD on 2018/10/18.
 */
interface AfterSaleAgreement {

    fun serviceSelect(type :Int)

    fun payInfo(data :AccountInfo)

    fun reson(reson :String)

    fun comfrim()

}