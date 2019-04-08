package com.enation.javashop.android.middleware.api

import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 *  会员状态管理器
 */
class MemberState {

    companion object {

        /**
         * @Name  com.enation.javashop.android.middleware.api.MemberManager.Companion.instance
         * @Type  MemberManager
         * @Note  单例
         */
        val manager = MemberState()

    }

    /**
     * @Name  member
     * @Type  MemberViewModel
     * @Note  会员信息
     */
    private var member : MemberViewModel? = null

    /**
     * @author LDD
     * @From   MemberManager
     * @Date   2018/8/13 下午5:23
     * @Note   更新会员信息
     * @param  member 会员信息
     */
    fun updateMember(member :MemberViewModel){
        this.member = member
    }

    /**
     * @author LDD
     * @From   MemberManager
     * @Date   2018/8/13 下午5:24
     * @Note   清空会员信息
     */
    fun clearMember(){
        this.member = null
    }

    /**
     * @author LDD
     * @From   MemberState
     * @Date   2018/8/13 下午5:31
     * @Note   获取会员信息
     */
    fun info() : MemberViewModel? {
        return member
    }

    /**
     * @author LDD
     * @From   MemberManager
     * @Date   2018/8/13 下午5:24
     * @Note   获取登录状态
     */
    fun getLoginState() : Boolean {
        return member != null
    }

}