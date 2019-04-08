package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * @author LDD
 * @Date   2018/3/27 下午1:45
 * @From   com.enation.javashop.android.middleware.model
 * @Note   Sku商品列表
 */
data class SkuGoods(var sn :String = "",
                    var buyCount : Int = 1,             /**购买数量*/
                    val skuId :Int,                     /**skuID*/
                    val goodsId :Int,                   /**GoodsId*/
                    val goodsName:String,               /**商品名称*/
                    val stock :Int,                     /**总库存*/
                    val enableStock:Int,                /**剩余库存*/
                    val price:Double,                   /**价格*/
                    val orgPrice:Double,                /**原始价格*/
                    val weight:Double,                  /**商品重量*/
                    val image:String,                   /**商品图片*/
                    val shopName:String,                /**所属店铺名称*/
                    val shopId:Int,                     /**所属店铺Id*/
                    val specList:ArrayList<Spec>,       /**规格列表*/
                    var identifier :String = ""){

    fun getSpecString() :String {
        var str :String = ""
        specList.forEach { spec ->
            str += ("${spec.specValue},")
        }
        if (str.length > 1){
            str.removeRange(str.length-1,str.length)
        }else{
            str = "默认"
        }
        return  str
    }
    companion object {

        fun mapSpec(datas :ArrayList<SkuGoods>) :ArrayList<Any>{
            var dataSource = ArrayList<Any>()
            if (datas.count() > 1) {
                for (i in 0..(datas.count() - 1)){
                for (j in 0..(datas[i].specList.count() - 1)){
                var has = false
                if (dataSource.count() != 0){
                    for (k in 0..(dataSource.count() - 1)){
                    if (dataSource[k] is Spec && (dataSource[k] as Spec).specId == datas[i].specList[j].specId &&  (dataSource[k] as Spec).specValueId == datas[i].specList[j].specValueId){
                        if (!has){
                            has = true
                        }
                    }
                }
                }
                if (!has){
                    dataSource.add(datas[i].specList[j])
                }
            }
            }
                var specSection = ArrayList<ArrayList<Spec>>()
                for (index in 0..(dataSource.count() - 1)){
                if (specSection.count() == 0){
                    var spec = (dataSource[index] as Spec)
                    spec.select = true
                    dataSource[index] = spec
                    specSection.add(arrayListOf(dataSource[index] as Spec))
                }else{
                    var has = false
                    for (parentIndex in 0..(specSection.count() - 1)){
                        if (specSection[parentIndex][0].specId == (dataSource[index] as Spec).specId){
                            has = true
                            if (!(specSection[parentIndex].contains((dataSource[index] as Spec)))){
                                specSection[parentIndex].add((dataSource[index] as Spec))
                            }
                        }
                    }
                    if (!has){
                        var spec = (dataSource[index] as Spec)
                        spec.select = true
                        dataSource[index] = spec
                        specSection.add(arrayListOf(dataSource[index] as Spec))
                    }
                }
            }

                dataSource.clear()

                for (parentIndex in 0..(specSection.count() - 1)){
                for (childIndex in 0..(specSection[parentIndex].count() - 1)){
                if (childIndex == 0){
                    dataSource.add(specSection[parentIndex][childIndex].specName)
                }
                dataSource.add(specSection[parentIndex][childIndex])
            }
            }
            }
            return dataSource
        }

        fun identifier(specs :ArrayList<Spec>) : String{
            specs.sortWith(Comparator sort@{ speca, specb ->
                return@sort (speca.specId > specb.specId).judge(1,-1)
            })
            var text = ""
            specs.forEach { item ->
                    text += "${item.specId}-${item.specValueId}"
            }
            return text
        }

        fun map(jsonObject :JSONObject) :SkuGoods{
            val specArray = ArrayList<Spec>()

            //遍历规格信息
            var specList = jsonObject.valueJsonArray("spec_list")
            for (i in 0..(specList.length() - 1)) {
                val specObject = specList.getJSONObject(i)

                var spec = Spec(
                        specObject.valueInt("spec_value_id"),
                        specObject.valueInt("spec_id"),
                        specObject.valueString("spec_name"),
                        specObject.valueString("spec_value"),
                        specObject.valueString("spec_image"),
                        specObject.valueInt("spec_type")
                )
                specArray.add(spec)
            }

            return  SkuGoods(jsonObject.valueString("sn"),
                    0,
                    jsonObject.valueInt("sku_id"),
                    jsonObject.valueInt("goods_id"),
                    jsonObject.valueString("goods_name"),
                    jsonObject.valueInt("quantity"),
                    jsonObject.valueInt("enable_quantity"),
                    jsonObject.valueDouble("price"),
                    jsonObject.valueDouble("price"),
                    jsonObject.valueDouble("weight"),
                    jsonObject.valueString("thumbnail"),
                    jsonObject.valueString("seller_name"),
                    jsonObject.valueInt("seller_id"),
                    specArray,SkuGoods.identifier(specArray)
            )
        }
    }
}

/**
 * @author LDD
 * @Date   2018/3/27 下午1:58
 * @From   com.enation.javashop.android.middleware.model
 * @Note   规格信息
 */
data class Spec(val specValueId:Int,        /**规格ID值*/
                val specId:Int,             /**规格ID*/
                val specName:String,        /**规格名*/
                val specValue:String,       /**规格值名称*/
                val specImage:String,       /**规格默认图*/
                val specType:Int,           /**规格Type*/
                var select :Boolean = false){
    companion object {
        fun map(json :JSONObject) :Spec{
            return Spec(
                    json.valueInt("spec_value_id"),
                    json.valueInt("spec_id"),
                    json.valueString("spec_name"),
                    json.valueString("spec_value"),
                    json.valueString("spec_image"),
                    json.valueInt("spec_type")
            )
        }
    }
}