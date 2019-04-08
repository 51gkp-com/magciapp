package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CouponViewModel

/**
 * @author LDD
 * @Date   2018/5/3 下午4:19
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   会员优惠券
 */
interface MemberCouponConract {

    /**
     * @author LDD
     * @Date   2018/5/3 下午4:20
     * @From   MemberCouponConract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/3 下午4:21
         * @Note   渲染优惠券视图
         * @param  data 优惠券列表
         */
        fun renderCoupon(data :ArrayList<CouponViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/3 下午4:21
     * @From   MemberCouponConract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/3 下午4:21
         * @Note   加载优惠券信息
         * @param  page  分页加载
         * @param  state 优惠券状态
         */
        fun loadCoupon(page :Int,state :Int)

    }

}