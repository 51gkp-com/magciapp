package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.getJsonString
import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueLong
import com.enation.javashop.android.lib.utils.valueString
import com.enation.javashop.utils.base.tool.CommonTool
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.*

/**
 * @author LDD
 * @Date   2018/1/16 下午12:24
 * @From   com.enation.javashop.android.middleware.model
 * @Note   用户信息Model
 * @param  username 用户名
 * @param  nikename 昵称
 * @param  buyPoint 消费积分
 * @param  levelPoint 等级积分
 * @param  face 头像
 * @param  sex 性别
 * @param  birthday 生日
 * @param  pro 省份
 * @param  proId 省份ID
 * @param  city 城市
 * @param  cityId 城市ID
 * @param  county 县级
 * @param  countyId 县级ID
 * @param  town 乡村
 * @param  townId 乡村ID
 * @param  address 详细地址
 * @param  email 电子邮件
 * @param  tel 固话
 */
data class MemberViewModel(var username:String ,
                           var nikename :String? ,
                           val buyPoint :Int ,
                           val levelPoint :Int ,
                           var face :String ?,
                           var sex :Int ,
                           var birthday :Long?,
                           var pro :String?,
                           var proId :Int?,
                           var city :String?,
                           var cityId :Int?,
                           var county :String?,
                           var countyId :Int?,
                           var town :String?,
                           var townId :Int?,
                           var address :String?,
                           var email :String?,
                           var mobile :String?,
                           var tel :String?){

    constructor() : this("","" , 0 , 0,"",0,0,"",0,"",0,"",0,"",0,"","","","")

    fun getSexString() :String{
        if (sex ==1) {
            return "男"
        } else{
            return "女"
        }
    }

    fun getRegionId() : Int{

        if (townId != null && townId!! > 0){
            return townId!!
        }

        if (countyId != null && countyId!! > 0){
            return countyId!!
        }

        return 0

    }

    fun getBirthdayString() :String{
        if (birthday!=null){
            return CommonTool.toString(Date(birthday!!),null)
        }else{
            return ""
        }
    }

    fun getRegion() : String{
        return  "$pro$city$county$town"
    }

    companion object {

        /**
         * @author LDD
         * @From   MemberViewModel
         * @Date   2018/8/13 上午10:15
         * @Note   解析Json
         * @param  body 响应体
         */
        fun map(body :ResponseBody) : MemberViewModel{

            var objc = JSONObject(body.getJsonString())

            return MemberViewModel(objc.valueString("uname"),
                    objc.valueString("nickname"),
                    objc.valueInt("consum_point"),
                    objc.valueInt("grade_point"),
                    objc.valueString("face"),
                    objc.valueInt("sex"),
                    objc.valueLong("birthday"),
                    objc.valueString("province"),
                    objc.valueInt("province_id"),
                    objc.valueString("city"),
                    objc.valueInt("city_id"),
                    objc.valueString("county"),
                    objc.valueInt("county_id"),
                    objc.valueString("town"),
                    objc.valueInt("town_id"),
                    objc.valueString("address"),
                    objc.valueString("email"),
                    objc.valueString("mobile"),
                    objc.valueString("tel"))

        }

    }

}