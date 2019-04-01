package com.enation.javashop.android.middleware.logic.presenter.shop

import android.app.Activity
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.ShopApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopActivityContract
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.connectview.logic.UmengShare
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import io.reactivex.functions.Function3
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/8 下午3:55
 * @From   com.enation.javashop.android.middleware.logic.presenter.shop
 * @Note   店铺页面逻辑控制器
 */
class ShopActivityPersenter @Inject constructor() : RxPresenter<ShopActivityContract.View>(), ShopActivityContract.Presenter {

    /**
     * @Name  shopApi
     * @Type  ShopApi
     * @Note  店铺Api
     */
    @Inject
    protected lateinit var shopApi: ShopApi

    @Inject
    protected lateinit var goodsApi : GoodsApi

    @Inject
    protected lateinit var memberApi: MemberApi

    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ShopViewModel -> {
                    providerView().initShop(data as ShopViewModel)
                    providerView().complete()
                }
                is String ->{
                    providerView().complete(data)
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
     * @From   ShopActivityPersenter
     * @Date   2018/4/8 下午3:58
     * @Note   绑定Dagger
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From    ShopActivityPersenter
     * @Date   2018/4/8 下午4:01
     * @Note   加载店铺信息
     * @param  storeId 店铺ID
     */
    override fun loadShopInfo(storeId: Int) {
        val shop = shopApi.getShop(storeId)
        var collect = shopApi.getIsCollectionShop(storeId).onErrorReturn { t-> ResponseBody.create(null, "{\"message\":false}") }
        val goodsNum = goodsApi.getGoodsNumForShop(storeId)

        Observable.zip(shop, goodsNum, collect, Function3<ResponseBody, ResponseBody, ResponseBody, ShopViewModel>{
            shopResponse, goodsNumResponse, collectResponse ->

            var shopJsonObject = JSONObject(shopResponse.getJsonString())
            val shopViewModel = ShopViewModel.map(shopJsonObject)

            val numJsonObject = JSONObject(goodsNumResponse.getJsonString())
            shopViewModel.hotNum = numJsonObject.optInt("hot_num")
            shopViewModel.new_Num = numJsonObject.optInt("new_num")
            shopViewModel.recommendNum = numJsonObject.optInt("recommend_num")

            if(collectResponse != null){
                var collectJsonObject = JSONObject(collectResponse.getJsonString())
                if(!collectJsonObject.has("code") && collectJsonObject.has("message")){
                    shopViewModel.favorited = collectJsonObject.optBoolean("message")
                }
            }

            shopViewModel
        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   ShopActivityPersenter
     * @Date   2018/4/8 下午4:01
     * @Note   关注店铺
     * @param  shopId 店铺ID
     * @param  isCollect 是否关注
     */
    override fun collectShop(shopId: Int, isCollect: Boolean) {
        if (isCollect){
            memberApi.addCollectionShop(shopId)
                    .map { "关注成功" }
                    .compose(ThreadFromUtils.defaultSchedulers())
                    .subscribe(observer)
        }else{
            memberApi.deleteCollectionShop(shopId)
                    .map { "取消关注成功" }
                    .compose(ThreadFromUtils.defaultSchedulers())
                    .subscribe(observer)
        }
    }


    override fun share(activity: Activity, url: String, image: String, title: String, description: String) {
        providerView().start()
        AppTool.Image.urlToBitmap(image).compose(ThreadFromUtils.defaultSchedulers()).subscribe({ image ->
            providerView().complete()
            UmengShare.Init(activity.weak().get())
                    .web(url)
                    .setWebTitle(title)
                    .setWebImage(image)
                    .setWebDescription(description)
                    .webShare()
        },{
            providerView().onError("分享失败")
        }).joinManager(this)
    }

}