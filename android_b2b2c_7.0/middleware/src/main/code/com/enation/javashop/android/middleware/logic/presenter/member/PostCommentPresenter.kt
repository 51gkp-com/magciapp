package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.BaseApi
import com.enation.javashop.android.middleware.api.ExtraApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OrderApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.PostCommentContract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.PostCommentGoodsViewModel
import com.enation.javashop.android.middleware.model.PostCommentModel
import com.enation.javashop.android.middleware.model.PostCommentViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/4/26 下午2:55
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   发送评论逻辑控制器
 */
class PostCommentPresenter @Inject constructor() :RxPresenter<PostCommentContract.View>() , PostCommentContract.Presenter {


    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var orderApi : OrderApi

    @Inject
    protected lateinit var memberApi: MemberApi

    @Inject
    protected lateinit var baseApi: BaseApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is PostCommentViewModel -> {
                    providerView().renderCommentUi(data)
                    providerView().complete()
                }
                is String ->{
                    providerView().complete(data)
                }
                is ArrayList<*> ->{
                    providerView().complete()
                    providerView().imageUploadResult(data[0] as String)
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

    override fun commit(data: PostCommentModel) {
        memberApi.addComment(data).map { "评价成功" }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }


    /**
     * @author LDD
     * @From   PostCommentPresenter
     * @Date   2018/4/26 下午2:55
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PostCommentPresenter
     * @Date   2018/4/26 下午2:56
     * @Note   加载评论数据
     * @param  orderSn 评论订单的订单号
     */
    override fun loadCommentData(orderSn: String) {

        orderApi.getOrderDetail(orderSn).map { responseBody ->
            var jsonOrder = JSONObject(responseBody.getJsonString())

            var jsonSkuArray = JSONArray(jsonOrder.get("order_sku_list").toString())

            var postCommentView = ArrayList<PostCommentGoodsViewModel>()

            for (i in 0..(jsonSkuArray.length() - 1)) {
                val jsonSku = jsonSkuArray.getJSONObject(i)

                var item =PostCommentGoodsViewModel(
                        jsonSku.valueString("goods_image"),
                        5,
                        jsonSku.valueInt("sku_id"),
                        "".bindingParams(),
                        ArrayList<String>()
                )
                postCommentView.add(item)
            }

            return@map PostCommentViewModel(
                    jsonOrder.valueString("sn"),
                    5,
                    5,
                    5,
                    postCommentView
            )
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    override fun uploader(filePath: String) {
        val localFile = File(filePath)
        val localRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), localFile)
        val localPart = MultipartBody.Part.createFormData("file", localFile.name, localRequestBody)
        baseApi.uploader(localPart,"")
                .map { responseBody ->
                    return@map arrayListOf(responseBody.toJsonObject().valueString("url"))
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

}