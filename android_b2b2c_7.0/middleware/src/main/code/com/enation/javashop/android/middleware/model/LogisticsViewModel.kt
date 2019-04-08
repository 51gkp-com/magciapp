package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/4/17 下午4:53
 * @From   com.enation.javashop.android.middleware.model
 * @Note   物流ViewModel
 */
data class LogisticsViewModel(val time :String ,        /**时间*/
                              val message :String ,     /**物流信息*/
                              val type :Int)            /**类型*/