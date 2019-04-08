package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CouponViewModel

/**
 * @author LDD
 * @Date   2018/5/23 下午3:55
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单优惠券
 */
interface OrderCreateCouponContract {

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:18
     * @From   OrderCreateCouponContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/23 下午7:00
         * @Note   渲染优惠券
         * @param  unusableCoupons 不可用
         * @param  usableCoupons   可用
         */
        fun renderCoupon(coupons : ArrayList<CouponViewModel>)

    }


    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/23 下午6:20
         * @Note   使用优惠券
         * @param  id 优惠券索引
         */
        fun useCoupon(id :Int,couponId :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/23 下午7:00
         * @Note   加载优惠券
         */
        fun loadCoupon(ids :String)

    }

}