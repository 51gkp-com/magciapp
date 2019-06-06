package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueBool
import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @From   com.enation.javashop.android.middleware.model
 * @Date   2018/3/9 上午10:24
 * @Note   商品列表Item
 */
data class GoodsItemViewModel(val goodsName : String ,                          /**商品名称*/
                              val goodsImage :String ,                          /**商品图片*/
                              val goodsPrice :Double ,                          /**商品价格*/
                              val goodsId : Int,                                /**商品id*/
                              val skuId:Int ,                                   /**商品默认skuId*/
                              val shopId : Int,                                 /**店铺ID*/
                              val commentNum : String,                          /**商品评论数*/
                              val feedbackRate :String,                         /**好评率*/
                              val promotionList :ArrayList<PromotionViewModel>, /**促销列表*/
                              val orginPrice : Double,                          /**原价*/
                              var priceColor:String = "#fbac42",                /**字体颜色 扩展字段*/
                              var countNum :Int = 0,                            /**商品总库存*/
                              var salesEnable :Boolean = false,                 /**促销是否开启*/
                              var buyNum  :Int = 0,                             /**购买个数 订单或售后模块用到*/
                              var canInquiry :Int = 0)                          /**是否询价*/{

    companion object {

        fun afterMap(json:JSONObject):GoodsItemViewModel{
            return GoodsItemViewModel(json.valueString("name"),
                    json.valueString("goods_image"),
                    json.valueDouble("purchase_price"),
                    json.valueInt("goods_id\t"),
                    json.valueInt("sku_id\t"),
                    json.valueInt("seller_id"),
                    "",
                    "",
                    ArrayList(),
                    json.valueDouble("original_price"),
                    countNum = json.valueInt("num"),
                    buyNum = json.valueInt("num"))
        }

        fun map(goods:CartGoodsItemViewModel) : GoodsItemViewModel{
            return  GoodsItemViewModel(goods.name,
                    goods.imageUrl,
                    goods.price,
                    goods.goodsId,
                    goods.skuId,
                    goods.shopId,
                    goods.currentNum+"件",
                    "",
                    ArrayList(),
                    goods.originalPrice)
        }

        fun map(jsonObject :JSONObject) : GoodsItemViewModel{
            return GoodsItemViewModel(jsonObject.valueString("goods_name"),
                    jsonObject.valueString("goods_img"),
                    jsonObject.valueDouble("goods_price"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.optInt("sku_id",0),
                    jsonObject.valueInt("shop_id"),
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("goods_price"))

        }

        fun pointMap(jsonObject :JSONObject) : GoodsItemViewModel{
            return GoodsItemViewModel(jsonObject.valueString("goods_name"),
                    jsonObject.valueString("goods_img"),
                    jsonObject.valueDouble("exchange_money"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.optInt("exchange_point",0),
                    jsonObject.valueInt("shop_id"),
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("goods_price"))

        }

        fun secMap(jsonObject :JSONObject) : GoodsItemViewModel{
            var result =  GoodsItemViewModel(jsonObject.valueString("goods_name"),
                    jsonObject.valueString("goods_image"),
                    jsonObject.valueDouble("seckill_price"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.optInt("sku_id",0),
                    jsonObject.valueInt("shop_id"),
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("original_price"))
            result.buyNum = jsonObject.valueInt("sold_num")
            result.countNum = jsonObject.valueInt("sold_quantity") + result.buyNum
            result.salesEnable = jsonObject.valueBool("is_start")
            return result
        }

        fun groupMap(jsonObject :JSONObject) : GoodsItemViewModel{
            return GoodsItemViewModel(jsonObject.valueString("goods_name"),
                    jsonObject.valueString("img_url"),
                    jsonObject.valueDouble("price"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.optInt("sku_id",0),
                    jsonObject.valueInt("seller_id"),
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("original_price"),
                    buyNum = jsonObject.valueInt("buy_num"))

        }

        fun goodssearchMap(jsonObject :JSONObject) : GoodsItemViewModel{
            return GoodsItemViewModel(
                    jsonObject.valueString("name"),
                    jsonObject.valueString("thumbnail"),
                    jsonObject.valueDouble("price"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.valueInt("sku_id"),
                    jsonObject.valueInt("seller_id"),
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("price"),
                    canInquiry = jsonObject.valueInt("can_inquiry"))

        }

        fun shopGoodsMap(jsonObject :JSONObject,sellerId: Int) : GoodsItemViewModel{
            return GoodsItemViewModel(
                    jsonObject.valueString("goods_name"),
                    jsonObject.valueString("thumbnail"),
                    jsonObject.valueDouble("price"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.optInt("sku_id",0),
                    sellerId,
                    "",
                    "",
                    ArrayList(),
                    jsonObject.valueDouble("price")
            )

        }


    }

}
