package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel

/**
 * @author LDD
 * @Date   2018/5/8 上午10:15
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   地址修改添加页面
 */
interface MemberAddressEditContract {

    /**
     * @author LDD
     * @Date   2018/5/8 上午10:19
     * @From   MemberAddressEditContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/9 上午9:15
         * @Note   渲染地区
         * @param  data 地区数据
         */
        fun renderRegion(data :ArrayList<RegionViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/8 上午10:19
     * @From   MemberAddressEditContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/8 上午10:35
         * @Note   添加地址
         * @param  data 地址
         */
        fun addAddress(data :MemberAddressViewModel)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/8 上午10:36
         * @Note   删除地址
         * @param  id 地址索引
         */
        fun deleteAddress(id :Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/8 上午10:36
         * @Note   修改地址
         * @param  data 修改后的数据
         */
        fun editAddress(data :MemberAddressViewModel)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 上午8:53
         * @Note   获取地区
         * @param  id 地区父ID
         */
        fun getRegion(id :Int)

        /**
         * 设置默认地址
         * @param id
         */
        fun setDefault(id :Int)

    }

}