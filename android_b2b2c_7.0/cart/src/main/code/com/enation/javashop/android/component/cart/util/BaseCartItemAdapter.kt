package com.enation.javashop.android.component.cart.util

import android.support.v7.widget.RecyclerView
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter


/**
 * @author LDD
 * @Date   2018/2/6 下午5:06
 * @From   com.enation.javashop.android.component.cart.util
 * @Note   购物车Item基础适配器
 */
 abstract class BaseCartItemAdapter<VH : RecyclerView.ViewHolder,DataType>(private val cartAction : CartActionAgreement): BaseDelegateAdapter<VH, DataType>() {

    /**
     * @author LDD
     * @From   BaseCartItemAdapter
     * @Date   2018/2/9 下午2:02
     * @Note   购物车操作协议提供者
     * @return 购物车操作协议
     */
    fun actionProvider():CartActionAgreement{
        return cartAction
    }

}