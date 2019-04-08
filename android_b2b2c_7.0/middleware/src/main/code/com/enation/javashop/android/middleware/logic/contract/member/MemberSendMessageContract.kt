package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract

/**
 * @author LDD
 * @Date   2018/5/10 上午11:44
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   发送短信接口控制器
 */
interface MemberSendMessageContract {

    /**
     * @author LDD
     * @Date   2018/5/10 上午11:46
     * @From   MemberSendMessageContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/8/14 上午11:20
         * @Note   发送成功
         * @param  mobile 手机号
         */
        fun sendSuccess(mobile :String)

        fun sendFindPwdMessage(mobile :String)

    }

    /**
     * @author LDD
     * @Date   2018/5/10 上午11:47
     * @From   MemberSendMessageContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/10 上午10:09
         * @Note   发送绑定手机号短信
         * @param  phoneNum 手机号
         * @param  sendVcode 发送短信验证码
         */
        fun sendBindPhoneNumVcode(phoneNum: String, sendVcode: String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/10 上午10:09
         * @Note   发送注册短信
         * @param  phoneNum 手机号
         * @param  sendVcode 发送短信验证码
         */
        fun sendRegisterVcode(phoneNum: String, sendVcode: String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/10 上午10:10
         * @Note   发送找回密码短信
         * @param  phoneNum 手机号
         * @param  sendVcode 发送短信验证码
         */
        fun sendFindPasswordVcode(phoneNum: String, sendVcode: String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/10 上午10:09
         * @Note   发送修改密码短信
         * @param  phoneNum 手机号
         * @param  sendVcode 发送短信验证码
         */
        fun sendEditPasswordNumVcode(phoneNum: String, sendVcode: String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 上午10:02
         * @Note   发送手机号快捷登录信息
         * @param  phone 手机号
         * @param  vcode 手机验证码
         */
        fun sendPhoneLoginMessage(phone:String,vcode:String)

        fun checkFindPwdAccount(account:String,vcode:String)

    }

}