package com.enation.javashop.android.middleware.logic.contract.shop

import android.app.Activity
import android.graphics.Bitmap
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ShopViewModel

/**
 * @author LDD
 * @Date   2018/4/13 下午4:40
 * @From   com.enation.javashop.android.middleware.logic.contract.shop
 * @Note   店铺信息页面接口控制器
 */
interface ShopInfoActivityContract {

    /**
     * @author LDD
     * @Date   2018/4/13 下午4:40
     * @From   ShopInfoActivityContract
     * @Note   视图控制器
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/13 下午4:44
         * @Note   初始化店铺数据
         * @param  data 店铺数据
         */
        fun initShopInfo(data :ShopViewModel)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/4/13 下午4:44
         * @Note   显示店铺二维码
         * @param  bitmap 二维码图片
         */
        fun showQrCode(bitmap: Bitmap)

    }

    /**
     * @author LDD
     * @Date   2018/4/13 下午4:40
     * @From   ShopInfoActivityContract
     * @Note   逻辑控制器
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/13 下午4:44
         * @Note   加载店铺信息
         * @param  shopId 店铺ID
         */
        fun loadShopInfo(shopId :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/4/13 下午4:45
         * @Note   构建店铺二维码
         * @param  shopId 店铺Id
         */
        fun buildQrCode(shopId :Int)

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