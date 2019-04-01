package com.enation.javashop.android.middleware.logic.presenter.goods

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.toJsonArray
import com.enation.javashop.android.lib.utils.valueString
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.ExtraApi
import com.enation.javashop.android.middleware.cache.HsitoryCache
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsSearchContract
import com.enation.javashop.android.middleware.logic.contract.goods.SearchContract
import com.enation.javashop.android.middleware.model.HistoryModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author LDD
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Date   2018/10/15 上午8:44
 * @Note   搜索逻辑控制器
 */
class SearchPresenter  @Inject constructor() : RxPresenter<SearchContract.View>(), SearchContract.Presenter  {

    @Inject
    protected lateinit var extraApi: ExtraApi

    private val cache = HsitoryCache()

    private val observer = object :ConnectionObserver<ArrayList<Any>>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<Any>, connectionQuality: ConnectionQuality) {
            providerView().render(result)
            providerView().complete("")
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    override fun loadData() {

            val history = Observable.create<ArrayList<HistoryModel>> {
            it.onNext(cache.get())
        }

        val hotKey = extraApi.getKeyword(10)

        Observable.zip(hotKey,history, BiFunction<ResponseBody,ArrayList<HistoryModel>,ArrayList<Any>> { t1, t2 ->
            val hotKeys = ArrayList<String>()

            val hotKeyRespone = t1.toJsonArray()

            if (hotKeyRespone.length() > 0){

                for (i in 0..(hotKeyRespone.length() - 1)) {
                    var key = hotKeyRespone.getJSONObject(i).valueString("hot_name")
                    hotKeys.add(key)
                }
            }

            val result = ArrayList<Any>()

            result.add(hotKeys)

            if (t2.count() > 0){
                result.add(0)
                result.add(t2)
                result.add(1)
            }
            return@BiFunction result
        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    override fun clearHistory() {
        cache.clear()
        loadData()
    }

    override fun addHistory(item: HistoryModel) {
        cache.add(item)
        loadData()
    }

    override fun deleteHistory(item: HistoryModel) {
        cache.delete(item)
        loadData()
    }

}