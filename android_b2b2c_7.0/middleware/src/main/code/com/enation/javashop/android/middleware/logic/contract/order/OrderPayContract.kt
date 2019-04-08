package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.OrderPayModel
import com.enation.javashop.android.middleware.model.PayMethodViewModel

/**
 * @author LDD
 * @Date   2018/4/20 下午5:55
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单支付逻辑控制
 */
interface OrderPayContract {

    /**
     * @author LDD
     * @Date   2018/4/20 下午5:56
     * @From   OrderPayContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/20 下午6:08
         * @Note   支付状态回调
         * @param  state 支付状态
         */
        fun callApp(str : String)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/20 下午6:09
         * @Note   渲染支付方式UI
         * @param  methods 支付方法数据
         */
        fun renderPayMethod(methods :ArrayList<PayMethodViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/4/20 下午5:56
     * @From   OrderPayContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/20 下午6:09
         * @Note   获取支付签名去支付
         * @param  sn 订单号或者交易号
         * @param  payMethodId 支付方式Id
         * @param  orderType  订单类型 订单/交易
         */
        fun toPay(pay : OrderPayModel)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/20 下午6:11
         * @Note   加载支付方式
         */
        fun loadPayMethod()

    }

}