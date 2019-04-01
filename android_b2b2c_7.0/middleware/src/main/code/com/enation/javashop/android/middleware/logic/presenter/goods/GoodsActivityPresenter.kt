package com.enation.javashop.android.middleware.logic.presenter.goods

import android.app.Activity
import android.util.Log
import com.alipay.sdk.auth.AlipaySDK
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsActivityContract
import com.enation.javashop.android.middleware.model.GoodsViewModel
import com.enation.javashop.connectview.logic.UmengShare
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/23 下午3:26
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Note   商品Activity逻辑加载器
 */
class GoodsActivityPresenter @Inject constructor() : RxPresenter<GoodsActivityContract.View>() , GoodsActivityContract.Presenter {

    /**
     * @Name  cartApi
     * @Type  CartApi
     * @Note  购物车API
     */
    @Inject
    protected lateinit var cartApi: CartApi

    /**
     * @Name  goodsApi
     * @Type  GoodsApi
     * @Note  商品Api
     */
    @Inject
    protected lateinit var goodsApi: GoodsApi

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi



    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is GoodsViewModel -> {
                    providerView().initFragment(data)
                }
                is Boolean ->{
                    providerView().collect(data)
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
     * @From   GoodsAcivityPresenter
     * @Date   2018/3/23 下午3:26
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsAcivityPresenter
     * @Date   2018/3/23 下午3:27
     * @Note   加载商品
     * @param  goodsId 商品ID
     */
    override fun loadGoods(goodsId: Int) {

        goodsApi.getGoodsDetail(goodsId).map { responseBody ->
            var goodsDetail = JSONObject(responseBody.getJsonString())
            return@map GoodsViewModel.map(goodsDetail)
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    /**
     * @author LDD
     * @From   GoodsAcivityPresenter
     * @Date   2018/3/23 下午3:31
     * @Note   关注收藏商品
     * @param  goodsId 商品ID
     */
    override fun collectGoods(goodsId: Int,collect :Boolean) {
        if (collect){
            memberApi.addCollection(goodsId)
                    .compose(ThreadFromUtils.defaultSchedulers())
                    .map { return@map true }
                    .subscribe(observer)
        }else{
            memberApi.deleteCollection(goodsId)
                    .compose(ThreadFromUtils.defaultSchedulers())
                    .map { return@map false }
                    .subscribe(observer)
        }
    }

    /**
     * @author LDD
     * @From   GoodsAcivityPresenter
     * @Date   2018/3/23 下午3:31
     * @Note   添加购物车
     * @param  skuId 商品Skuid
     * @param  num   商品数量
     */
    override fun addToCart(skuId: Int, num: Int) {

    }

    /**
     * @author LDD
     * @From   GoodsAcivityPresenter
     * @Date   2018/3/23 下午3:22
     * @Note   分享商品
     * @param  activity    调用Activity
     * @param  url         分享URL
     * @param  image       商品图片
     * @param  title       标题
     * @param  description 商品标题
     */
    override fun shareGoods(activity: Activity, url: String, image: String, title: String, description: String) {
        AppTool.Image.urlToBitmap(image).compose(ThreadFromUtils.defaultSchedulers()).subscribe({ image ->
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