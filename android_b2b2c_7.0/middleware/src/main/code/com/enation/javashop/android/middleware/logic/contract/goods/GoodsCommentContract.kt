package com.enation.javashop.android.middleware.logic.contract.goods

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CommentNumViewModel
import com.enation.javashop.android.middleware.model.GoodsCommentViewModel

/**
 * @author LDD
 * @From   com.enation.javashop.android.middleware.logic.contract.goods
 * @Date   2018/3/30 下午2:09
 * @Note   商品评论接口控制器
 */
class GoodsCommentContract {

    /**
     * @author LDD
     * @Date   2018/3/30 下午2:10
     * @From   GoodsCommentContract
     * @Note   接口控制器
     */
    interface View : BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/3/30 下午2:40
         * @Note   显示评论
         * @param  comments 评论数据
         */
        fun showComment(comments :ArrayList<GoodsCommentViewModel>)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/3/30 下午2:41
         * @Note   显示评论数据
         * @param  commentNum 评论数据
         */
        fun showCommentNum(commentNum :CommentNumViewModel)
    }

    /**
     * @author LDD
     * @Date   2018/3/30 下午2:16
     * @From   GoodsCommentContract
     * @Note   逻辑接口控制
     */
    interface Presenter : BaseContract.BasePresenter{
        
        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/30 下午2:20
         * @Note   加载评论数
         * @param  goodsId 商品ID
         */
        fun loadCommentNum(goodsId: Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/30 下午2:20
         * @Note   加载评论数据
         * @param  goodsId 商品ID
         * @param  state   评论分类
         * @param  page    分页
         */
        fun loadComment(goodsId : Int ,state : String,page : Int)
    }
}