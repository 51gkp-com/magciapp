package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/4/23 上午9:23
 * @From   com.enation.javashop.android.middleware.model
 * @Note   售后信息ViewModel
 */
data class AfterSaleViewModel(var returnGoodsPrice :Double = 0.0,         /**单价 单个售后时使用*/
                              var returnGoodsCount :Int = 0,            /**商品数 单个售后时使用*/
                              var returnPoint :Int = 0,                 /**售后积分*/
                              var returnCountMoney: Double = 0.0,            /**订单总价 整单退是使用*/
                              var originalWay:String = "",               /**是否支持原路退回*/
                              var returnGoodsList :ArrayList<GoodsItemViewModel> = ArrayList())   /**售后商品数据*/