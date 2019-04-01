package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract

/**
 * @author LDD
 * @Date   2018/5/23 下午6:20
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   支付方式选择页面
 */
interface OrderCreatePayShipContract {

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:23
     * @From   OrderCreatePayShipContract
     * @Note   视图借口
     */
    interface View :BaseContract.BaseView{



    }

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:23
     * @From   OrderCreatePayShipContract
     * @Note   视图借口
     */
    interface Presenter : BaseContract.BasePresenter{

        fun setData(time:String,payType :String)

    }

}