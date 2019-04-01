package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/22 上午11:08
 * @From   com.enation.javashop.android.middleware.model
 * @Note   团购积分商城列表ViewModel
 */
data class GroupPointViewModel(var id :Int =0,var title :String = ""){
    companion object {

        fun map(dic :JSONObject) :GroupPointViewModel{
            val result =  GroupPointViewModel()
            result.id = (dic.valueInt( "cat_id") == -1).judge(dic.valueInt( "category_id"),dic.valueInt( "cat_id"))
            result.title = (dic.valueString( "cat_name") == "").judge(dic.valueString( "name"),dic.valueString( "cat_name"))
            return  result
        }
    }
}
