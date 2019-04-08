package com.enation.javashop.android.middleware.logic.contract.shop

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.ShopFirstViewModel

/**
 * @author LDD
 * @Date   2018/4/9 下午5:06
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   商品首页接口控制器
 */
interface ShopHomeContract {

    /**
     * @author LDD
     * @Date   2018/4/9 下午5:06
     * @From   ShopHomeContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{
        
        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/9 下午5:21
         * @Note   初始化首页数据
         * @param  data 数据
         */
        fun initFirstData(data :ShopFirstViewModel)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/9 下午5:21
         * @Note   初始化优惠券
         * @param  data 优惠券数据
         */
        fun initCoupon(data :List<CouponViewModel>)
    }

    /**
     * @author LDD
     * @Date   2018/4/9 下午5:07
     * @From   ShopHomeContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/9 下午5:22
         * @Note   加载首页数据
         * @param  shopId 店铺ID
         */
        fun loadFirstData(shopId : Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/9 下午5:23
         * @Note   加载优惠券数据
         * @param  shopId 店铺ID
         */
        fun loadCoupon(shopId :Int)
    }

}