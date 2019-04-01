package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberAddressViewModel

/**
 * @author LDD
 * @Date   2018/5/7 下午2:46
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   会员收货地址逻辑控制器
 */
interface MemberAddressContract {
    
    /**
     * @author LDD
     * @Date   2018/5/7 下午2:49
     * @From   MemberAddressContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/7 下午3:58
         * @Note   渲染地址UI
         * @param  data 地址数据
         */
        fun renderAddress(data :ArrayList<MemberAddressViewModel>)

        fun updateOrderAddress()

        fun deleteSuccess()

        fun setDefaultSuccess()

    }


    /**
     * @author LDD
     * @Date   2018/5/7 下午2:49
     * @From   MemberAddressContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{
        
        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/7 下午2:53
         * @Note   加载地址信息
         */
        fun loadAddress(page :Int)

        fun setOrderAddress(id :Int)

        fun setDefalut(id :Int)

        fun delete(id :Int)

    }

}