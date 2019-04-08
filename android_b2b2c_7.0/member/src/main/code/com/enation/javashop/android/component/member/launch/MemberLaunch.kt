package com.enation.javashop.android.component.member.launch

import com.enation.javashop.android.component.member.di.MemberComponent
import com.enation.javashop.android.component.member.di.DaggerMemberComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/1/22 下午2:44
 * @From   com.enation.javashop.android.component.member.launch
 * @Note   Member模块启动类 代替Application 在壳工程Application中反射调用
 */
@Router(path = "/member/launch")
class MemberLaunch :BaseLaunch() {

    companion object {
        lateinit var component: MemberComponent
    }

    override fun moduleInit() {
        component = DaggerMemberComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }


}