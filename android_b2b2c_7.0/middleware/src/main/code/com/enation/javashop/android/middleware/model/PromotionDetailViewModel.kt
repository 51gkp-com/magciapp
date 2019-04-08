package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/27 下午1:20
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品详情促销ViewModel
 */
data class PromotionDetailViewModel(
        var minusPromotion: MinusPromotion? = null,        /**单品立减*/
        var FullDiscountGroup :FullDiscount? = null,       /**满减组合*/
        var halfPrice: HalfPrice? = null,                  /**第二件半价*/
        var exchange: Exchange? = null,                    /**积分商城*/
        var groupBuy: GroupBuy? = null,                    /**团购*/
        var secKill: SecKill? = null)                      /**秒杀*/ {

    fun inject(promotion :PromotionDetailViewModel){
        if (promotion.exchange != null) {
            this.exchange = promotion.exchange
        }
        if (promotion.FullDiscountGroup != null) {
            this.FullDiscountGroup = promotion.FullDiscountGroup
        }
        if (promotion.groupBuy != null){
            this.groupBuy = promotion.groupBuy
        }
        if (promotion.minusPromotion != null){
            this.minusPromotion = promotion.minusPromotion
        }
        if (promotion.halfPrice != null) {
            this.halfPrice = promotion.halfPrice
        }
        if (promotion.secKill != null) {
            this.secKill = promotion.secKill
        }
    }

    companion object {

        fun map(resultObject :JSONObject) :PromotionDetailViewModel{

            var parent = PromotionDetailViewModel()

            val minusVO = resultObject.valueJsonObject("minus_vo")

            if (minusVO.length() > 0) {
                parent.minusPromotion = MinusPromotion(
                        minusVO.valueString("title"),
                        minusVO.valueDouble("single_reduction_value"),
                        minusVO.valueDate("start_time")+"-"+minusVO.valueDate("end_time"),
                        minusVO.valueString("description"),
                        true
                )
            }

            val resultFullDiscount = resultObject.valueJsonObject("full_discount_vo")
            if (resultFullDiscount.length() > 0) {
                val fulldiscountView = FullDiscount()
                fulldiscountView.basePrice = resultFullDiscount.valueDouble("full_money")
                fulldiscountView.date = resultFullDiscount.valueDate("start_time")+"-"+resultFullDiscount.valueDate("end_time")
                fulldiscountView.title = resultFullDiscount.valueString("title")
                fulldiscountView.descript = resultFullDiscount.valueString("description")
                fulldiscountView.isDiscount = (resultFullDiscount.valueInt("is_discount") == 0).judge(false,true)
                fulldiscountView.isFreeShip = (resultFullDiscount.valueInt("is_free_ship") == 0).judge(false,true)
                fulldiscountView.isGift = (resultFullDiscount.valueInt("is_send_gift") == 0).judge(false,true)
                fulldiscountView.isBonus = (resultFullDiscount.valueInt("is_send_bonus") == 0).judge(false,true)
                fulldiscountView.isMinus = (resultFullDiscount.valueInt("is_full_minus") == 0).judge(false,true)
                fulldiscountView.minusPrice = resultFullDiscount.valueDouble("minus_value")
                fulldiscountView.discount = resultFullDiscount.valueDouble("discount_value")
                if(fulldiscountView.isGift){
                    var giftDO = resultFullDiscount.valueJsonObject("full_discount_gift_do")

                    var gift = Gift(giftDO.valueString("gift_name"),
                            giftDO.valueDouble("gift_price"),
                            giftDO.valueString("gift_img"),
                            giftDO.valueInt("gift_type")
                    )
                    fulldiscountView.giftValue = gift
                }

                if(fulldiscountView.isBonus){
                    var bonusDO = resultFullDiscount.valueJsonObject("coupon_do")

                    var bonusView = CouponViewModel(
                            bonusDO.valueDouble("coupon_price"),
                            bonusDO.valueDouble("coupon_threshold_price"),
                            bonusDO.valueString("seller_name"),
                            false,
                            false,
                            false,
                            bonusDO.valueInt("coupon_id"),
                            bonusDO.valueDate("start_time")+"-"+bonusDO.valueDate("end_time"),
                            bonusDO.valueInt("seller_id"),
                            bonusDO.valueString("title")
                    )
                    fulldiscountView.bonusValue = bonusView
                }
                parent.FullDiscountGroup = fulldiscountView
            }

            //第二件半价
            val halfPriceVO = resultObject.valueJsonObject("half_price_vo")
            if (halfPriceVO.length() > 0){
                parent.halfPrice = HalfPrice(
                        halfPriceVO.valueString("description"),
                        false,
                        halfPriceVO.valueDate("start_time")+"-"+halfPriceVO.valueDate("end_time"),
                        halfPriceVO.valueString("title")
                )
            }


            //积分
            val exchangeVO = resultObject.valueJsonObject("exchange")

            if (exchangeVO.length() > 0) {
                parent.exchange = Exchange(
                        exchangeVO.valueInt("exchange_id"),
                        exchangeVO.valueDouble("exchange_money"),
                        exchangeVO.valueDouble("exchange_point"),
                        false,
                        0,
                        0,
                        exchangeVO.valueDouble("goods_price")
                )
            }

            val groupBuyVO = resultObject.valueJsonObject("groupbuy_goods_vo")
            if (groupBuyVO.length() > 0){
                parent.groupBuy = GroupBuy(groupBuyVO.valueInt("act_id"),
                        groupBuyVO.valueString("gb_name"),
                        groupBuyVO.valueDouble("price"),
                        groupBuyVO.valueDate("start_time")+"-"+groupBuyVO.valueDate("end_time"),
                        groupBuyVO.valueInt("goods_num"),
                        groupBuyVO.valueDouble("original_price"),
                        resultObject.valueLong("end_time"),
                        true,
                        groupBuyVO.valueInt("buy_num")
                )
            }

            var seckillVO = resultObject.valueJsonObject("seckill_goods_vo")
            if(seckillVO.length() > 0){
                parent.secKill =  SecKill(resultObject.valueInt("activity_id"),
                        "限时抢购",
                        seckillVO.valueInt("is_start") == 1,
                        seckillVO.valueDouble("seckill_price"),
                        seckillVO.valueDouble("original_price"),
                        seckillVO.valueDate("seckill_start_time"),
                        seckillVO.valueLong("distance_start_time"),
                        seckillVO.valueLong("distance_end_time")
                )
            }
            return parent
        }
    }
}


