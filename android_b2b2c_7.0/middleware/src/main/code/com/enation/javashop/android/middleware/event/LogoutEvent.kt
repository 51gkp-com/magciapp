package com.enation.javashop.android.middleware.event

import com.enation.javashop.android.middleware.api.MemberState

/**
 * @author LDD
 * @Date   2018/8/13 下午6:18
 * @From   com.enation.javashop.android.middleware.event
 * @Note   登出事件
 */
class LogoutEvent{
    constructor(){
        MemberState.manager.clearMember()
    }
}