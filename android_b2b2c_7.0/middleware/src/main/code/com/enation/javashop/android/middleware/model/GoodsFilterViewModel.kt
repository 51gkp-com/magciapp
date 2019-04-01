package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField

/**
 * @author LDD
 * @Date   2018/3/9 上午11:22
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品筛选参数
 */
data class GoodsFilterViewModel(val filterName:String,                          /**筛选名称*/
                                var selectedFilterValue: GoodsFilterValue?,     /**选中的筛选值*/
                                val type :String,                               /**筛选类型*/
                                val valueList :ArrayList<GoodsFilterValue>)     /**筛选值列表*/{

    var selectedValueObser = ObservableField(selectedFilterValue)

    var openFlag = ObservableField(false)
}

/**
 * @author LDD
 * @Date   2018/3/9 上午11:23
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品筛选参数Value
 */
data class GoodsFilterValue(
                            val name :String,       /**参数名*/
                            val value :String,      /**参数值*/
                            var selected :Boolean,  /**是否选中*/
                            var letter:String? =null){/**首字母*/

    /**
     * @Name  selectedObservable
     * @Type  ObservableField<Boolean>
     * @Note  选中监听器
     */
    var selectedObservable =  ObservableField<Boolean>(selected)
}