/**
 * @author LDD
 * @Date   2018/3/27 下午1:22
 * @From   com.enation.javashop.android.middleware.model
 * @Note   赠品ViewModel
 */
data class Gift(val name: String,       /**赠品名称*/
                val price: Double,      /**赠品价格*/
                val image: String,      /**赠品图片*/
                val type: Int)          /**赠品类型*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:24
 * @From   com.enation.javashop.android.middleware.model
 * @Note   单品立减
 */
data class MinusPromotion(val title: String,        /**标题*/
                          val minusPrice: Double,   /**立减金额*/
                          val date: String,         /**活动日期区间*/
                          val descript: String,     /**描述*/
                          val disabled: Boolean)    /**是否开启*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:26
 * @From   com.enation.javashop.android.middleware.model
 * @Note   满减组合促销
 */
data class FullDiscount(var basePrice: Double = 0.0,          /**满减最低金额条件*/
                        var date: String = "",               /**活动日期区间*/
                        var title: String = "",              /**标题*/
                        var descript: String = "",           /**描述*/
                        var isDiscount: Boolean = false,        /**是否满折*/
                        var isFreeShip: Boolean = false,        /**是否包邮*/
                        var isGift: Boolean = false,            /**是否送赠品*/
                        var isPonit: Boolean = false,           /**是否送积分*/
                        var isBonus: Boolean = false,           /**是否送优惠券*/
                        var isMinus: Boolean = false,           /**是否满减*/
                        var pointValue: Int = 0,            /**赠送的积分*/
                        var minusPrice: Double = 0.0,         /**满减金额*/
                        var discount: Double = 0.0,           /**满折*/
                        var giftValue:Gift? = null,             /**赠品*/
                        var bonusValue:CouponViewModel? = null, /**优惠券*/
                        var disabled: Boolean = false)          /**是否开启*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:30
 * @From   com.enation.javashop.android.middleware.model
 * @Note   第二件半价
 */
data class HalfPrice(val descript : String,     /**描述*/
                     val disabled: Boolean,     /**是否开启*/
                     val date:String,           /**活动日期区间*/
                     val title: String)         /**标题*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:32
 * @From   com.enation.javashop.android.middleware.model
 * @Note   积分换购
 */
data class Exchange(val id :Int,
                    val exchangeMoney:Double,       /**金额*/
                    val exchangePoint:Double,       /**积分*/
                    val disabled: Boolean,          /**是否开启活动*/
                    val exchangeNum:Int,            /**总数量*/
                    val buyNum: Int,                /**已购买数量*/
                    val orgPrice: Double)           /**原价*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:32
 * @From   com.enation.javashop.android.middleware.model
 * @Note   团购
 */
data class GroupBuy(var id :Int = 0,
                    val title:String,           /**标题*/
                    val price:Double,           /**价格*/
                    val date :String,           /**互动日期区间*/
                    val groupNum:Int,           /**总库存*/
                    val orgPrice:Double,        /**原价*/
                    val endTime:Long,           /**结束时间*/
                    val disabled: Boolean,      /**是否开启*/
                    val buyNum :Int)            /**已购数量*/

/**
 * @author LDD
 * @Date   2018/3/27 下午1:32
 * @From   com.enation.javashop.android.middleware.model
 * @Note   秒杀
 */
data class SecKill(var id :Int = 0,
                   val title:String,             /**标题*/
                   val disabled:Boolean,         /**是否开启*/
                   val seckillPrice:Double,      /**秒杀价*/
                   val orgPrice: Double,         /**原价*/
                   val date:String,              /**活动日期区间*/
                   var disStartTime :Long,        /**距离开始时间*/
                   val disEndTime: Long)         /**距离结束时间*/