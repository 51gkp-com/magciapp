package com.enation.javashop.android.middleware.logic.contract.member

import android.graphics.Bitmap
import com.enation.javashop.android.lib.base.BaseContract

/**
 * @author LDD
 * @Date   2018/5/14 下午2:51
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   图片验证码逻辑控制器
 */
interface ImageVcodeContract {

    /**
     * @author LDD
     * @Date   2018/5/14 下午2:52
     * @From   ImageVcodeContract
     * @Note   图片验证码视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/14 下午3:15
         * @Note   渲染验证码
         * @param  bitmap  验证码图片
         */
        fun renderVcode(bitmap :Bitmap)


    }

    /**
     * @author LDD
     * @Date   2018/5/14 下午2:53
     * @From   ImageVcodeContract
     * @Note   图片验证码逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 下午3:17
         * @Note   加载登录图片验证码
         */
        fun loadLoginImageVcode()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 下午3:18
         * @Note   加载注册图片验证码
         */
        fun loadRegisterImageVcode()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 下午3:18
         * @Note   加载找回密码图片验证码
         */
        fun loadFindImageVcdoe()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 下午3:18
         * @Note   加载绑定手机号验证码
         */
        fun loadBindImageVcode()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 下午3:19
         * @Note   加载更改密码图片验证码
         */
        fun loadEditImageVcode()

        /**
         * @author LDD
         * @Date   2018/8/14 上午9:40
         * @From   Presenter
         * @Note   加载手机验证
         */
        fun loadValidateMobileVcodde()

    }

}