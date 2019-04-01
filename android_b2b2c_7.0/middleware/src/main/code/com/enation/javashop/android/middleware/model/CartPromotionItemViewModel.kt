package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import com.enation.javashop.android.middleware.enum.Promotion
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/15 下午4:42
 * @From   com.enation.javashop.android.middleware.model
 * @Note   购物车促销ViewModel
 */
data class CartPromotionItemViewModel(val isFullPrice:Boolean,                      /**价格是否已满*/
                                      val isFullDicount:Boolean,                    /**是否满折*/
                                      val isFullMinus:Boolean,                      /**是否满减*/
                                      val isPoint:Boolean,                          /**是否送积分*/
                                      val isGift:Boolean,                           /**是否包含赠品*/
                                      val isBouns:Boolean,                          /**是否赠送优惠券*/
                                      val isFreeShip:Boolean,                       /**是否免运费*/
                                      val shopId:Int,                               /**店铺ID*/
                                      val promotionContent: String,                 /**促销描述*/
                                      val promotionList:List<PromotionViewModel>,   /**促销列表*/
                                      val difference :Double                        /**价格未满足满减时的差价*/
                                      ){

    companion object {

        fun map(json :JSONObject):CartPromotionItemViewModel{
            return  CartPromotionItemViewModel(false,
                    json.valueInt("is_discount") == 1,
                    json.valueInt("is_full_minus") == 1,
                    json.valueInt("is_send_point") == 1,
                    json.valueInt("is_send_gift") == 1,
                    json.valueInt("is_send_bonus") == 1,
                    json.valueInt("is_free_ship") == 1,
                    0,
                    json.valueString("description"),
                    emptyList(),
                    0.0)
        }

    }

}

/**
 * @author LDD
 * @Date   2018/3/15 下午4:47
 * @From   com.enation.javashop.android.middleware.model
 * @Note   促销ViewModel
 */
data class PromotionViewModel(val promotionType : Promotion,       /**促销类型*/
                              val promotionContent:String,         /**促销内容*/
                              val promotionId:Int,                 /**促销ID*/
                              val promotionStartTime:String,       /**促销开始时间*/
                              val promotionEndTime:String)         /**促销结束时间*/
