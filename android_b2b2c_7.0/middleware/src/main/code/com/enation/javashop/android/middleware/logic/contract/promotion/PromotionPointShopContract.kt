package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GroupPointViewModel

/**
 * @author LDD
 * @Date   2018/5/22 上午11:16
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   积分商城页面
 */
interface PromotionPointShopContract {


    /**
     * @author LDD
     * @Date   2018/5/22 上午11:18
     * @From   PromotionPointShopContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/22 上午11:17
         * @Note   渲染积分商城
         * @param  data 数据
         */
        fun renderPointShop(data :ArrayList<GroupPointViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/22 上午11:19
     * @From   PromotionPointShopContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/22 上午11:17
         * @Note   加载积分商城
         */
        fun loadPointShop()

    }

}