package com.enation.javashop.android.middleware.logic.contract.goods

import android.app.Activity
import android.graphics.Bitmap
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsViewModel

/**
 * @author LDD
 * @Date   2018/3/23 上午11:26
 * @From   com.enation.javashop.android.middleware.logic.contract.goods
 * @Note   商品Activity接口控制器
 */
interface GoodsActivityContract {

    /**
     * @author LDD
     * @Date   2018/3/23 下午2:49
     * @From   GoodsActivityContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView{
        fun initFragment(goods :GoodsViewModel)

        fun collect(collect: Boolean)

    }

    /**
     * @author LDD
     * @From   GoodsActivityContract
     * @Date   2018/3/23 下午2:50
     * @Note   数据接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/23 下午2:50
         * @Note   加载商品数据
         * @param  goodsId 商品索引ID
         */
        fun loadGoods(goodsId :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/23 下午3:20
         * @Note   收藏商品
         * @param  goodsId 商品索引ID
         */
        fun collectGoods(goodsId: Int,collect :Boolean)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/23 下午3:21
         * @Note   添加购物车
         * @param  skuId 规格商品ID
         * @param  num   商品数量
         */
        fun addToCart(skuId :Int ,num :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/23 下午3:22
         * @Note   分享商品
         * @param  activity    调用Activity
         * @param  url         分享URL
         * @param  image       商品图片
         * @param  title       标题
         * @param  description 商品标题
         */
        fun shareGoods(activity :Activity,url :String,image:String,title:String,description:String)
    }
}