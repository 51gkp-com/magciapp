package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField

/**
 * @author LDD
 * @Date   2018/3/27 上午11:15
 * @From   com.enation.javashop.android.middleware.model
 * @Note   优惠券VM
 */
data class CouponViewModel(val price : Double,          /**抵用的价格*/
                           val basePrice : Double,      /**最低使用金额*/
                           val shopName : String,       /**所属店铺*/
                           var isReceive : Boolean,     /**是否已经领取过*/
                           var isUseed :Boolean,        /**是否使用过*/
                           var isDateed :Boolean,       /**是否过期*/
                           val id : Int,                /**优惠券ID*/
                           var useDate :String,         /**使用时间区间*/
                           val shopId :Int ,            /**所属店铺ID*/
                           val describe:String,         /**额外描述*/
                           var isSelect: ObservableField<Int>  /**是否选中*/,
                           var isGet :Boolean)  /**是否为仅领取UI*/{

    constructor( price : Double,          /**抵用的价格*/
                 basePrice : Double,      /**最低使用金额*/
                 shopName : String,       /**所属店铺*/
                 isReceive : Boolean,     /**是否已经领取过*/
                 isUseed :Boolean,        /**是否使用过*/
                 isDateed :Boolean,       /**是否过期*/
                 id : Int,                /**优惠券ID*/
                 useDate :String,         /**使用时间区间*/
                 shopId :Int ,            /**所属店铺ID*/
                 describe:String         /**额外描述*/) : this(price,basePrice,shopName,isReceive,isUseed,isDateed,id,useDate,shopId,describe,ObservableField(-1 ),false)

    constructor(price: Double, basePrice: Double, shopName: String, isReceive: Boolean, isUseed: Boolean, isDateed: Boolean, id: Int, useDate: String, shopId: Int, describe: String, isGet: Boolean) : this(price,basePrice,shopName,isReceive,isUseed,isDateed,id,useDate,shopId,describe,ObservableField(-1 ),isGet)

    constructor(price: Double, basePrice: Double, shopName: String, isReceive: Boolean, isUseed: Boolean, isDateed: Boolean, id: Int, useDate: String, shopId: Int, describe: String, isSelect: ObservableField<Int>) : this(price,basePrice,shopName,isReceive,isUseed,isDateed,id,useDate,shopId,describe,isSelect,false)

}