package com.enation.javashop.android.middleware.model

import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/1/22 下午12:12
 * @From   com.enation.javashop.android.middleware.model
 * @Note   父分类model
 */
data class ParentCategoryViewModel(val parentId:Int,    /**分类父ID*/
                                   val name:String) {   /**分类名称*/
    val selected  = ObservableField(false)
    val bind_name = ObservableField(name)

    companion object {

        fun map(jsonObject :JSONObject) :ParentCategoryViewModel{
            return ParentCategoryViewModel(jsonObject.valueInt("category_id"),
                    jsonObject.valueString("name"))
        }

    }

}