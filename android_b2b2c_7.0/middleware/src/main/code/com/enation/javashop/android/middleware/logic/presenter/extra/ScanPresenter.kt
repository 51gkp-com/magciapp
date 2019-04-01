package com.enation.javashop.android.middleware.logic.presenter.extra

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.extra.ScanContract
import javax.inject.Inject

/**
 * Created by LDD on 2018/11/5.
 */
class ScanPresenter  @Inject constructor() : RxPresenter<ScanContract.View>(),ScanContract.Presenter {


    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}