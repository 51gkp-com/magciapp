package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueDate
import com.enation.javashop.android.lib.utils.valueDouble
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/27 下午3:15
 * @From   com.enation.javashop.android.middleware.model
 * @Note   会员邀请ViewModel
 */
data class InviteViewModel(val time:String,/**店铺描述*/
                         var name:String,/**是否已关注*/
                        var count:Int/**商品总数量*/
                        ){
    companion object {
        fun map(jsonObject : JSONObject) : InviteViewModel{
            val result = InviteViewModel(
                    jsonObject.valueString("time"),
                    jsonObject.valueString("name"),
                    jsonObject.valueInt("goods_num")
                    )
            return result
        }
    }
}