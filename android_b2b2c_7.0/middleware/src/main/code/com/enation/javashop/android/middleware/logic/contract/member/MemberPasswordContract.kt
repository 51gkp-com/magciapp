package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract

/**
 * @author LDD
 * @Date   2018/5/11 下午12:55
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   会员密码接口控制
 */
interface MemberPasswordContract {

    /**
     * @author LDD
     * @Date   2018/5/11 下午12:57
     * @From   MemberPasswordContract
     * @Note   视图借口
     */
    interface View : BaseContract.BaseView{



    }

    /**
     * @author LDD
     * @Date   2018/5/11 下午12:57
     * @From   MemberPasswordContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/11 下午1:17
         * @Note   修改密码
         * @param  password 新密码
         * @param  vcode    图片验证码
         */
        fun editPassword(password:String,vcode:String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/11 下午1:25
         * @Note   注册
         * @param  password 用户密码
         * @param  mobile   手机号
         */
        fun registerUser(password:String,mobile:String)

        fun registerConnectUser(type :String,uoionId:String,password:String,mobile:String,vcode:String)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/11 下午1:26
         * @Note   查找密码
         * @param  vcode 图片验证码
         * @param  password 密码
         */
        fun findPassword(password:String,vcode:String)

    }

}