package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/5/22 上午11:19
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   积分商城子页面接口控制
 */
interface PromotionPointShopFragContract {

    /**
     * @author LDD
     * @Date   2018/5/22 上午11:19
     * @From   PromotionPointShopFragContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/22 上午11:22
         * @Note   渲染积分商城
         * @param  data 数据
         */
        fun renderPointShop(data :ArrayList<GoodsItemViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/22 上午11:20
     * @From   PromotionPointShopFragContract
     * @Note   逻辑接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/22 上午11:20
         * @Note   加载积分商城数据
         * @param  id   分类ID
         * @param  page 分页查询
         */
        fun loadPointShop(id :Int , page :Int)

    }

}