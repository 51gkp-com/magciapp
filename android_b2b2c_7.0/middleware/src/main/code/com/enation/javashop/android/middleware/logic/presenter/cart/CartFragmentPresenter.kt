package com.enation.javashop.android.middleware.logic.presenter.cart

import android.util.Log
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.PromotionApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.cart.CartFragmentContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/2/9 上午10:21
 * @From   com.enation.javashop.android.middleware.logic.presenter.cart
 * @Note   购物车逻辑控制器
 */
class CartFragmentPresenter @Inject constructor() : RxPresenter<CartFragmentContract.View>(),CartFragmentContract.Presenter {

    @Inject
    protected lateinit var cartApi : CartApi

    @Inject
    protected lateinit var promotionApi:PromotionApi

    @Inject
    protected lateinit var memberApi : MemberApi

    private val observer = object  :ConnectionObserver<Any>() {

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if (result is ArrayList<*>){
                if (result.count() > 0){
                    if (result[0] is Double){
                        providerView().showCartView(result[3] as List<Any>, result[0] as Double,result[1] as Double,result[2] as Double)
                    }else if (result[0] is String){
                        providerView().complete("暂无优惠券")
                    }else{
                        providerView().showCoupon(result as ArrayList<CouponViewModel>)
                    }
                }
            }
            if (result is Boolean){
                if (result){
                    loadCartData()
                }
            }
            if (result is String){
                if (result == "切换成功" || result == "取消成功"){
                    loadCartData()
                }
                providerView().complete(result)
            }else{
                providerView().complete("")
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            if (error.customMessage.contains("登录")){
                providerView().showCartView(emptyList(), 0.00,0.00,0.00)
            }
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    override fun loadCartData() {
       cartApi.getCartData("all").map(Function<ResponseBody,ArrayList<Any>> { t1 ->

            var result = ArrayList<Any>()

            val price = t1.toJsonObject().valueJsonObject("total_price").valueDouble("total_price")
            val cashPrice = t1.toJsonObject().valueJsonObject("total_price").valueDouble("cash_back")
            val orgPrice = t1.toJsonObject().valueJsonObject("total_price").valueDouble("original_price")

            result.add(price)
            result.add(cashPrice)
            result.add(orgPrice)

            val cartList = ArrayList<Any>()

            val cartJsonArray = t1.toJsonObject().valueJsonArray("cart_list")
            for (i in 0..(cartJsonArray.length() - 1)){

                val shopJson = cartJsonArray.getJSONObject(i)

                var cartShop = CartShopItemViewModel.map(shopJson)

                var shopPromotionGoodsList = shopJson.valueJsonArray("sku_list")

                var groupString = shopJson.valueString("promotion_notice")

                var promotionGoods =  ArrayList<Any>()

                var nomalGoods =  ArrayList<Any>()


                cartList.add(cartShop)

                for (i in 0..(shopPromotionGoodsList.length() - 1)) {

                    val itemJson = shopPromotionGoodsList.getJSONObject(i)

                    val promotionJson = itemJson.valueJsonArray("group_list")

                    var goods = CartGoodsItemViewModel.map(itemJson)

                    var promotionId : Int = -1
                    if (promotionJson.length() > 0){
                        promotionJson.arrayObjects().forEach { item ->
                            if (item.valueInt("is_check") == 1){
                                 promotionId = item.valueInt("activity_id")
                            }
                        }
                    }
                    if (promotionId > 0){
                        goods.groupPromotionId = promotionId
                        promotionGoods.add(goods)
                    }else{
                        nomalGoods.add(goods)
                    }

                }

                if (promotionGoods.count() > 0) {
                    cartList.add(groupString)
                    cartList.addAll(promotionGoods)
                    cartList.add("Holder")
                }

                cartList.addAll(nomalGoods)
                cartList.add("Holder")

            }
            result.add(cartList)

            return@Function result
        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    override fun editItem(productId: Int, checked: Boolean?, num: Int?) {
        var check :Int? = null
        if (checked != null){
            check = checked.judge(1,0)
        }
        cartApi.updateItemState("$productId",check,num)
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { return@map true }
                .subscribe(observer)
    }

    override fun editPromotion(sellerId: Int, skuId: Int, actId: Int, actType: String) {
        var rxRequest :Observable<ResponseBody> = promotionApi.changeActivity(sellerId,skuId,actId,actType)
        if (actId <= 0){
          rxRequest = promotionApi.deleteActivity(sellerId,skuId)
        }
        rxRequest.compose(ThreadFromUtils.defaultSchedulers())
                .map { return@map if(actId > 0){"切换成功"}else{"取消成功"} }
                .subscribe(observer)
    }

    override fun shopCheck(shopId: Int, checked: Boolean) {
        cartApi.updateSellerGoods(shopId,checked.judge(1,0))
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { return@map true }
                .subscribe(observer)
    }

    override fun deleteGoods(productId: Int) {
        cartApi.delete("$productId")
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { return@map true }
                .subscribe(observer)
    }

    override fun collectionGoods(goodsId: Int) {
        memberApi.addCollection(goodsId)
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { return@map "收藏成功" }
                .subscribe(observer)
    }

    override fun allCheck(check: Boolean) {
        cartApi.setAllCheck(check.judge(1,0))
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { true }
                .subscribe(observer)
    }

    override fun loadCoupon(shopId: Int) {
        promotionApi.getSellerCoupon(shopId)
                .map {
                    var couponJson = it.toJsonArray()
                    var couponList = ArrayList<CouponViewModel>()

                    //遍历优惠券数据
                    for (i in 0..(couponJson.length() - 1)){
                        val jsonObject = couponJson.getJSONObject(i)

                        couponList.add(CouponViewModel(
                                jsonObject.valueDouble("coupon_price"),
                                jsonObject.valueDouble("coupon_threshold_price"),
                                jsonObject.valueString("seller_name"),
                                false,
                                false,
                                false,
                                jsonObject.valueInt("coupon_id"),
                                jsonObject.valueDate("start_time")+"-"+jsonObject.valueDate("end_time"),
                                jsonObject.valueInt("seller_id"),
                                jsonObject.valueString("title"),
                                isGet = true
                        ))
                    }
                    return@map couponList
                }.map {
                    if (it.isEmpty()){
                        return@map ArrayList<String>().then {
                            it.add("暂无优惠券")
                        }
                    }else{
                        return@map it
                    }
                }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun collectionGoods(goodsIds: ArrayList<Int>) {

    }

    override fun deleteGoods(productIds: ArrayList<Int>) {

    }

    override fun getCoupon(couponId: Int) {
        memberApi.receiveCoupon("$couponId")
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { "领取成功" }
                .subscribe(observer)
    }

    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}