package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/5/21 下午3:44
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   秒杀Frag接口控制器
 */
interface PromotionSecKillFragContract {

    /**
     * @author LDD
     * @Date   2018/5/21 下午3:51
     * @From   PromotionSecKillFragContract
     * @Note   视图借口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/21 下午3:50
         * @Note   渲染商品列表
         * @param  data 商品数据
         */
        fun renderGoodsList(data :ArrayList<GoodsItemViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/21 下午3:51
     * @From   Presenter
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/21 下午3:49
         * @Note   加载数据
         * @param  time 当前时间段
         * @param  page 分页查询
         */
        fun loadSecGoods(time :String , page :Int)

    }

}