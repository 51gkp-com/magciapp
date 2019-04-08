package com.enation.javashop.android.middleware.logic.presenter.promotion

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.promotion.WebContract
import javax.inject.Inject

/**
 * Created by LDD on 2018/10/29.
 */
class WebPresenter  @Inject constructor() :RxPresenter<WebContract.View>(),WebContract.Presenter {

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}