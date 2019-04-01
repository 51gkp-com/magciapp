package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.OrderItemViewModel

/**
 * @author LDD
 * @Date   2018/4/24 下午5:55
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   评论列表接口控制
 */
interface CommentListContract {

    /**
     * @author LDD
     * @Date   2018/4/24 下午5:57
     * @From   CommentListContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/24 下午5:58
         * @Note   渲染评论UI
         * @param  data 可以评论的订单
         */
        fun renderCommentUi(data :ArrayList<OrderItemViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/4/24 下午5:57
     * @From   CommentListContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/24 下午6:00
         * @Note   加载评论数据
         */
        fun loadComment(page :Int)

    }

}