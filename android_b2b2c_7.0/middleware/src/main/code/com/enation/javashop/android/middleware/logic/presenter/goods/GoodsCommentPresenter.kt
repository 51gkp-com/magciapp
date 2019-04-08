package com.enation.javashop.android.middleware.logic.presenter.goods

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsCommentContract
import com.enation.javashop.android.middleware.model.GoodsCommentViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/30 下午2:42
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Note   数据控制器
 */
class GoodsCommentPresenter @Inject constructor() :RxPresenter<GoodsCommentContract.View>(),GoodsCommentContract.Presenter {

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


    private val observer = object :ConnectionObserver<ArrayList<GoodsCommentViewModel>>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<GoodsCommentViewModel>, connectionQuality: ConnectionQuality) {
            providerView().showComment(result)
            providerView().complete("")
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError("加载失败")
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
        
    }


    /**
     * @author LDD
     * @From   GoodsCommentPresenter
     * @Date   2018/3/30 下午2:43
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsCommentPresenter
     * @Date   2018/3/30 下午2:43
     * @Note   加载评论数
     * @param  goodsId 商品ID
     */
    override fun loadCommentNum(goodsId: Int) {

    }

    /**
     * @author LDD
     * @From   GoodsCommentPresenter
     * @Date   2018/3/30 下午2:43
     * @Note   加载评论
     * @param  goodsId 商品ID
     * @param  state   评论等级
     * @param  page    查询分页
     */
    override fun loadComment(goodsId: Int, state: String, page: Int) {
        val haveImage :Boolean? = if (state == "image") {true} else {null}
        val grade :String? = if (state.isEmpty()){null}else{state}
        memberApi.getComments(goodsId,grade,haveImage,page,10).map { responseBody ->

            var jsonPage = JSONObject(responseBody.getJsonString())

            var jsonArray = JSONArray(jsonPage.get("data").toString())

            var commentList = ArrayList<GoodsCommentViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val commentObject = jsonArray.getJSONObject(i)

                commentList.add(GoodsCommentViewModel.map(commentObject))
            }

            return@map commentList

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}