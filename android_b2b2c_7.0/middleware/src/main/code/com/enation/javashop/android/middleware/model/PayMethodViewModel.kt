package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/4/20 下午6:08
 * @From   com.enation.javashop.android.middleware.model
 * @Note   支付方式VM
 */
data class PayMethodViewModel(var id :String ="",         /**支付方式ID*/
                              var name :String = "")    /**支付方式名称*/

{
    companion object {
        fun map(dic :JSONObject) :PayMethodViewModel{
            var model = PayMethodViewModel()
            model.id = dic.valueString( "plugin_id")
            model.name = dic.valueString( "method_name")
            return model
        }
    }
}