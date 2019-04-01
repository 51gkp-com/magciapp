package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.AfterSaleViewModel
import com.enation.javashop.android.middleware.model.ReturnData

/**
 * @author LDD
 * @Date   2018/4/23 上午8:30
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单售后接口控制
 */
interface OrderAfterSaleContract {

    /**
     * @author LDD
     * @Date   2018/4/23 上午8:32
     * @From   OrderAfterSaleContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/23 上午10:47
         * @Note   渲染售后视图
         * @param  data 售后视图模型
         */
        fun renderAfterSaleUI(data :AfterSaleViewModel)

    }
    
    /**
     * @author LDD
     * @Date   2018/4/23 上午8:33
     * @From   OrderAfterSaleContract
     * @Note   逻辑控制接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/23 上午10:48
         * @Note   加载售后数据
         * @param  orderSn  订单编号
         * @param  skuId    货品ID   整单退时 不填写
         */
        fun loadAfterSaleData(orderSn : String , skuId :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/23 上午10:49
         * @Note   退货
         * @param  returnData 退货参数
         */
        fun commitGoods(returnData: ReturnData)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/23 上午10:49
         * @Note   退款
         * @param  returnData 退款参数
         */
        fun commitMoney(returnData: ReturnData)

    }

}