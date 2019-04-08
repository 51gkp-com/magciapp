package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField

/**
 * @author LDD
 * @Date   2018/4/17 下午5:22
 * @From   com.enation.javashop.android.middleware.model
 * @Note   会员地址信息
 */
data class MemberAddressViewModel(var id :Int,                   /**地址ID*/
                                  var name :String ,             /**收货人姓名*/
                                  var phoneNum :String ,         /**手机号*/
                                  var addressDetail :String,     /**地址详细*/
                                  var proId :Int ,               /**省份ID*/
                                  var proName:String,            /**省份名称*/
                                  var cityId :Int,               /**城市ID*/
                                  var cityName :String,          /**城市名称*/
                                  var counId :Int,               /**城镇ID*/
                                  var counName :String,          /**城镇名称*/
                                  var townId :Int?,              /**乡村ID*/
                                  var townName :String?,         /**乡村名称*/
                                  var tag  :String?,             /**标签*/
                                  var isDefault: Boolean = false)/**是否是默认地址*/{

    val nameObser = ObservableField(name)

    val addressInfoObser = ObservableField("$proName$cityName$counName$townName  $addressDetail")

    val addressRegionObser =  ObservableField("$proName$cityName$counName$townName")

    val phoneNumObser = ObservableField(if (phoneNum.isEmpty()){phoneNum}else{ phoneNum.replace(phoneNum.substring(3,7),"****") })

    val addressDetailObser = ObservableField(addressDetail)
}