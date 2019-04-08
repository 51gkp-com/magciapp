package com.enation.javashop.android.component.member.vm

import android.databinding.ObservableField

/**
 * @author LDD
 * @Date   2018/5/14 上午9:30
 * @From   com.enation.javashop.android.component.member.vm
 * @Note   登录页面ViewModel
 */
data class MemberLoginViewModel(var loginType :Boolean){

    /**
     * @Name  loginType
     * @Type  ObservableField<Boolean>
     * @Note  登录类型
     */
    val loginTypeObser  = ObservableField(loginType)

}