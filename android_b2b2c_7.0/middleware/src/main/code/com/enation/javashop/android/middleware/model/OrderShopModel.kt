package com.enation.javashop.android.middleware.model

/**
 * Created by LDD on 2018/12/13.
 */
data class OrderShopModel(var cartGoods : ArrayList<CartGoodsItemViewModel>,
                          var coupon :ArrayList<CouponViewModel>,
                          var gift :ArrayList<Gift>,
                          var promotionTitle :String,
                          var shopName: String){
    constructor() : this(ArrayList(), ArrayList(),ArrayList(),"","")
}