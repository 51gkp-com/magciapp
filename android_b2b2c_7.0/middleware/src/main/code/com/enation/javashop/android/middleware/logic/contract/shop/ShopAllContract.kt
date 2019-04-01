package com.enation.javashop.android.middleware.logic.contract.shop

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/4/9 下午5:26
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   店铺全部商品页面接口
 */
interface ShopAllContract {

    /**
     * @author LDD
     * @Date   2018/4/9 下午5:26
     * @From   ShopAllContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/9 下午5:27
         * @Note   初始化商品列表
         * @param  data 商品数据
         */
        fun initGoods(data :List<GoodsItemViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/4/9 下午5:26
     * @From   ShopAllContract
     * @Note   逻辑接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/9 下午5:27
         * @Note   加载店铺内商品
         * @param  filter 筛选数据
         * @param  page   分页查询
         */
        fun loadGoods(filter :HashMap<String,Any> , page :Int)

    }

}