package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import com.enation.javashop.districtselectorview.model.BaseRagionModel
import com.enation.javashop.pay.core.wechat.WechatHelper
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/9 上午8:59
 * @From   com.enation.javashop.android.middleware.model
 * @Note   地区ViewModel
 */
data class RegionViewModel(val typeId :Int ,val name :String ,val id :Int ,val pid :Int) :BaseRagionModel {

    /**
     * @author LDD
     * @From   RegionViewModel
     * @Date   2018/5/9 上午9:00
     * @Note   获取TypeId
     * @param
     */
    override fun getType(): Int {
        return typeId - 1
    }

    override fun getPickerName(): String {
        return name
    }

    companion object {

        fun map(objc :JSONObject) : RegionViewModel{

            return  RegionViewModel(objc.valueInt("region_grade"),
                    objc.valueString("local_name"),
                    objc.valueInt("id"),
                    objc.valueInt("parent_id"))

        }

    }
}