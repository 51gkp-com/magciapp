package com.enation.javashop.android.middleware.logic.presenter.order

import android.databinding.ObservableField
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.api.TradeApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/23 上午9:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单创建逻辑控制器
 */
class OrderCreatePresenter @Inject constructor() :RxPresenter<OrderCreateContract.View>(),OrderCreateContract.Presenter {

    @Inject
    protected lateinit var tradeApi :TradeApi

    @Inject
    protected lateinit var memberApi :MemberApi

    @Inject
    protected lateinit var cartApi :CartApi

    private val observer = object  :ConnectionObserver<Any>() {

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {

            if (result is OrderCreateModel){
                providerView().renderUi(result)
            }
            if (result is OrderPayModel){
                providerView().createOrderResult(result)
            }
            if (result is String){
                providerView().complete(result)
            }else{
                providerView().complete("")
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
     * @From   OrderCreatePresenter
     * @Date   2018/5/23 上午9:45
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    override fun createOrder() {
        tradeApi.create()
                .map { responseBody ->
                    val json = responseBody.toJsonObject()
                    var result = OrderPayModel()

                    result.sn = json.valueString("trade_sn")
                    result.tradeType = TradeType.Trade
                    result.payPrice = json.valueJsonObject("price_detail").valueDouble("total_price")
                    if (json.optString( "payment_type",  "ONLINE") == "ONLINE") {
                        result.paymentType = GlobalState.PAY_ONLINE
                    } else {
                        result.paymentType = GlobalState.PAY_COD
                    }
                    return@map result
                }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun loadData() {

        val goods = cartApi.getCartData("checked")

        val params = tradeApi.getParam()

        lateinit var info : OrderCreateModel

        Observable.zip(goods,params, BiFunction<ResponseBody,ResponseBody,OrderCreateModel> { goods, params ->

            var priceData = OrderCreatePriceViewModel.map(goods.toJsonObject().valueJsonObject("total_price"))

            var paramsViewModel = OrderCreateParamsViewModel.map(params.toJsonObject())

            var shopArray = ArrayList<OrderShopModel>()
            var couponArray = ArrayList<CouponViewModel>()
            var shopIds= ArrayList<Int>()

            goods.toJsonObject().valueJsonArray("cart_list").arrayObjects().forEach { item ->
                var shop = OrderShopModel()
                shop.shopName = item.valueString("seller_name")
                shop.promotionTitle = item.valueString("promotion_notice")
                shopIds.add(item.valueInt("seller_id"))

                item.valueJsonArray("sku_list").arrayObjects().forEach { goods ->
                    var goodsVo = CartGoodsItemViewModel.map(goods)
                    val promotionJson = goods.valueJsonArray("group_list")
                    if (promotionJson.length() > 0){
                        promotionJson.arrayObjects().forEach { item ->
                            if (item.valueInt("is_check") == 1){
                                goodsVo.groupPromotionId = item.valueInt("activity_id")
                            }
                        }
                    }
                    shop.cartGoods.add(goodsVo)
                }

                item.valueJsonArray("coupon_list").arrayObjects().forEach { dic ->
                    if (dic.valueInt("enable") == 1){
                        var coupon = CouponViewModel(dic.valueDouble("amount"),0.0,"",false,false,false,1,"",1,"超级大促销",isSelect = ObservableField(dic.optInt("selected",0)))
                        couponArray.add(coupon)
                    }
                }

                item.valueJsonArray("gift_list").arrayObjects().forEach { giftJson ->
                    shop.gift.add(Gift(giftJson.valueString("gift_name"),giftJson.valueDouble("gift_price"),giftJson.valueString("gift_img"),0))
                }

                item.valueJsonArray("gift_coupon_list").arrayObjects().forEach {dic ->
                    var coupon = CouponViewModel(dic.valueDouble("amount"),dic.valueDouble("coupon_threshold_price"),"",false,false,false,dic.valueInt("member_coupon_id"),"",dic.valueInt("coupon_id"),"超级大促销",isSelect = ObservableField(dic.optInt("selected",0)))
                    shop.coupon.add(coupon)
                }
                shopArray.add(shop)
            }
            info = OrderCreateModel(priceData,paramsViewModel,shopArray,null,shopIds,couponArray)
            return@BiFunction info
        }).compose(ThreadFromUtils.all_io())
                .flatMap { result ->
                    return@flatMap memberApi.getAddressDetail(result.params.addressId)
                }.map { address ->
                    if (!address.isError && address.response()!!.code() == 200){
                        val jsonObject = address.response()!!.body()!!.toJsonObject()

                        var isDefault = (jsonObject.valueInt("def_addr") == 1).judge(true,false)

                        var item = MemberAddressViewModel(jsonObject.valueInt("addr_id"),
                                jsonObject.valueString("name"),
                                jsonObject.valueString("mobile"),
                                jsonObject.valueString("addr"),
                                jsonObject.valueInt("province_id"),
                                jsonObject.valueString("province"),
                                jsonObject.valueInt("city_id"),
                                jsonObject.valueString("city"),
                                jsonObject.valueInt("county_id"),
                                jsonObject.valueString("county"),
                                jsonObject.valueInt("town_id"),
                                jsonObject.valueString("town"),
                                jsonObject.valueString("ship_address_name"),
                                isDefault
                        )
                        info.address = item
                    }
                    return@map info
                }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }


}