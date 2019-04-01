package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.ShopViewModel

/**
 * @author LDD
 * @Date   2018/5/4 下午3:07
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   关注页面接口控制器
 */
interface MemberCollectContract {

    /**
     * @author LDD
     * @Date   2018/5/4 下午3:10
     * @From   CommentListContract
     * @Note   视图借口
     */
    interface View : BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/4 下午3:11
         * @Note   渲染商品UI
         * @param  data 商品数据
         */
        fun renderGoodsUI(data :ArrayList<GoodsItemViewModel>)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/4 下午3:12
         * @Note   渲染店铺UI
         * @param  data 店铺UI
         */
        fun renderShopUI(data: ArrayList<ShopViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/4 下午3:27
     * @From   CommentListContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/4 下午3:14
         * @Note   加载数据
         * @param  page 分页
         * @param  type 加载数据
         */
        fun loadData(page :Int , state :Int)

    }

}