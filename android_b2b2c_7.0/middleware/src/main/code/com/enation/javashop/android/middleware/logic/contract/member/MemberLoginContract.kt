package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 * @author LDD
 * @Date   2018/5/14 上午9:57
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   会员登录逻辑
 */
interface MemberLoginContract {

    /**
     * @author LDD
     * @Date   2018/5/14 上午9:58
     * @From   MemberLoginContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/14 上午10:03
         * @Note   更新用户信息
         * @param  info 用户信息
         */
        fun refreshMemberInfo(info :MemberViewModel)


        fun alipayAuthInfo(info :String)


        fun authLoginError()

    }

    /**
     * @author LDD
     * @Date   2018/5/14 上午10:11
     * @From   MemberLoginContract
     * @Note   逻辑控制
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 上午10:01
         * @Note   用户名登录
         * @param  username 用户名
         * @param  password 密码
         * @param  vcode    图片验证码
         */
        fun login(username :String , password :String ,vcode :String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/14 上午10:09
         * @Note   手机号快捷登录
         * @param  phone 手机号
         * @param  vcode 手机验证码
         */
        fun phoneLogin(phone :String , vcode: String)

        fun authMobileBind(type :String,openId :String,phone :String , vcode: String)

        /**
         * 获取支付宝第三方登录参数
         */
        fun getAlipayAuthInfo()

        /**
         * 第三方登录
         */
        fun authLogin(type :String,openId :String)

        fun authBind(type :String,openId :String,username :String , password :String ,vcode :String)

    }

}