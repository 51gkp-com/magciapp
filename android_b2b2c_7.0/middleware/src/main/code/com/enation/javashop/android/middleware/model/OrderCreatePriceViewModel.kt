package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.bindingParams
import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/17 上午9:57
 * @From   com.enation.javashop.android.middleware.model
 * @Note   订单创建时价格详细
 */
data class OrderCreatePriceViewModel(val goodsPrice :ObservableField<String>,
                                     val discountPrice :ObservableField<String>,
                                     val shipPrice :ObservableField<String>,
                                     val pointPrice :ObservableField<String>,
                                     val cashPrice :ObservableField<String>,
                                     val payPrice :ObservableField<String>){
    companion object {
        fun map(dic :JSONObject) :OrderCreatePriceViewModel{
            return OrderCreatePriceViewModel(ObservableField(String.format("￥%.2f",dic.valueDouble("original_price"))),
                    ObservableField(String.format("-￥%.2f",dic.valueDouble("coupon_price"))),
                    ObservableField(String.format("+￥%.2f",dic.valueDouble("freight_price"))),
                    ObservableField(String.format("+%d积分",dic.valueInt("exchange_point"))),
                    ObservableField(String.format("-￥%.2f",dic.valueDouble("cash_back"))),
                    ObservableField(String.format("￥%.2f",dic.valueDouble("total_price"))))
        }
    }
}