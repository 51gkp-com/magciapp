package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueJsonObject
import com.enation.javashop.android.lib.utils.valueString
import com.enation.javashop.android.middleware.enum.GlobalState
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/23 上午8:54
 * @From   com.enation.javashop.android.middleware.model
 * @Note   创建订单初始数据
 */
data class OrderCreateParamsViewModel(val addressId :Int,               /**地址Id*/
                                       val paymentType :Int,         /**支付方式*/
                                       val receipt  :ReceiptViewModel?,  /**发票*/
                                       val receiveTime :Int,         /**配送时间*/
                                       val remark     :String?)          /**留言*/{

    companion object {

        fun map(dic :JSONObject) :OrderCreateParamsViewModel{

            var payType = GlobalState.PAY_ONLINE

            if (dic.valueString("payment_type") == "COD"){
                payType = GlobalState.PAY_COD
            }

            var shipTime = GlobalState.SHIP_EVERYTIME

            if (dic.valueString("receive_time") == "仅休息日"){
                shipTime = GlobalState.SHIP_RESTTIME
            }

            if (dic.valueString("receive_time") == "仅工作日"){
                shipTime = GlobalState.SHIP_WORKTIME
            }


            return  OrderCreateParamsViewModel(dic.valueInt("address_id"),
                    payType,
                    if (dic.valueJsonObject("receipt").length() == 0) { ReceiptViewModel() }else{ ReceiptViewModel.map(dic.valueJsonObject( "receipt"))},
                    shipTime,
                    dic.valueString( "remark"))

        }


    }

}