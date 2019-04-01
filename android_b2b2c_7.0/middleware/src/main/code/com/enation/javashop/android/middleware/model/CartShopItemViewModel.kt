package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/15 下午4:49
 * @From   com.enation.javashop.android.middleware.model
 * @Note   购物车店铺Item
 */
data class CartShopItemViewModel(val name:String,           /**店铺名称*/
                                 val isSelf:Boolean,        /**是否是自营店铺*/
                                 val hasBouns:Boolean,      /**是否有可以领取的优惠券*/
                                 val shopId : Int,          /**店铺ID*/
                                 val isCheck:Boolean)       /**是否选中*/ {

    companion object {

        fun map(json :JSONObject) : CartShopItemViewModel{
            return CartShopItemViewModel(json.valueString("seller_name"),
                    json.valueInt("seller_id") == 1,
                    true,
                    json.valueInt("seller_id"),
                    json.valueInt("checked") == 1)
        }

    }

    /**
     * @Name  isCheckObserver
     * @Type  ObservableField<Boolean>
     * @Note  选中监听着
     */
    var isCheckObserver = ObservableField(isCheck)
}