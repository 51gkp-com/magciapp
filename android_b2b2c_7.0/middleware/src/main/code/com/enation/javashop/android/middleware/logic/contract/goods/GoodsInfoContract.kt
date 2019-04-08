package com.enation.javashop.android.middleware.logic.contract.goods

import com.alibaba.android.vlayout.DelegateAdapter
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.*

/**
 * @author LDD
 * @Date   2018/3/27 上午11:01
 * @From   com.enation.javashop.android.middleware.logic.contract.goods
 * @Note   商品详细页面接口控制器
 */
interface GoodsInfoContract {

    /**
     * @author LDD
     * @Date   2018/3/27 上午11:02
     * @From   GoodsInfoContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/10/6 下午9:35
         * @Note   初始化列表数据
         * @param  data 试图适配器列表
         */
        fun initList(goodsDetail : GoodsViewModel,
                     gallery : ArrayList<GoodsGallery>,
                     promotion : PromotionDetailViewModel,
                     skuGoodsList : ArrayList<SkuGoods>,
                     couponList : ArrayList<CouponViewModel>,
                     commentList : HashMap<String,Any>,
                     seller :  ShopViewModel)


    }

    /**
     * @author LDD
     * @Date   2018/3/27 上午11:02
     * @From   GoodsInfoContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/10/6 下午9:36
         * @Note   加载数据
         * @param  goodsId 商品ID
         * @param  shopId  店铺ID
         */
        fun loadData(goodsId :Int ,shopId :Int)


        fun getCoupon(couponId :Int)

        fun addCart(id :Int,num : Int , actId :Int?)

        fun buyNum(id :Int,num : Int , actId :Int?)

    }
}