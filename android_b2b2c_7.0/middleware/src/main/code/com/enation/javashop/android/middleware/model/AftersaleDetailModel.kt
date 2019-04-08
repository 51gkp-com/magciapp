package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * Created by LDD on 2018/10/16.
 */
class AftersaleDetailModel( var sn :String = "",

                            var time :String = "",

                            var state :String = "",

                            var type :String = "",

                            var reson :String = "",

                            var des :String = "",

                            var refundWay :String = "",

                            var price :Double = 0.0,

                            var point :Int = 0,

                            var goods : ArrayList<OrderDetailGoodsViewModel> = ArrayList()) {

    companion object {
        fun map(dic :JSONObject) :AftersaleDetailModel{
            var data = AftersaleDetailModel()
            val refund = dic.valueJsonObject( "refund")
            data.sn = refund.valueString( "sn")
            data.state = refund.valueString( "refund_status_text")
            data.type = refund.valueString( "refuse_type_text")
            data.price = refund.valueDouble( "refund_price")
            data.point = refund.valueInt( "refund_point")
            data.reson = refund.valueString( "refund_reason")
            data.refundWay = refund.valueString( "account_type_text")
            data.time = refund.valueDate( "create_time")
            data.des = refund.valueString( "customer_remark")
            dic.valueJsonArray( "refund_goods").arrayObjects().forEach { goodsDic ->
                var goodsModel = OrderDetailGoodsViewModel()
                goodsModel.goodsName = goodsDic.valueString( "goods_name")
                goodsModel.goodsImg = goodsDic.valueString( "goods_image")
                goodsModel.goodsId = goodsDic.valueInt( "goods_id")
                goodsModel.skuId = goodsDic.valueInt( "sku_id")
                goodsModel.goodsPrice = goodsDic.valueDouble( "price")
                goodsModel.sepc = "${goodsDic.valueInt( "return_num")}件"
                data.goods.add(goodsModel)
            }
            return data
        }
    }

}