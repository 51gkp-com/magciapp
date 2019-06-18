package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/1/22 下午12:13
 * @From   com.enation.javashop.android.middleware.model
 * @Note   推荐商品model
 */
data class RecommendGoodsViewModel(var id :Int ,        /**商品id*/
                                   var name:String ,    /**商品名称*/
                                   var image:String ,   /**商品图片*/
                                   var price:Double,
                                   var canInquiry :Int = 0)    /**商品价格*/
{
    companion object {
        fun map(jsonObject: JSONObject) : RecommendGoodsViewModel{
            var goods =  RecommendGoodsViewModel(jsonObject.valueInt("goods_id"),
                    jsonObject.valueString("goods_name"),
                    jsonObject.valueString("goods_image"),
                    jsonObject.valueDouble("goods_price"),
                    jsonObject.valueInt("can_inquiry"))

            if (goods.image == ""){
                goods.image = jsonObject.valueString("thumbnail")
            }
            if (goods.name == ""){
                goods.name = jsonObject.valueString("name")
            }
            if (goods.price == -1.0){
                goods.price = jsonObject.valueDouble("price")
            }
            if (goods.id == 0){
                goods.id = jsonObject.valueInt("goods_id")
            }
            if (goods.canInquiry == 0){
                goods.canInquiry = jsonObject.valueInt("can_inquiry")
            }
            return goods
        }
    }
}