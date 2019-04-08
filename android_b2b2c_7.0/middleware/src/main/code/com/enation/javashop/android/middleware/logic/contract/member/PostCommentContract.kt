package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.PostCommentModel
import com.enation.javashop.android.middleware.model.PostCommentViewModel

/**
 * @author LDD
 * @Date   2018/4/26 下午2:20
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   评论详细页面接口控制
 */
interface PostCommentContract {

    /**
     * @author LDD
     * @Date   2018/4/26 下午2:20
     * @From   PostCommentContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/26 下午2:32
         * @Note   渲染评论UI
         * @param  data  评论数据
         */
        fun renderCommentUi(data :PostCommentViewModel)

        fun imageUploadResult(url :String)
    }

    /**
     * @author LDD
     * @Date   2018/4/26 下午2:21
     * @From   PostCommentContract
     * @Note   逻辑控制器
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/26 下午2:51
         * @Note   加载评论数据
         * @param  orderSn  评论订单号
         */
        fun loadCommentData(orderSn : String)

        fun commit(data : PostCommentModel)

        fun uploader(filePath :String)

    }

}