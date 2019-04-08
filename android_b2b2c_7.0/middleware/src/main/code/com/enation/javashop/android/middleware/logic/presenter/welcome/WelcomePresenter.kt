package com.enation.javashop.android.middleware.logic.presenter.welcome

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.vo.NetStateEvent
import com.enation.javashop.android.middleware.api.AdApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.welcome.WelcomeContract
import com.enation.javashop.android.middleware.model.AdViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/1/9 下午4:17
 * @From   com.enation.javashop.android.lib.logic.presenter.welcome
 * @Note   欢迎页面控制器
 */
class WelcomePresenter @Inject constructor() : RxPresenter<WelcomeContract.View>(), WelcomeContract.Presenter {


    /**
     * @Name  tangramApi
     * @Type  TangramApi
     * @Note  七巧板框架
     */
    @Inject
    protected lateinit var adApi : AdApi

    /**
     * @author LDD
     * @From   HomeFragmentPresenter
     * @Date   2018/1/19 下午6:28
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @Name  welcomeObserver
     * @Type  ConnectionObserver
     * @Note  注册广告监听
     */
    private val welcomeObserver = object: ConnectionObserver<ArrayList<AdViewModel>>() {
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<AdViewModel>, connectionQuality: ConnectionQuality) {
            /**当返回值的size为0时证明没有广告直接跳转首页，有广告的话展示广告*/
            if (result.size == 0){
                providerView().toHome()
            }else{
                providerView().showAd(result)
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
            providerView().toHome()
        }

        override fun attachSubscribe(p0: Disposable) {
            /**添加至Disposable引用集合*/
            addDisposable(p0)
        }

        override fun connectionBitsOfSecond(bits: Double) {
            if (bits < 100){
                showMessage("当前网络质量差")
            }
        }

        override fun onNoneNet() {
             getEventCenter().post(NetStateEvent(NetState.NONE))
        }
    }

    /**
     * @author  LDD
     * @From    WelcomePresenter
     * @Date   2018/1/9 下午4:25
     * @Note   加载广告
     */
    override fun loadAd() {
        adApi.getAd()
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(welcomeObserver)
    }

}