package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/5/22 上午10:43
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   团购子页面接口控制器
 */
interface PromotionGroupBuyFragContract {

    /**
     * @author LDD
     * @Date   2018/5/22 上午10:44
     * @From   PromotionGroupBuyFragContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/22 上午10:45
         * @Note   渲染团购商品数据
         * @param  data 团购商品数据
         */
        fun renderGroupBuy(data :ArrayList<GoodsItemViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/22 上午10:44
     * @From   PromotionGroupBuyFragContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/22 上午10:49
         * @Note   加载团购商品数据
         * @param  id 分类ID
         * @param  page 分页查询
         */
        fun loadGroupBuy(id : Int ,page :Int)

    }

}