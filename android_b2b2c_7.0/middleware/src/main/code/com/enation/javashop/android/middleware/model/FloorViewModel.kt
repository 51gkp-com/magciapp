package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueJsonObject
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject


data class FloorViewModel(val typeId : Int,
                          val itemList :ArrayList<FloorItem>) {

    fun mapBanner(): List<BannerModel> {
        return itemList.filter {
            return@filter  it.opt != null
        }.map {
            return@map BannerModel(it.getImageValue(),it.opt!!.type,it.opt!!.value)
        }
    }

    companion object {

        fun map(objc :JSONObject) :FloorViewModel{

            val items = ArrayList<FloorItem>()

            if (objc.has("blockList")){

                val itemJsonArray = objc.getJSONArray("blockList")

                for (i in 0..(itemJsonArray.length() - 1)){
                    items.add(FloorItem.map(itemJsonArray.getJSONObject(i)))
                }

            }

            return  FloorViewModel(objc.valueInt("tpl_id"),items)

        }

    }
}

data class FloorItem(var type :String = "",
                     var value :Any = "",
                     var opt :FloorOpt? = null){


    /// 获取商品
    ///
    /// - Returns: 商品
    fun getGoodsValue() : RecommendGoodsViewModel {
        return value as RecommendGoodsViewModel
    }

    /// 获取图片
    ///
    /// - Returns: 图片
    fun getImageValue() : String{
        return value as String
    }

    companion object {
        fun map(dic :JSONObject) :FloorItem{

            var floor = FloorItem()


            floor.type = dic.valueString( "block_type")
            if (dic.get("block_opt") is String){
                floor.opt = null
            }else{
                floor.opt = FloorOpt.map(dic.valueJsonObject("block_opt"))
            }
            if (dic.get("block_value") is String) {
                floor.value = dic.valueString( "block_value")
            } else {
                floor.value = RecommendGoodsViewModel.map(dic.valueJsonObject( "block_value"))
            }

            return  floor
        }
    }
}

/// 楼层扩展数据
data class FloorOpt(var type  :String = "" , var value :String = "") {

    companion object {
        fun map(dic :JSONObject) :FloorOpt{
            var opt = FloorOpt()
            opt.type = dic.valueString("opt_type")
            opt.value = dic.valueString("opt_value")
            return opt
        }
    }

}

data class BannerModel(var image :String = "",

                       var action :String = "",

                       var value :String = ""){

    companion object {
        fun map(dic :JSONObject) : BannerModel{
            var model = BannerModel()
            model.image = dic.valueString( "pic_url")
            model.action = dic.valueString("operation_type")
            model.value = dic.valueString("operation_param")
            return model
        }
    }

}

data class FloorMenuModel(var image :String = "",

                          var text :String = "",

                          var action :String = ""){

    companion object {
        fun map(dic :JSONObject) : FloorMenuModel{
            var model = FloorMenuModel()
            model.image = dic.valueString( "image")
            model.text = dic.valueString("navigation_name")
            model.action = dic.valueString( "url")
            return model
        }
    }

}