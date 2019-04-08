package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.NetStateEvent
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberFragmentContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RecommendGoodsViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/2/24 下午5:26
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员Fragment逻辑控制器
 */
open class MemberFragmentPresenter @Inject constructor() : RxPresenter<MemberFragmentContract.View>(), MemberFragmentContract.Presenter {

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi

    @Inject
    protected lateinit var goodsApi: GoodsApi

    /**
     * @author LDD
     * @Date   2018/2/24 下午6:02
     * @From   MemberFragmentPresenter
     * @Note   会员数据监听器
     */
    inner class MemberObserable<T>(private val onNextCallBack: (data: T) -> (Unit)) : ConnectionObserver<T>() {

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:03
         * @Note   请求开始去
         */
        override fun onStartWithConnection() {
            providerView().start()
        }

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:04
         * @Note   请求成功
         * @param  result 结果
         * @param  connectionQuality 网络状态
         */
        override fun onNextWithConnection(result: T, connectionQuality: ConnectionQuality) {
            providerView().complete("加载完成")
            onNextCallBack(result)
        }

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:04
         * @Note   请求错误
         * @param  error 错误信息
         * @param  connectionQuality 网络状态
         */
        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:05
         * @Note   绑定索引
         * @param  p0 观察者索引
         */
        override fun attachSubscribe(p0: Disposable) {
            addDisposable(p0)
        }

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:05
         * @Note   当前网速
         * @param  bits 网速
         */
        override fun connectionBitsOfSecond(bits: Double) {
            if (bits < 100 && bits != -1.0) {
                showMessage("当前网络质量差   ${bits.toInt()}kbps/s")
            }
        }

        /**
         * @author LDD
         * @From   MemberObserable
         * @Date   2018/2/24 下午6:06
         * @Note   无网络状态进行什么操作
         */
        override fun onNoneNet() {
            getEventCenter().post(NetStateEvent(NetState.NONE))
        }

    }

    /**
     * @author LDD
     * @From   MemberFragmentPresenter
     * @Date   2018/2/24 下午6:06
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberFragmentPresenter
     * @Date   2018/2/24 下午6:07
     * @Note   加载会员信息
     */
    override fun loadMemberInfo() {
        memberApi.memberInfo()
                .map { return@map MemberViewModel.map(it) }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(MemberObserable {result ->
                    MemberState.manager.updateMember(result)
                    providerView().showMember(result)
                })
    }

    /**
     * @author LDD
     * @From   MemberFragmentPresenter
     * @Date   2018/2/24 下午6:07
     * @Note   加载猜你喜欢商品信息
     * @param  page 分页加载
     */
    override fun loadGuessLike(page: Int) {
        goodsApi.searchGoodsList(page,10,HashMap<String,Any>()).map { responseBody ->

            var jsonPage = JSONObject(responseBody.getJsonString())

            var jsonArray = JSONArray(jsonPage.get("data").toString())

            var goodsItemList = ArrayList<RecommendGoodsViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                goodsItemList.add(RecommendGoodsViewModel.map(jsonObject))
            }

            return@map goodsItemList

        }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe( MemberObserable{
                    providerView().showGuessLike(it)
                })
    }

}