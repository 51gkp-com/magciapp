package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RecommendGoodsViewModel

/**
 * @author LDD
 * @Date   2018/2/24 下午4:49
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   个人中心MVP接口
 */
interface MemberFragmentContract {

    /**
     * @author LDD
     * @Date   2018/2/24 下午4:51
     * @From   MemberFragmentContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/2/24 下午5:21
         * @Note   展示会员信息
         * @param  member 会员信息
         */
        fun showMember(member: MemberViewModel)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/2/24 下午5:22
         * @Note   显示猜你喜欢
         * @param  goods 商品信息
         */
        fun showGuessLike(goods: List<RecommendGoodsViewModel>)
    }

    /**
     * @author LDD
     * @Date   2018/2/24 下午4:51
     * @From   MemberFragmentContract
     * @Note   逻辑层接口
     */
    interface Presenter : BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/24 下午5:22
         * @Note   加载会员详细信息
         */
        fun loadMemberInfo()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/24 下午5:23
         * @Note   加载猜你喜欢商品数据
         * @param  page 分页加载
         */
        fun loadGuessLike(page : Int)
    }
}