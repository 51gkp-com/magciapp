package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.LogisticsViewModel
import com.enation.javashop.android.middleware.model.OrderDetailViewModel

/**
 * @author LDD
 * @Date   2018/4/18 下午4:09
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单详细接口控制器
 */
interface OrderDetailContract {

    /**
     * @author LDD
     * @Date   2018/4/18 下午4:09
     * @From   OrderDetailContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/18 下午4:16
         * @Note   渲染订单UI
         * @param  order 订单信息
         */
        fun renderOrder(order :ArrayList<Any>)

        fun notification()
    }

    /**
     * @author LDD
     * @Date   2018/4/18 下午4:09
     * @From   Presenter
     * @Note   逻辑层接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/18 下午4:18
         * @Note   加载订单详情
         * @param  orderSn 订单号
         */
        fun loadOrder(orderSn :String)

        fun rog(orderSn: String)


        fun cancel(orderSn: String,reson :String)

        fun addCart(skuId :Int)

    }

}