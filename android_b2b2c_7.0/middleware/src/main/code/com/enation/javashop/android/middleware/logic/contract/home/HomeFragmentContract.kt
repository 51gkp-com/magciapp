package com.enation.javashop.android.middleware.logic.contract.home

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.FloorViewModel
import com.enation.javashop.android.middleware.model.RecommendGoodsViewModel
import org.json.JSONArray

/**
 * @author LDD
 * @Date   2018/1/19 下午5:50
 * @From   com.enation.javashop.android.middleware.logic.contract.home
 * @Note   HomeFragment接口控制器
 */
interface HomeFragmentContract {

    /**
     * @author LDD
     * @Date   2018/1/19 下午5:51
     * @From   HomeFragmentContract
     * @Note   View接口
     */
    interface View : BaseContract.BaseView{

        /**
         * @author LDD
         * @Date   2018/1/19 下午5:51
         * @From   HomeFragmentContract.View
         * @Note   展示七巧板数据
         */
        fun renderFloor(data : ArrayList<Any>)

    }

    /**
     * @author LDD
     * @Date   2018/1/19 下午5:51
     * @From   HomeFragmentContract
     * @Note   逻辑控制接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   HomeFragmentContract.Presenter
         * @Date   2018/1/19 下午5:53
         * @Note   加载七巧板数据
         */
        fun loadFloor()

    }
}