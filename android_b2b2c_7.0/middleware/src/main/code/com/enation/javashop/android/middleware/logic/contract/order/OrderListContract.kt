package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.OrderItemViewModel

/**
 * @author LDD
 * @Date   2018/4/16 下午4:15
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单列表接口控制器
 */
interface OrderListContract {

    /**
     * @author LDD
     * @Date   2018/4/16 下午4:18
     * @From   OrderListContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{


        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/16 下午4:34
         * @Note   初始化订单列表
         * @param  orderList 订单列表
         */
        fun initOrderList(orderList :ArrayList<OrderItemViewModel>)


    }

    /**
     * @author LDD
     * @Date   2018/4/16 下午4:21
     * @From   OrderListContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/16 下午4:21
         * @Note   加载订单列表数据
         * @param  state 订单状态
         * @param  page  分页查询
         */
        fun loadOrderList(state :String , page :Int)


        /**
         * @author Snow
         * @From   View
         * @Date   2018/4/16 下午4:34
         * @Note   确认收货
         * @param  orderSn 订单编号
         */
        fun rog(orderSn :String)

        /**
         * @author Snow
         * @From   View
         * @Date   2018/4/16 下午4:34
         * @Note   取消订单
         * @param   orderSn 订单编号
         * @param   reason    取消原因
         */
        fun cancal(orderSn :String,reason :String)

    }

}