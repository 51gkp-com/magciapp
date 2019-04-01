package com.enation.javashop.android.middleware.logic.contract.welcome

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.AdViewModel

/**
 * @author  LDD
 * @Date   2018/1/9 下午2:04
 * @From   com.enation.javashop.android.lib.logic.contract.welcome
 * @Note   欢迎页面 MVP接口
 */
interface WelcomeContract {

    /**
     * @author  LDD
     * @Data   2018/1/9 下午2:05
     * @From   WelcomeContract
     * @Note   欢迎页面基础View接口
     */
    interface View : BaseContract.BaseView{
        /**
         * @author  LDD
         * @From    View
         * @Date   2018/1/9 下午2:07
         * @Note   跳转到主页面
         */
        fun toHome()

        /**
         * @author  LDD
         * @From    View
         * @Date   2018/1/9 下午2:08
         * @Note   跳转广告页面
         * @param  data  广告数据
         */
        fun showAd(data: ArrayList<AdViewModel>)
    }

    /**
     * @author  LDD
     * @Date   2018/1/9 下午2:09
     * @From   WelcomeContract
     * @Note   欢迎页面Presenter接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author  LDD
         * @From    Presenter
         * @Date   2018/1/9 下午2:40
         * @Note   加载广告
         */
        fun loadAd()
    }
}