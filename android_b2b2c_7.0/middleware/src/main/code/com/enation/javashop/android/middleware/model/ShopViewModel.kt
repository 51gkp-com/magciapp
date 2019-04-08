package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueDate
import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/27 下午3:15
 * @From   com.enation.javashop.android.middleware.model
 * @Note   店铺详情ViewModel
 */
data class ShopViewModel(val description:String,        /**店铺描述*/
                         var favorited:Boolean,         /**是否已关注*/
var goodsNum:Int,              /**商品总数量*/
var hotNum:Int,                /**热卖商品总数*/
var new_Num:Int,               /**上新商品总数*/
var recommendNum :Int,         /**推荐商品总数*/
                         val banner :String,            /**banner图*/
                         var collectNum : Int,          /**关注人数*/
                         val credit :Double,               /**店铺总评分*/
                         val deliveryCredit:Double,        /**物流评分*/
                         val descCredit :Double,           /**描述评分*/
                         val serviceCredit:Double,         /**服务态度*/
                         val id:Int,                    /**店铺ID*/
                         val level:Int,                 /**店铺等级*/
                         val name:String,               /**店铺名*/
                         var openTime :String,          /**开店时间*/
                         var logo:String,
                         val selfOperated:Boolean=false      /**是否平台自营**/
)               /**店铺Logo*/{

    fun collectNumString() :String{
        return "${collectNum}人"
    }

    companion object {
        fun map(jsonObject : JSONObject) : ShopViewModel{
            val result = ShopViewModel(jsonObject.valueString("shop_desc"),
                    false,
                    jsonObject.valueInt("goods_num"),
                    0,
                    0,
                    0,
                    jsonObject.valueString("shop_logo"),
                    jsonObject.valueInt("shop_collect"),
                    jsonObject.valueDouble("shop_praise_rate"),
                    jsonObject.valueDouble("shop_delivery_credit"),
                    jsonObject.valueDouble("shop_description_credit"),
                    jsonObject.valueDouble("shop_service_credit"),
                    jsonObject.valueInt("shop_id"),
                    0,
                    jsonObject.valueString("shop_name"),
                    jsonObject.valueDate("shop_createtime"),
                    jsonObject.valueString("shop_logo"),
                    jsonObject.valueInt("self_operated")==1
                    )
            if (result.logo == ""){
                result.logo = jsonObject.valueString("logo")
            }
            return result
        }
    }
}