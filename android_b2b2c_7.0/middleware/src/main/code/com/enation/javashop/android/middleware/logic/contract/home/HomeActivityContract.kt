package com.enation.javashop.android.middleware.logic.contract.home

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 * @author LDD
 * @Date   2018/1/16 上午11:56
 * @From   com.enation.javashop.android.middleware.logic.contract.home
 * @Note   首页 MVP接口
 */
interface HomeActivityContract {

    /**
     * @author LDD
     * @Date   2018/1/16 下午12:01
     * @From   HomeActivityContract
     * @Note   View接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/1/16 下午12:02
         * @Note   处理User相应状态
         * @param  isLogin 是否登录
         */
        fun onUserState(userInfo : MemberViewModel)

    }

    /**
     * @author  LDD
     * @Date   2018/1/16 下午12:03
     * @From   HomeActivityContract
     * @Note   基础Presenter接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/1/16 下午12:04
         * @Note   用户是否登录
         */
        fun isLogin()

    }
}