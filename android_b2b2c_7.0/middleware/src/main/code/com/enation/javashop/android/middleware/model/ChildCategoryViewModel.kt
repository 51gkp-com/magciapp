package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/27 下午1:54
 * @From   com.enation.javashop.android.middleware.model
 * @Note   子分类
 */
data class ChildCategoryViewModel(val catId: Int ,          /**分类ID*/
                                  val name:String,          /**分类名称*/
                                  val imageUrl :String)     /**分类图片*/{

    companion object {
        fun map( json :JSONObject) : ChildCategoryViewModel{
            return ChildCategoryViewModel(json.valueInt("category_id"),
                    json.valueString("name"),
                    json.valueString("image"))

        }
    }

}

/**
 *
 * @author Snow
 * @Note   子分类可以
 */
data class  ChildCategoryShell(val parentName :String,                          /** 二级分类名称 */
                               val item :ArrayList<ChildCategoryViewModel>)     /** 三级分类集合 */{

    companion object {
        fun map(json :JSONObject) : ChildCategoryShell{

            val items = ArrayList<ChildCategoryViewModel>()

            val jsonChildList = json.getJSONArray("children")

            if (jsonChildList.length() > 0){
                for(i in 0..(jsonChildList.length() - 1)){
                    val itemJson = jsonChildList.getJSONObject(i)
                    items.add(ChildCategoryViewModel.map(itemJson))
                }
            }

            return ChildCategoryShell(json.valueString("name"),items)

        }
    }

}