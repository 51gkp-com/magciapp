package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/3/30 下午2:23
 * @From   com.enation.javashop.android.middleware.model
 * @Note   商品评论数ViewModel
 */
data class CommentNumViewModel(val all :Int,        /**全部数量*/
                               val goods:Int,       /**好评数*/
                               val secondary :Int,  /**中评数*/
                               val difference:Int,  /**差评数*/
                               val image :Int)      /**图片评论数*/