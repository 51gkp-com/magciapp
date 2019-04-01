package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/4/9 下午5:13
 * @From   com.enation.javashop.android.middleware.model
 * @Note   店铺首页数据模型
 */
data class ShopFirstViewModel(val recommendGoods:ArrayList<GoodsItemViewModel>, /**推荐商品*/
                              val newGoods :ArrayList<GoodsItemViewModel>,      /**上新*/
                              val hotGoods :ArrayList<GoodsItemViewModel>)      /**热卖*/