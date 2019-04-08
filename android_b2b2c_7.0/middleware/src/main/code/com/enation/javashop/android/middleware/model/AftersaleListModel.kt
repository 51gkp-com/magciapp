package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * Created by LDD on 2018/10/16.
 */
class AftersaleListModel(var sn :String = "",

                         var state :String = "",

                         var type :String = "",

                         var money :Double = 0.0,

                         var point :Int = 0,

                         var failReson :String = "") {

    companion object {
        fun map(dic :JSONObject) :AftersaleListModel{
            var data = AftersaleListModel()
            data.sn = dic.valueString("sn")
            data.state = dic.valueString("refund_status_text")
            data.type = dic.valueString("refuse_type_text")
            data.money = dic.valueDouble( "refund_price")
            data.point = dic.valueInt( "refund_point")
            data.failReson = dic.valueString( "refund_fail_reason")
            return data
        }
    }

}