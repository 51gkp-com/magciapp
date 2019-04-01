package com.enation.javashop.android.middleware.logic.contract.shop

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ShopCategoryViewModel

/**
 * @author LDD
 * @Date   2018/4/12 上午9:00
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   店铺种类接口控制
 */
interface ShopCategoryActivityContract {

    /**
     * @author LDD
     * @Date   2018/4/12 上午9:01
     * @From   ShopCategoryActivityContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/12 上午9:08
         * @Note   初始化分类
         * @param  data 数据
         */
        fun initCategory(data :ArrayList<ShopCategoryViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/4/12 上午9:02
     * @From   ShopCategoryActivityContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/12 上午9:08
         * @Note   加载分类数据
         * @param  shopId     店铺ID
         */
        fun loadCategory(shopId :Int)

    }

}