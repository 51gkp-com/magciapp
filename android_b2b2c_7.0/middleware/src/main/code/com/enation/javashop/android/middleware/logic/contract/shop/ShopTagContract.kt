package com.enation.javashop.android.middleware.logic.contract.shop

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/4/9 下午5:28
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   店铺标签商品页面接口控制
 */
interface ShopTagContract {

    /**
     * @author LDD
     * @Date   2018/4/9 下午5:29
     * @From   ShopTagContract
     * @Note   视图借口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/9 下午5:31
         * @Note   初始化商品数据
         * @param  data 商品数据
         */
        fun initGoods(data :List<GoodsItemViewModel>)
    }
    
    /**
     * @author LDD
     * @Date   2018/4/9 下午5:29
     * @From   ShopTagContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From    Presenter
         * @Date   2018/4/9 下午5:31
         * @Note   加载商品数据
         * @param  tag 标签
         * @param  shopId 店铺Id
         */
        fun loadGoods(tag :String , shopId :Int)
    }

}