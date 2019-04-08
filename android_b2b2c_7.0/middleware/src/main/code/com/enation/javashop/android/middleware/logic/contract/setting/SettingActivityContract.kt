package com.enation.javashop.android.middleware.logic.contract.setting

import android.content.Context
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 * @author LDD
 * @Date   2018/3/6 下午3:52
 * @From   com.enation.javashop.android.middleware.logic.contract.setting
 * @Note   设置页面接口控制
 */
interface SettingActivityContract {


    /**
     * @author LDD
     * @Date   2018/2/24 下午4:51
     * @From   SettingActivityContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/3/9 上午9:34
         * @Note   展示用户信息
         * @param  member 用户信息数据
         */
        fun renderInfo(member:MemberViewModel)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/8/13 下午9:12
         * @Note   退出登录
         */
        fun logout()


        fun renderCacheSize(size :String)
    }

    /**
     * @author LDD
     * @Date   2018/2/24 下午4:51
     * @From   SettingActivityContract
     * @Note   逻辑层接口
     */
    interface Presenter : BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午9:34
         * @Note   加载用户信息
         */
        fun loadMemberInfo()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午9:34
         * @Note   App分享
         */
        fun appShare()

        fun getCacheSize(context: Context)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/3/9 上午9:35
         * @Note   清除缓存
         */
        fun clearChche(context : Context)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/8/13 下午8:52
         * @Note   退出登录
         */
        fun logout(uid :String)
    }
}
