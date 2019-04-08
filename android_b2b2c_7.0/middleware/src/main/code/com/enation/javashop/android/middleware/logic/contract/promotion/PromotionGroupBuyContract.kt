package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GroupPointViewModel

/**
 * @author LDD
 * @Date   2018/5/22 上午11:11
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   团购页面接口控制器
 */
interface PromotionGroupBuyContract {

    /**
     * @author LDD
     * @Date   2018/5/22 上午11:13
     * @From   PromotionGroupBuyContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/22 上午11:15
         * @Note   渲染团购
         * @param  data 数据
         */
        fun renderGroupBuy(data :ArrayList<GroupPointViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/22 上午11:13
     * @From   PromotionGroupBuyContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/22 上午11:15
         * @Note   加载团购数据
         */
        fun loadGroupBuy()

    }

}