package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * Created by LDD on 2018/10/15.
 */
data class ShopItem(var shopName : String = "",
                    var collectNum : Int = 0,
                    var logo : String = "",
                    var shopId :Int = -1,
                    var collectId :Int = -1,
                    var childGoods :ArrayList<GoodsItemViewModel> = ArrayList()) {

    companion object {

        fun map(json :JSONObject):ShopItem{
            var self = ShopItem()
            self.shopId = json.valueInt("shop_id")
            self.collectNum = json.valueInt( "shop_collect")
            self.shopName = json.valueString( "shop_name")
            self.logo = json.valueString("shop_logo")
            var goods = ArrayList<GoodsItemViewModel>()
            json.valueJsonArray( "goods_list").arrayObjects().forEach { dic ->
                    goods.add(GoodsItemViewModel.goodssearchMap(dic))
            }
            self.childGoods = goods
            return  self
        }

    }
}