package com.enation.javashop.android.middleware.logic.presenter.home

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.BuildConfig
import com.enation.javashop.android.middleware.api.ApiManager
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.OtherApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.home.HomeActivityContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.model.NetStateEvent
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.security.MessageDigest
import javax.inject.Inject

/**
 * @author  LDD
 * @Date   2018/1/16 下午12:05
 * @From   com.enation.javashop.android.middleware.logic.presenter.home
 * @Note   HomeActivity逻辑支撑
 */
class HomeActivityPresenter @Inject constructor() :RxPresenter<HomeActivityContract.View>(),HomeActivityContract.Presenter {

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员API
     */
    @Inject
    protected lateinit var memberApi : MemberApi


    /**
     * @Name  otherApi
     * @Type  OtherApi
     * @Note  其他API
     */
    @Inject
    protected lateinit var otherApi : OtherApi

    /**
     * @author LDD
     * @From   HomeActivityPresenter
     * @Date   2018/1/19 下午6:27
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @Name  homeActivityObserver
     * @Type  ConnectionObserver<MemberModel>
     * @Note  网络数据观察者
     */
    private val homeActivityObserver = object :ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if(result is String){
                if(!BuildConfig.VERSION_NAME.equals(result)){
                    providerView().showUpdate()
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(p0: Disposable) {
            addDisposable(p0)
        }

        override fun connectionBitsOfSecond(bits: Double) {
            AppTool.Net.verifyNetSpeed(bits)
        }

        override fun onNoneNet() {
            getEventCenter().post(NetStateEvent(NetState.NONE))
        }

    }

    /**
     * @author LDD
     * @From   HomeActivityPresenter
     * @Date   2018/1/16 下午12:30
     * @Note   判断当前用户登录状态
     */
    override fun isLogin() {
//        memberApi.getUserState()
//                .compose(ThreadFromUtils.defaultSchedulers())
//                .subscribe(homeActivityObserver)
    }

    override fun checkUpdate() {

        otherApi.getAppVersion()
                .map {
                    it.getJsonString()
                }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(homeActivityObserver)
    }

}

