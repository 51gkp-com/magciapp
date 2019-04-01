package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract

/**
 * @author LDD
 * @Date   2018/5/9 下午2:05
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   检测手机验证码Activity
 */
interface MemberCheckVcodeContract {


    /**
     * @author LDD
     * @Date   2018/5/9 下午2:05
     * @From   MemberCheckVcodeContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{


    }

    /**
     * @author LDD
     * @Date   2018/5/9 下午2:05
     * @From   MemberCheckVcodeContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   MemberCheckVcodePresenter
         * @Date   2018/5/9 下午2:38
         * @Note   检查重新绑定手机号验证码
         * @param  vcode 验证码
         * @param  phoneNum 手机号
         */
        fun checkUpDatePhoneNumVcode(vcode: String, phoneNum: String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 下午2:31
         * @Note   检查绑定手机号验证码
         * @param  vcode    验证码
         * @param  phoneNum 手机号
         */
        fun checkBindPhoneNumVcode(vcode :String,phoneNum :String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 下午2:32
         * @Note   检查注册验证码
         * @param  vcode 验证码
         * @param  phoneNum 手机号
         */
        fun checkRegisterVcode(vcode :String,phoneNum :String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 下午2:33
         * @Note   验证码找回密码手机号
         * @param  vcode 验证码
         * @param  phoneNum 手机号
         */
        fun checkFindPasswordVcode(vcode :String,phoneNum :String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/9 下午2:33
         * @Note   验证码修改密码手机号
         * @param  vcode 验证码
         * @param  phoneNum 手机号
         */
        fun checkEditPasswordVcode(vcode :String,phoneNum :String)

    }

}