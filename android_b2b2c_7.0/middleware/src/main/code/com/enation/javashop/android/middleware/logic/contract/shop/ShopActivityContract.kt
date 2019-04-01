package com.enation.javashop.android.middleware.logic.contract.shop

import android.app.Activity
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ShopViewModel

/**
 * @author LDD
 * @Date   2018/4/8 下午3:21
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   店铺页面接口控制器
 */
interface ShopActivityContract {

    /**
     * @author LDD
     * @Date   2018/4/8 下午3:26
     * @From   ShopActivityContract
     * @Note   视图
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/9 下午5:18
         * @Note   初始化店铺信息
         * @param  shop 店铺信息
         */
        fun initShop(shop :ShopViewModel)
    }

    /**
     * @author LDD
     * @Date   2018/4/8 下午3:26
     * @From   ShopActivityContract
     * @Note   视图控制器
     */
    interface Presenter:BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/9 下午5:19
         * @Note   加载店铺信息
         * @param  storeId 店铺ID
         */
        fun loadShopInfo(shopId : Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/9 下午5:19
         * @Note   关注店铺
         * @param  shopId 店铺ID
         * @param  isCollect 是否关注
         */
        fun collectShop(shopId :Int ,isCollect :Boolean)


        fun share(activity: Activity, url: String, image: String, title: String, description: String)
    }

}