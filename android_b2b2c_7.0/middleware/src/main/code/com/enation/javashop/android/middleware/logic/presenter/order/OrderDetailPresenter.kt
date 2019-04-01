package com.enation.javashop.android.middleware.logic.presenter.order

import android.databinding.ObservableField
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.event.CartDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderDetailContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/18 下午4:20
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   ...
 */
class OrderDetailPresenter @Inject constructor() :RxPresenter<OrderDetailContract.View>() , OrderDetailContract.Presenter {


    /**
     * @Note  订单API
     */
    @Inject
    protected lateinit var orderApi : OrderApi

    /**
     * @Note  商品API
     */
    @Inject
    protected lateinit var goodsApi : GoodsApi

    /**
     * @Note  商品API
     */
    @Inject
    protected lateinit var cartApi : CartApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ArrayList<*> -> {
                    providerView().renderOrder(data as ArrayList<Any>)
                    providerView().complete("")
                }
                is String ->{
                    providerView().complete(data)
                    providerView().notification()
                }
                is Int ->{
                    getEventCenter().post(CartDataChange())
                    providerView().complete("加入购物车成功！")
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
     * @From   OrderDetailPresenter
     * @Date   2018/4/18 下午4:23
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderDetailPresenter
     * @Date   2018/4/18 下午4:25
     * @Note   加载订单详细
     * @param  orderSn 订单号
     */
    override fun loadOrder(orderSn: String) {

        var orderDetailViewModel = OrderDetailViewModel()


        val orderApi =  this.orderApi.getOrderDetail(orderSn).flatMap { responseBody ->
            var jsonResult = responseBody.getJsonString()
            var order = JSONObject(jsonResult)
            //订单可操作状态
            var orderOperate = JSONObject(order.get("order_operate_allowable_vo").toString())
            var orderAction =  OrderActionModel(
                    orderOperate.valueBool("allow_cancel"),
                    orderOperate.valueBool("allow_confirm"),
                    orderOperate.valueBool("allow_pay"),
                    orderOperate.valueBool("allow_ship"),
                    orderOperate.valueBool("allow_rog"),
                    orderOperate.valueBool("allow_comment"),
                    orderOperate.valueBool("allow_complete"),
                    orderOperate.valueBool("allow_apply_service"),
                    orderOperate.valueBool("allow_service_cancel"),
                    order.valueString("ship_status") != "SHIP_NO"
            )
            var giftList = ArrayList<Gift>()
            var coupons = ArrayList<CouponViewModel>()
            for (giftJson in order.valueJsonArray("gift_list").arrayObjects()) {
                giftList.add(Gift(giftJson.valueString("gift_name"),giftJson.valueDouble("gift_price"),giftJson.valueString("gift_img"),0))
            }

            if(order.get("gift_coupon") is JSONObject){
                val dic = order.valueJsonObject("gift_coupon")
                var coupon = CouponViewModel(dic.valueDouble("amount"),dic.valueDouble("coupon_threshold_price"),"",false,false,false,dic.valueInt("member_coupon_id"),"",dic.valueInt("coupon_id"),"超级大促销",isSelect = ObservableField(dic.optInt("selected",0)))
                coupons.add(coupon)
            }else{
                for (dic in order.valueJsonArray("gift_coupon").arrayObjects()) {
                    var coupon = CouponViewModel(dic.valueDouble("amount"),dic.valueDouble("coupon_threshold_price"),"",false,false,false,dic.valueInt("member_coupon_id"),"",dic.valueInt("coupon_id"),"超级大促销",isSelect = ObservableField(dic.optInt("selected",0)))
                    coupons.add(coupon)
                }
            }

            orderDetailViewModel.gifts = giftList
            orderDetailViewModel.coupons = coupons
            //商品项列表
            var goodsList = ArrayList<OrderDetailGoodsViewModel>()
            var orderSkuList = JSONArray(order.get("order_sku_list").toString())
            for (i in 0..(orderSkuList.length() - 1)){
                val skuObject = orderSkuList.getJSONObject(i)

                //商品可操作状态
                var goodsOperate = skuObject.valueJsonObject("goods_operate_allowable_vo")

                var spec = ""

                skuObject.valueJsonArray("spec_list").arrayObjects().forEach {
                    spec += "${it.valueString("spec_name")},${it.valueString("spec_value")} "
                }

                if (spec.isEmpty()){
                    spec = "默认"
                }

                var tags = ArrayList<String>()

                for (arrayObject in skuObject.valueJsonArray("group_list").arrayObjects()) {
                    if(arrayObject.valueInt("is_check") == 1){
                        tags.add("满优惠")
                    }
                }

                for (arrayObject in skuObject.valueJsonArray("single_list").arrayObjects()) {
                    if(arrayObject.valueInt("is_check") == 1){
                        when(arrayObject.valueString("promotion_type")){
                            "MINUS" -> {
                                tags.add("立减")
                            }
                            "GROUPBUY" -> {
                                tags.add("团购")
                            }
                            "EXCHANGE" -> {
                                tags.add("积分商城")
                            }
                            "HALF_PRICE" -> {
                                tags.add("第二件半价")
                            }
                            "SECKILL" -> {
                                tags.add("秒杀")
                            }
                        }
                    }
                }

                goodsList.add(OrderDetailGoodsViewModel(
                        skuObject.valueString("name"),
                        skuObject.valueDouble("purchase_price"),
                        skuObject.valueString("service_status"),
                        skuObject.valueString("goods_image"),
                        spec,
                        skuObject.valueInt("goods_id"),
                        skuObject.valueInt("sku_id"),
                        tags,
                        GoodsActionModel(goodsOperate.valueBool("allow_apply_service"))
                ))
            }

            val receipt = order.valueJsonObject("receipt_history")

            var receiptviewModel :ReceiptViewModel? = null
            if (receipt.length() > 0){
                receiptviewModel = ReceiptViewModel(
                        0,
                        receipt.valueString("tax_no").bindingParams(),
                        receipt.valueString("receipt_content"),
                        receipt.valueString("receipt_title").bindingParams(),
                        receipt.valueString("receipt_type")
                )
            }

            //设置参数值
            orderDetailViewModel.orderSn = order.valueString("sn")
            orderDetailViewModel.orderStatus = order.valueString("order_status_text")
            orderDetailViewModel.consigneeName = order.valueString("ship_name")
            orderDetailViewModel.consigneeMobile = order.valueString("ship_mobile")
            orderDetailViewModel.consigneeAddress = order.valueString("ship_province")+
                    order.valueString("ship_city")+
                    order.valueString("ship_county")+
                    order.valueString("ship_town")+
                    order.valueString("ship_addr")
            orderDetailViewModel.sellerId = order.valueInt("seller_id")
            orderDetailViewModel.sellerName = order.valueString("seller_name")
            orderDetailViewModel.createTime = order.valueDate("create_time")
            orderDetailViewModel.payTime = order.valueDate("payment_time")
            orderDetailViewModel.payType = order.valueString("payment_type")
            orderDetailViewModel.logiName = order.valueString("logi_name")
            orderDetailViewModel.serviceStatus = order.valueString("service_status")
            orderDetailViewModel.goodsPrice = order.valueDouble("goods_price")
            orderDetailViewModel.shipPrice = order.valueDouble("shipping_price")
            orderDetailViewModel.discountPrice = order.valueDouble("cash_back")
            orderDetailViewModel.couponPrice = order.valueDouble("coupon_price")
            orderDetailViewModel.pointPrice = order.valueInt("use_point")
            orderDetailViewModel.needPayPrice = order.valueDouble("need_pay_money")
            orderDetailViewModel.receiptViewModel = receiptviewModel
            orderDetailViewModel.orderActionModel = orderAction
            orderDetailViewModel.goodsList = goodsList
            orderDetailViewModel.logId = order.valueInt("logi_id")
            orderDetailViewModel.shipNo = order.valueString("ship_no")
            return@flatMap orderApi.express(order.valueInt("logi_id"),order.valueString("ship_no"))
        }.map { express ->
            if (!express.isError && express.response()!!.code() == 200){
                var expressObject = express.response()!!.body()!!.toJsonObject()
                if (expressObject.length() > 0){
                    var dataArray = expressObject.valueJsonArray("data")
                    var expressObj =  dataArray.getJSONObject(0)
                    var context = expressObj.valueString("context")
                    var time = expressObj.valueString("time")
                    orderDetailViewModel.lastModify = time
                    orderDetailViewModel.lastExpressText = context
                }
            }
            return@map orderDetailViewModel

        }


    val goodsNet = goodsApi.searchGoodsList(1,10,HashMap<String,Any>()).map { responseBody ->

            var jsonPage = JSONObject(responseBody.getJsonString())

            var jsonArray = JSONArray(jsonPage.get("data").toString())

            var goodsItemList = ArrayList<RecommendGoodsViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                goodsItemList.add(RecommendGoodsViewModel.map(jsonObject))
            }

            return@map goodsItemList

        }

        Observable.zip(orderApi,goodsNet, BiFunction<OrderDetailViewModel,ArrayList<RecommendGoodsViewModel>,ArrayList<Any>> { t1, t2 ->

            return@BiFunction arrayListOf<Any>(t1,t2)
        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    override fun rog(orderSn: String) {
        orderApi.rog(orderSn).map { "确认收货成功" }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun cancel(orderSn: String,reson:String) {
        orderApi.cancel(orderSn,reson).map { "取消成功" }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun addCart(skuId: Int) {
        cartApi.add(skuId,1,null).map { 1 }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

}