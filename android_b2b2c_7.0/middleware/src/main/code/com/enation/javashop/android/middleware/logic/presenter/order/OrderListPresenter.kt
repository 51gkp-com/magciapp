package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderListContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/16 下午4:36
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单列表逻辑控制器
 */
class OrderListPresenter @Inject constructor() :RxPresenter<OrderListContract.View>(), OrderListContract.Presenter {



    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var orderApi : OrderApi



    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ArrayList<*> -> {
                    providerView().initOrderList(data as ArrayList<OrderItemViewModel>)
                    providerView().complete()
                }else -> {
                    providerView().complete("操作成功",3)
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
     * @From   OrderListPresenter
     * @Date   2018/4/16 下午4:37
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderListPresenter
     * @Date   2018/4/16 下午4:37
     * @Note   加载订单列表数据
     * @param  state 订单类别
     * @param  page  分页查询
     */
    override fun loadOrderList(state: String, page: Int) {

        orderApi.getOrderList(state,page).map { responseBody ->

            val list = ArrayList<OrderItemViewModel>()
            val jsonResult = responseBody.getJsonString()
            val jsonData = JSONObject(jsonResult).get("data").toString()
            val jsonArray = JSONArray(jsonData)

            //遍历订单
            for (i in 0..(jsonArray.length() - 1)){
                val jsonObject = jsonArray.getJSONObject(i)

                //商品的Json
                var jsonSkuList = jsonObject.get("sku_list").toString()
                var skuArray = JSONArray(jsonSkuList)
                val skuList = ArrayList<OrderItemGoodsViewModel>()

                //遍历商品
                for (i in 0..(skuArray.length() - 1)){
                    val skuObject = skuArray.getJSONObject(i)
                    skuList.add(OrderItemGoodsViewModel(skuObject.valueString("name"),
                            skuObject.valueString("goods_image"))
                    )
                }

                //订单可操作状态
                var orderOperate = JSONObject(jsonObject.get("order_operate_allowable_vo").toString())
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
                        jsonObject.valueString("ship_status") != "SHIP_NO"
                )

                //订单ViewModel
                var orderItem =  OrderItemViewModel(
                    jsonObject.valueString("sn"),
                    jsonObject.valueString("seller_name"),
                    jsonObject.valueInt("seller_id"),
                    jsonObject.valueDouble("order_amount"),
                    jsonObject.valueDouble("order_amount"),
                    jsonObject.valueString("order_status_text"),
                    jsonObject.valueInt(""),
                        orderAction,
                        skuList
                )

                list.add(orderItem)
            }

            return@map  list
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }


    /**
     * @author Snow
     * @From   OrderListPresenter
     * @Date   2018/4/16 下午4:37
     * @Note   确认收货
     * @param  orderSn 订单类别
     */
    override fun rog(orderSn: String) {
        orderApi.rog(orderSn).
                compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author Snow
     * @From   OrderListPresenter
     * @Date   2018/4/16 下午4:37
     * @Note   取消订单
     * @param  orderSn 订单类别
     */
    override fun cancal(orderSn: String,reason :String) {
        orderApi.cancel(orderSn,reason).
                compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

}