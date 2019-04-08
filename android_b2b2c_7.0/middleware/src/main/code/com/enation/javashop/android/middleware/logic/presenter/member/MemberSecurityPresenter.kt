package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberSecurityContract
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/9 上午11:12
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员安全逻辑控制器
 */
class MemberSecurityPresenter @Inject constructor() :RxPresenter<MemberSecurityContract.View>() ,MemberSecurityContract.Presenter {

    /**
     * @author LDD
     * @From   MemberSecurityPresenter
     * @Date   2018/5/9 上午11:14
     * @Note   验证绑定手机号状态
     */
    override fun load() {
        MemberState.manager.info()?.then {
            providerView().renderUI(it)
        }
    }

    /**
     * @author LDD
     * @From   MemberSecurityPresenter
     * @Date   2018/5/9 上午11:14
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}