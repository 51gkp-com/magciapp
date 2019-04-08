package com.enation.javashop.android.component.shop.agreement

import com.enation.javashop.android.middleware.model.ShopCategoryViewModel

/**
 * @author LDD
 * @Date   2018/4/12 上午11:45
 * @From   com.enation.javashop.android.component.shop.agreement
 * @Note   fragment回调协议
 */
interface ShopCategoryAgreement {

    /**
     * @author LDD
     * @From   ShopCategoryAgreement
     * @Date   2018/4/12 上午11:47
     * @Note   切换Fragment
     * @param  data 数据
     */
    fun pushFragment(data :ArrayList<ShopCategoryViewModel>)

    /**
     * @author LDD
     * @From   ShopCategoryAgreement
     * @Date   2018/4/12 上午11:48
     * @Note   切换Activity
     * @param  data 数据
     */
    fun pushActivity(data :ShopCategoryViewModel)

}