package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.bindingParams
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/17 上午10:23
 * @From   com.enation.javashop.android.middleware.model
 * @Note   发票ViewModel
 */
data class ReceiptViewModel(var id :Int = 0,/**ID*/
                            var duty_invoice :ObservableField<String> = ObservableField(""),/**纳税人税号*/
                            var receipt_content:String = "",/**发票内容*/
                            var receipt_title:ObservableField<String> = ObservableField(""),/**发票抬头*/
                            var receipt_type:String = "")        /**发票类型*/
{
    companion object {
        fun map(json :JSONObject) :ReceiptViewModel{
            return  ReceiptViewModel(json.valueInt("id"),
                    json.valueString("tax_no").bindingParams(),
                    json.valueString("receipt_content"),
                    json.valueString("receipt_title").bindingParams(),
                    (json.valueInt("type") == 1).judge("country","person"))
        }
    }
}