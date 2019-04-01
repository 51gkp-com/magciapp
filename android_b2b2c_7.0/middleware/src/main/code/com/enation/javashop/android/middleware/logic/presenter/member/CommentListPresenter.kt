package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.CommentListContract
import com.enation.javashop.android.middleware.model.OrderActionModel
import com.enation.javashop.android.middleware.model.OrderItemGoodsViewModel
import com.enation.javashop.android.middleware.model.OrderItemViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/24 下午6:02
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   评论列表逻辑控制层
 */
class CommentListPresenter @Inject constructor() :RxPresenter<CommentListContract.View>(),CommentListContract.Presenter {

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var orderApi : OrderApi

    private val observer = object: ConnectionObserver<ArrayList<OrderItemViewModel>>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: ArrayList<OrderItemViewModel>, connectionQuality: ConnectionQuality) {
            providerView().renderCommentUi(data)
            providerView().complete("")
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
     * @From   CommentListPresenter
     * @Date   2018/4/24 下午6:12
     * @Note   加载评论数据
     */
    override fun loadComment(page :Int) {
        orderApi.getOrderList("WAIT_COMMENT",page,10).map { responseBody ->

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
                        skuList,
                        isComment = true
                )

                list.add(orderItem)
            }

            return@map  list
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)


    }

    /**
     * @author LDD
     * @From   CommentListPresenter
     * @Date   2018/4/24 下午6:12
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}