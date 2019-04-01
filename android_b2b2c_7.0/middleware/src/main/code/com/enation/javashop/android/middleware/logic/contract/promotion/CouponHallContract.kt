package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CouponViewModel

/**
 * Created by LDD on 2018/10/29.
 */
interface CouponHallContract {

    interface View :BaseContract.BaseView{

        fun render(list :ArrayList<CouponViewModel>)

    }

    interface Presenter :BaseContract.BasePresenter{

        fun load(page :Int)

        fun getCoupon(id :Int)

    }

}