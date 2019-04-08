package com.enation.javashop.android.component.cart.util

import com.enation.javashop.android.middleware.model.CartPromotionItemViewModel
import com.enation.javashop.android.middleware.model.SingglePromotionViewModel

/**
 * @author LDD
 * @Date   2018/2/6 下午4:19
 * @From   com.enation.javashop.android.component.cart.util
 * @Note   购物车列表操作协议
 */
interface CartActionAgreement {

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:25
     * @Note   店铺选择
     * @param  shopId 店铺Id
     * @param  checked 是否选中
     */
    fun shopCheck(shopId :Int , checked : Boolean)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:26
     * @Note   商品数量修改
     * @param  productId 商品ID
     * @param  num 修改后的数量
     */
    fun goodsEdit(productId: Int,num :Int?,checked: Boolean?)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:28
     * @Note   组合促销详细
     * @param  cartPromotionItemViewModel 组合促销详情
     */
    fun groupPromotionDetail(cartPromotionItemViewModel: CartPromotionItemViewModel)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:29
     * @Note   单品促销选择
     * @param  singglepromotionList 单品促销列表
     */
    fun selectSingglePromotion(sellerId :Int , skuId :Int,singglepromotionList : List<SingglePromotionViewModel>?)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:30
     * @Note   显示商品操作遮罩
     * @param  productId  商品skuId
     * @param  goodsId    商品ID
     */
    fun showGoodsMoreMask(productId : Int , goodsId :Int)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/6 下午4:31
     * @Note   显示店铺优惠券列表
     * @param  shopId 店铺Id
     */
    fun showShopBonus(shopId: Int)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/24 下午1:43
     * @Note   去店铺
     * @param  shopId 店铺Id
     */
    fun toShop(shopId : Int)

    /**
     * @author LDD
     * @From   CartActionAgreement
     * @Date   2018/2/24 下午1:44
     * @Note   去商品页
     * @param  goodsId 商品ID
     */
    fun toGoods(goodsId :Int)

    fun deleteGoods(productId: Int)

}