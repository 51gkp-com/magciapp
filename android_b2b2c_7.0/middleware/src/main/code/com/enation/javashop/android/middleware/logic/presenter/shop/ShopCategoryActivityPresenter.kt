package com.enation.javashop.android.middleware.logic.presenter.shop

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CategoryApi
import com.enation.javashop.android.middleware.api.ShopApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopCategoryActivityContract
import com.enation.javashop.android.middleware.model.ShopCategoryViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/12 上午9:32
 * @From   com.enation.javashop.android.middleware.logic.presenter.shop
 * @Note   店铺分类页面逻辑控制
 */
class ShopCategoryActivityPresenter @Inject constructor() :RxPresenter<ShopCategoryActivityContract.View>() ,ShopCategoryActivityContract.Presenter {


    /**
     * @Name  categoryApi
     * @Type  CategoryApi
     * @Note  分类API
     */
    @Inject
    protected lateinit var shopApi : ShopApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ArrayList<*> -> {
                    providerView().initCategory(data as ArrayList<ShopCategoryViewModel>)
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }



    /**
     * @author LDD
     * @From   ShopCategoryActivityPresenter
     * @Date   2018/4/12 上午9:38
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivityPresenter
     * @Date   2018/4/12 上午9:38
     * @Note   加载分类
     * @param  shopId 店铺ID
     */
    override fun loadCategory(shopId: Int) {

        shopApi.getShopCats(shopId).map { responseBody ->
            var jsonArray = JSONArray(responseBody.getJsonString())

            var jsonShopCat = ArrayList<ShopCategoryViewModel>()

            for (i in 0..(jsonArray.length() - 1)){
                val jsonObject = jsonArray.getJSONObject(i)
                val jsonChildrenArray = JSONArray(jsonObject.get("children").toString())
                var childrenView = ArrayList<ShopCategoryViewModel>()

                //判断是否有子
                if(jsonChildrenArray != null && jsonChildrenArray.length() > 0 ){

                    for (i in 0..(jsonChildrenArray.length() - 1)){
                        val jsonChildren = jsonChildrenArray.getJSONObject(i)
                        childrenView.add(ShopCategoryViewModel(
                                jsonChildren.valueInt("shop_cat_id"),
                                jsonChildren.valueString("shop_cat_name"),
                                null
                        ))

                    }

                }

                jsonShopCat.add(ShopCategoryViewModel(
                        jsonObject.valueInt("shop_cat_id"),
                        jsonObject.valueString("shop_cat_name"),
                        childrenView
                ))
            }

            return@map jsonShopCat
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}