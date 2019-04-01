package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/5/23 下午6:40
 * @From   com.enation.javashop.android.middleware.logic.contract.order
 * @Note   订单创建商品页面接口控制
 */
interface OrderCreateGoodsContract {

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:42
     * @From   OrderCreateGoodsContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView{

    }

    /**
     * @author LDD
     * @Date   2018/5/23 下午6:42
     * @From   OrderCreateGoodsContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{


    }

}