package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField

/**
 * @author LDD
 * @Date   2018/4/17 下午4:21
 * @From   com.enation.javashop.android.middleware.model
 * @Note   订单详细ViewModel
 */
data class OrderDetailViewModel(var orderSn :String = "",/** 订单编号 */
                                var orderStatus : String = "",/** 订单状态 */
                                var lastExpressText :String = "",/** 最后的物流信息 */
                                var lastModify :String = "",/** 最后的物流更新时间 */
                                var consigneeName :String = "",/** 收货人姓名 */
                                var consigneeMobile :String = "",/** 收货人手机号 */
                                var consigneeAddress :String = "",/** 收货人地址 */
                                var sellerId :Int = 0,/** 商家ID */
                                var sellerName :String = "",/** 商家名称 */
                                var createTime :String = "",/** 订单创建时间 */
                                var payType : String = "",/** 付款方式 */
                                var logiName :String = "",/** 配送方式 */
                                var serviceStatus :String = "",/** 售后状态 */
                                var goodsPrice :Double = 0.0,/** 商品价格 */
                                var shipPrice :Double = 0.0,/** 运费 */
                                var discountPrice :Double = 0.0,/** 商品优惠 */
                                var pointPrice :Int = 0,
                                var couponPrice :Double = 0.0,
                                var needPayPrice :Double = 0.0,/** 实付款 */
                                var logId :Int = 0,
                                var shipNo :String = "",
                                var payTime: String = "",
                                var gifts : ArrayList<Gift> = ArrayList(),
                                var coupons : ArrayList<CouponViewModel>  = ArrayList(),
                                var receiptViewModel: ReceiptViewModel? = null,/** 发票信息 */
                                var orderActionModel: OrderActionModel? = null,/** 订单可操作允许情况 */
                                var goodsList: ArrayList<OrderDetailGoodsViewModel> = ArrayList())    /** 订单商品项 */{
    fun getPayName() :String{
        if (payType == "ONLINE"){
            return  "在线支付"
        }
        if (payType == "COD"){
            return  "货到付款"
        }
        return ""
    }
}


data class OrderDetailGoodsViewModel(var goodsName :String = "",          /** 商品名称 */
                                     var goodsPrice :Double = 0.0,         /** 商品成交价 */
                                     var serviceStatus :String = "",      /** 售后状态 */
                                     var goodsImg :String="",           /** 商品图片 */
                                     var sepc :String="",
                                     var goodsId :Int = 0,
                                     var skuId :Int = 0,
                                     var promotionTags :ArrayList<String> = ArrayList(),
                                     var goodsActionModel: GoodsActionModel = GoodsActionModel(false)   /** 商品可操作允许情况 */
                                     )

