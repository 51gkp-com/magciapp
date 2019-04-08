package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/3/27 下午1:55
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品规格
 */
data class GoodsSpecViewModel(val defaultSkuId : Int,           /**默认Skuid*/
                              val haveSpec : Boolean,           /**是否有规格*/
                              val skuList : ArrayList<SkuGoods>)/**sku商品列表*/