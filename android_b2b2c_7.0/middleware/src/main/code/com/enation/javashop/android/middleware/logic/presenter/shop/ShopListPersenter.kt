package com.enation.javashop.android.middleware.logic.presenter.shop

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.arrayObjects
import com.enation.javashop.android.lib.utils.toJsonObject
import com.enation.javashop.android.lib.utils.valueJsonArray
import com.enation.javashop.android.middleware.api.ShopApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopListContract
import com.enation.javashop.android.middleware.model.ShopItem
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ShopListPersenter @Inject constructor() : RxPresenter<ShopListContract.View>(), ShopListContract.Presenter {

    @Inject
    protected lateinit var shopApi: ShopApi

    private val observer = object :ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            providerView().complete("")
            providerView().render(result as ArrayList<ShopItem>)
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun loadData(keyWord: String, page: Int) {
        shopApi.getShopList(page,10,keyWord)
                .map {
                    var shop = ArrayList<ShopItem>()
                    it.toJsonObject().valueJsonArray("data").arrayObjects().forEach({ dic ->
                        shop.add(ShopItem.map( dic))
                })
                    return@map shop
                }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}