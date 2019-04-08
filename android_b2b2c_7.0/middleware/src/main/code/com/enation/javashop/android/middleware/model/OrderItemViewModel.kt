package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/4/16 下午4:24
 * @From   com.enation.javashop.android.middleware.model
 * @Note   订单列表item视图模型
 */
data class OrderItemViewModel(val orderSn :String,                  /**订单号*/
                              val shopName:String,                  /**店铺名称*/
                              val shopId :Int,                      /**店铺ID*/
                              val orginPrice:Double,                /**订单原价*/
                              val payedPrice:Double,                /**订单实付款*/
                              val orderStateText :String,           /**订单状态文字*/
                              val orderState :Int,                  /**订单状态标识*/
                              val orderAction :OrderActionModel,
                              val goodsList:ArrayList<OrderItemGoodsViewModel>, /**订单中的商品*/
                              val isComment :Boolean = false)       /**是否在评论中心中*/

/**
 * @author LDD
 * @Date   2018/4/16 下午4:32
 * @From   com.enation.javashop.android.middleware.model
 * @Note   订单列表商品视图模型
 */
data class OrderItemGoodsViewModel(val goodsName :String,   /**商品名*/
                                   val goodsImage :String)  /**商品图片*/