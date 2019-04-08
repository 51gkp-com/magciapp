package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.ObserableString

/**
 * @author LDD
 * @Date   2018/4/23 上午10:19
 * @From   com.enation.javashop.android.middleware.model
 * @Note   返回数据
 */
data class ReturnData(var orderSn :String = "",              /**订单号*/
var returnPoint :Int = 0,             /**售后积分*/
var skuId    :Int = 0,                /**单个商品售后时的skuID*/
var refuseType :String= "",           /**售后类型 退货/退款*/
var refundWay :String= "",            /**退款方式*/
var accountType :String= "",          /**账号类型*/
var returnAccount :String= "",        /**退款账号*/
var customerRemark :String= "",       /**买家留言*/
var refundReason :String= "",         /**退款原因*/
var bankName : String= "",            /**银联退款的话 银行名*/
var bankAccountNumber:String= "",     /**银联退款的话 银行账号*/
var bankAccountName:String= "",       /**银联退款的话 开户名*/
var bankDepositName:String= "",       /**银联退款的话 开户行*/
var returnNum :Int= 0)            /**单个退款时 退货个数*/

data class AccountInfo(var accountType :String = "WEIXINPAY",/**账号类型*/
                        var returnAccount :ObserableString = ObserableString(""),/**退款账号*/
                        var bankName : ObserableString = ObserableString(""),/**银联退款的话 银行名*/
                        var bankAccountNumber:ObserableString = ObserableString(""),/**银联退款的话 银行账号*/
                        var bankAccountName:ObserableString = ObserableString(""),/**银联退款的话 开户名*/
                        var bankDepositName:ObserableString = ObserableString("")){

    fun reset(){
        returnAccount.set("")
        bankName.set("")
        bankAccountNumber.set("")
        bankAccountName.set("")
        bankDepositName.set("")
    }

}