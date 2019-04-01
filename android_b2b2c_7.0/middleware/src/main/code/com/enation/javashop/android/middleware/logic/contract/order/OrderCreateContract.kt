package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.*

/**
 * @author LDD
 * @Date   2018/5/23 上午8:26
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单创建接口控制器
 */
interface OrderCreateContract {

    /**
     * @author LDD
     * @Date   2018/5/23 上午8:30
     * @From   OrderCreateContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * 渲染UI
         */
        fun renderUi(data :OrderCreateModel)

        fun createOrderResult(pay :OrderPayModel)

    }

    /**
     * @author LDD
     * @Date   2018/5/23 上午8:30
     * @From   OrderCreateContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        fun loadData()

        fun createOrder()


    }

}