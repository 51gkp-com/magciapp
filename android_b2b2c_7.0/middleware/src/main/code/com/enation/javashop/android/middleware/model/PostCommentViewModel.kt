package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.ObserableString

/**
 * @author LDD
 * @Date   2018/4/26 下午2:33
 * @From   com.enation.javashop.android.middleware.model
 * @Note   评论页面 ViewModel
 */
data class PostCommentViewModel(val sn :String,                                        /**店铺ID*/
                                var desStar :Int,                                       /**描述相符*/
                                var logisticsStar :Int,                                 /**物流相符*/
                                var serviceStar :Int,                                   /**服务相符*/
                                val goodsList :ArrayList<PostCommentGoodsViewModel>)    /**商品信息列表*/{

    fun getPostData():PostCommentModel{
        val goods = ArrayList<PostGoodsModel>()
        goodsList.forEach {
            val item = PostGoodsModel()
            item.content = it.commentContent.get()
            if (it.goodsStar < 3) {
                item.grade = "bad"
            } else if (it.goodsStar == 3 || it.goodsStar == 4) {
                item.grade = "neutral"
            } else {
                item.grade = "good"
            }
            item.images = it.commentImages
            item.sku_id = it.goodsSkuId
            goods.add(item)
        }
        val result = PostCommentModel()
        result.delivery_score = logisticsStar
        result.description_score = desStar
        result.service_score = serviceStar
        result.order_sn = sn
        result.comments = goods
        return  result
    }

}

data class PostCommentGoodsViewModel(val goodsImage :String,                    /**商品图片*/
                                     var goodsStar :Int,                        /**商品评论星级*/
                                     val goodsSkuId :Int,                       /**商品SkuId*/
                                     var commentContent :ObserableString,                /**商品评论内容*/
                                     val commentImages :ArrayList<String>)      /**评论图片集合*/

data class PostCommentModel (

    var delivery_score : Int = 5,

    var description_score : Int = 5,

    var order_sn :String = "",

    var service_score :Int = 5,

    var comments : ArrayList<PostGoodsModel> = ArrayList()
)

data class PostGoodsModel (

        var content :String = "",

        var grade :String = "",

        var images :ArrayList<String> = ArrayList(),

        var sku_id :Int = 0

)