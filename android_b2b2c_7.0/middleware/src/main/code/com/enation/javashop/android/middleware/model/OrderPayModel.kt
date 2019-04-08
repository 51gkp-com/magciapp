package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.middleware.enum.GlobalState

/**
 * Created by LDD on 2018/10/11.
 */
data class OrderPayModel(var sn: String = "",

                         var payPrice: Double = 0.0,

                         var tradeType: TradeType = TradeType.Order,

var payId: String = "",

var client: String = "NATIVE",

var paymode: String = "normal",

var paymentType: Int = GlobalState.PAY_ONLINE)


enum class TradeType {
     Order{
         override fun toString(): String {
             return  "order"
         }
     } ,
     Trade {
         override fun toString(): String {
             return  "trade"
         }
     }
}
