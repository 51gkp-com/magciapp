package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 * @author LDD
 * @Date   2018/5/9 上午10:53
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   安全页面接口控制
 */
interface MemberSecurityContract {

    /**
     * @author LDD
     * @Date   2018/5/9 上午10:54
     * @From   MemberSecurityContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/9 上午11:11
         * @Note   渲染UI
         * @param  bindPhoneText 绑定Text
         */
        fun renderUI(member :MemberViewModel)

    }

    /**
     * @author LDD
     * @Date   2018/5/9 上午10:55
     * @From   MemberSecurityContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 上午11:11
         * @Note   验证绑定手机号状态
         */
        fun load()

    }
}