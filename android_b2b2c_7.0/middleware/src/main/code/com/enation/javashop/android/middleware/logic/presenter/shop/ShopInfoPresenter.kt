package com.enation.javashop.android.middleware.logic.presenter.shop

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.R
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.ShopApi
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopInfoActivityContract
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.connectview.logic.UmengShare
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject


/**
 * @author LDD
 * @Date   2018/4/13 下午4:47
 * @From   com.enation.javashop.android.middleware.logic.presenter.shop
 * @Note   店铺信息逻辑控制器
 */
class ShopInfoPresenter @Inject constructor() : RxPresenter<ShopInfoActivityContract.View>(), ShopInfoActivityContract.Presenter {


    /**
     * @Name  shopApi
     * @Type  ShopApi
     * @Note  店铺API
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
                    providerView().initShopInfo(data)
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
     * @From   ShopInfoPresenter
     * @Date   2018/4/24 下午3:36
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopInfoPresenter
     * @Date   2018/4/24 下午3:37
     * @Note   加载店铺信息
     * @param  shopId 店铺ID
     */
    override fun loadShopInfo(shopId: Int) {
        val shop = shopApi.getShop(shopId)
        var collect = shopApi.getIsCollectionShop(shopId).onErrorReturn { t-> ResponseBody.create(null, "{\"message\":false}") }
        val goodsNum = goodsApi.getGoodsNumForShop(shopId)

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
     * @From   ShopInfoPresenter
     * @Date   2018/4/24 下午3:37
     * @Note   构建二维码
     * @param  shopId 店铺Id
     */
    override fun buildQrCode(shopId: Int) {
        providerView().start()
        Observable.create<Bitmap> { observableEmitter ->
            val logo = BitmapFactory.decodeResource(BaseApplication.appContext.resources, R.mipmap.launcher)
            val qrCode = EcodeHelper().createQRImage(JavaShopConfigCenter.INSTANCE.WAP_SELLER_URL+"$shopId", 200, logo)
            if (qrCode != null) {
                observableEmitter.onNext(qrCode)
            } else {
                observableEmitter.onError(Throwable("创建二维码失败，请重试！"))
            }
        }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe({ value ->
                    providerView().showQrCode(value)
                    providerView().complete()
                }, { throwable ->
                    providerView().onError(throwable.localizedMessage)
                }).joinManager(this)
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