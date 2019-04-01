package com.enation.javashop.android.middleware.logic.contract.goods

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/3/9 上午11:36
 * @From   com.enation.javashop.android.middleware.logic.contract.goods
 * @Note   商品搜索页面接口控制器
 */
interface GoodsSearchContract {

    /**
     * @author LDD
     * @Date   2018/3/9 上午11:36
     * @From   GoodsSearchContract
     * @Note   商品搜索页面View接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/3/9 上午11:47
         * @Note   展示商品列表
         * @param  goodsList 商品列表
         */
        fun showGoodsList(goodsList:ArrayList<GoodsItemViewModel>)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/3/9 上午11:51
         * @Note   初始化筛选页面
         * @param  filters 筛选信息
         */
        fun initFilterPage(filters :ArrayList<GoodsFilterViewModel>)
    }

    /**
     * @author LDD
     * @Date   2018/3/9 上午11:37
     * @From   GoodsSearchContract
     * @Note   商品搜索页面逻辑接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午11:52
         * @Note   搜索商品
         * @param  params 搜索参数
         */
        fun searchGoods(page :Int,params : HashMap<String,Any>)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午11:57
         * @Note   加载筛选条件
         * @param  params 商品搜索索引
         */
        fun loadFilter(params: HashMap<String, Any>)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午11:57
         * @Note   加入购物车
         * @param  skuId 商品SKUId
         */
        fun addCart(skuId :Int ,num : Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午11:58
         * @Note   商品收藏
         * @param  goodsId 商品Id
         */
        fun  collectionGoods(goodsId :Int)
    }

}