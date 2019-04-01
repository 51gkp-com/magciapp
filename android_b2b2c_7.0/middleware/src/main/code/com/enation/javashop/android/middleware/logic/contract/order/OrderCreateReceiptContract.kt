package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ReceiptContentViewModel
import com.enation.javashop.android.middleware.model.ReceiptViewModel

/**
 * @author LDD
 * @Date   2018/5/23 下午6:23
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   发票填写页面
 */
interface OrderCreateReceiptContract {

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:24
     * @From   OrderCreateReceiptContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/23 下午6:32
         * @Note   渲染发票内容
         * @param  data 数据
         */
        fun renderReceiptContent(data :ArrayList<ReceiptContentViewModel>)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/23 下午6:32
         * @Note   渲染发票列表
         * @param  data 发票数据
         */
        fun renderReceiptList(data :ArrayList<ReceiptViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:24
     * @From   OrderCreateReceiptContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/23 下午6:32
         * @Note   加载发票列表
         */
        fun loadReceiptList()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/23 下午6:33
         * @Note   新增发票
         * @param  data 发票信息
         */
        fun addReceipt(data :ReceiptViewModel)


        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/23 下午6:53
         * @Note   加载发票内容列表
         */
        fun loadReceiptContent()

        fun setReceipt(receiptViewModel: ReceiptViewModel)

    }

}