package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/15 下午4:29
 * @From   com.enation.javashop.android.middleware.model
 * @Note   购物车商品ViewModel
 */
data class CartGoodsItemViewModel(var isCheck: Boolean,      /**商品是否选择*/
                                  var imageUrl: String,      /**商品图片URL*/
                                  var price: Double,         /**商品介个*/
                                  var originalPrice :Double, /**商品原价*/
                                  var name: String,          /**商品名称*/
                                  var spec: String,          /**商品规格*/
                                  var maxNum: Int,           /**商品Sku最大库存*/
                                  var currentNum: String,    /**商品当前购买数量*/
                                  var goodsId: Int,          /**商品ID*/
                                  var shopId: Int,           /**店铺ID*/
                                  var skuId:Int,             /**商品skuID*/
                                  var bottomVisible:Boolean, /**商品底部是否显示*/
                                  var promotionList: List<SingglePromotionViewModel>?, /**促销列表*/
                                  var groupPromotionId :Int,
                                  var errorMessage : String) {

    companion object {
        fun map(json : JSONObject):CartGoodsItemViewModel{
            var result = CartGoodsItemViewModel(json.valueInt("checked") == 1,
                    json.valueString("goods_image"),
                    json.valueDouble("purchase_price"),
                    json.valueDouble("original_price"),
                    json.valueString("name"),
                    "",
                    json.valueInt("enable_quantity"),
                    json.valueInt("num").toString(),
                    json.valueInt("goods_id"),
                    json.valueInt("seller_id"),
                    json.valueInt("sku_id"),
                    false,
                    null,-1,json.valueString("error_message"))

            var spec  = ArrayList<Spec>()
            json.valueJsonArray( "spec_list").arrayObjects().forEach { it ->

                spec.add(Spec.map(it))

            }
            var str :String = ""
            spec.forEach { spec ->
                    str += ("${spec.specValue},")
            }
            if (str.length > 1){
                str.removeRange(str.length-1,str.length)
            }else{
                str = "默认"
            }
            result.spec = str


            var singgleArray = ArrayList<SingglePromotionViewModel>()
            json.valueJsonArray("single_list").arrayObjects().forEach{ singleDic ->
                var single = SingglePromotionViewModel(singleDic.valueInt( "activity_id"),
                        singleDic.valueInt("is_check") == 1,
                        singleDic.valueString("title"),
                        singleDic.valueString("promotion_type"))
                singgleArray.add(single)
            }
            if (singgleArray.count() > 0){
                result.promotionList = singgleArray
            }

            return  result
        }
    }

    /**
     * @Name  checkedObservable
     * @Type  ObservableField<Boolean>
     * @Note  商品选择监听
     */
    val checkedObservable = ObservableField(isCheck)

    /**
     * @author LDD
     * @From   CartGoodsItemViewModel
     * @Date   2018/3/15 下午4:39
     * @Note   促销标题
     */
    fun title():String{
        var title = ""
        promotionList?.forEach {
            if (it.isCheck){
                title =  it.title
            }
        }
        if (title.isEmpty()){
            title = "不参与促销活动"
        }
        return title
    }
}

/**
 * @author LDD
 * @Date   2018/3/15 下午4:40
 * @From   com.enation.javashop.android.middleware.model
 * @Note   单品促销
 */
data class SingglePromotionViewModel(var promotionId: Int,  /**促销ID*/
                                     var isCheck: Boolean,  /**是否选择*/
                                     var title: String,
                                     var type :String)     /**促销标题*/