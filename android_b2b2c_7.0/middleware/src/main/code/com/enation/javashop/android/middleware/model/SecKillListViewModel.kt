package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.valueInt
import com.enation.javashop.android.lib.utils.valueString
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/5/21 下午3:38
 * @From   com.enation.javashop.android.middleware.model
 * @Note   秒杀种类列表VM
 */
data class SecKillListViewModel(var distanceTime :Int =0,var nextDistanceTime :Int=0 ,var text :String=""){

    companion object {

        fun map(dic :JSONObject) :SecKillListViewModel{
            var model = SecKillListViewModel()

            model.distanceTime = dic.valueInt( "distance_time")

            model.text = dic.valueString( "time_text")

            model.nextDistanceTime = dic.valueInt( "next_distance_time")

            return model
        }

    }

}