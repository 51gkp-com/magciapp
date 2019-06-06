package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/23 下午3:13
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品ViewModel
 */
data class GoodsViewModel(val name:String,              /**商品名*/
                          val goodsId:Int,              /**商品ID*/
                          val shopId:Int,               /**店铺ID*/
                          val weight :Double,
                          val skuId:Int,                /**默认SkuId*/
                          val allStock:Int,             /**全部存库*/
                          val skuStock:Int,             /**默认Sku库存*/
                          val allEnableStock:Int,       /**全部剩余库存*/
                          val skuEnableStock:Int,       /**默认Sku剩余库存*/
                          val goodsImage:String,        /**商品默认图片*/
                          val price :Double,            /**商品价格*/
                          val orgPrice:Double,          /**商品原价*/
                          var defaultSpec:String,       /**商品默认规格信息*/
                          var commentGrade  :Double,
                          var intro :String = "",
                          var params :ArrayList<GoodsParamParent> = ArrayList(),
                          var collect :Boolean = false,
                          var canInquiry :Int = 0)      /**是否询价*/{

    companion object {

        fun map(json :JSONObject) :GoodsViewModel{

            var params = ArrayList<GoodsParamParent>()
            json.valueJsonArray( "param_list").arrayObjects().forEach { item ->
                var paramParent = GoodsParamParent()
                var childs = ArrayList<GoodsParamChild>()
                paramParent.name = item.valueString( "group_name")
                item.valueJsonArray("params").arrayObjects().forEach({ childDic ->
                    var child = GoodsParamChild()
                    child.name = childDic.valueString( "param_name")
                    child.value = childDic.valueString( "param_value")
                    childs.add(child)
                })
                paramParent.child = childs
                params.add(paramParent)
            }


            return  GoodsViewModel(
                    json.valueString("goods_name"),
                    json.valueInt("goods_id"),
                    json.valueInt("seller_id"),
                    json.valueDouble("weight"),
                    0,
                    json.valueInt("quantity"),
                    0,
                    json.valueInt("enable_quantity"),
                    0,
                    json.valueString("thumbnail"),
                    json.valueDouble("price"),
                    json.valueDouble("price"),
                    "默认",
                    json.valueDouble("grade"),
                    intro = json.valueString("intro"),
                    params = params,
                    canInquiry = json.valueInt("can_inquiry")
                    )
        }

    }

}

/// 商品规格参数 父model
data class GoodsParamParent (
    var name :String = "",
    var child :ArrayList<GoodsParamChild> = ArrayList()
)

/// 商品规格参数 子model
data class GoodsParamChild (
    var name : String = "" ,
    var value : String = ""
)