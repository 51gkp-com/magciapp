package com.enation.javashop.android.middleware.model

data class OrderCreateModel(var price :OrderCreatePriceViewModel,
                            var params :OrderCreateParamsViewModel,
                            var goods :ArrayList<OrderShopModel>,
                            var address :MemberAddressViewModel?,
                            var shopId :ArrayList<Int>,
                            var coupons :ArrayList<CouponViewModel>) {
}