package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.*
import org.json.JSONObject

/**
 * @author LDD
 * @Date   2018/3/27 下午2:15
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品评论ViewModel
 */
data class GoodsCommentViewModel(val userName:String,           /**用户姓名*/
                                 val userFace:String,           /**用户头像*/
                                 val commentTime:String,        /**评论时间*/
                                 val buyTime:String,            /**购买时间*/
                                 var images:ArrayList<String>,  /**图片集合*/
                                 var grade :Int,                /**评分*/
                                 val content:String,            /**评论详细*/
                                 val reply :String?)             /**店家回复*/

{

    fun getStarViewNum() :Int{
        if (grade == 3){
          return 5
        }
        if (grade == 2){
            return 3
        }
        if (grade == 1){
            return 1
        }
        return 5
    }

    companion object {

        fun map(json:JSONObject) :GoodsCommentViewModel{
            var comment =  GoodsCommentViewModel(
                    json.valueString("member_name"),
                    json.valueString("member_face"),
                    json.valueDate("create_time"),
                    json.valueDate("create_time"),
                    ArrayList<String>(),
                    0,
                    json.valueString("content"),
                    json.valueJsonObject("reply").valueString("content")
            )

            val grade = json.valueString( "grade")

            if (grade == "good"){
                comment.grade = 3
            }else if (grade == "neutral"){
                comment.grade = 2
            }else if (grade == "bad") {
                comment.grade = 1
            }

            val imageJson = json.valueJsonArray(
                    "images")

            if (imageJson.length() > 0){
                for (i in 0..(imageJson.length()-1)){
                    comment.images.add(imageJson.optString(i))
                }
            }

            return  comment
        }

    }

}
