package com.enation.javashop.android.middleware.logic.presenter.setting

import android.content.Context
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.joinManager
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.TokenManager
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.setting.SettingActivityContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.imagepluin.utils.ClearUtils
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/9 上午9:36
 * @From   com.enation.javashop.android.middleware.logic.presenter.setting
 * @Note   设置页面逻辑控制器
 */
open class SettingActivityPresenter @Inject constructor() : RxPresenter<SettingActivityContract.View>(), SettingActivityContract.Presenter {

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  监听者
     */
    private val observer  = object : ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            when(result){
                is ResponseBody ->{
                    providerView().logout()
                }

                is MemberViewModel ->{
                    providerView().renderInfo(result)
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
     * @From   SettingActivityPresenter
     * @Date   2018/3/9 上午9:37
     * @Note   加载用户信息
     */
    override fun loadMemberInfo() {
        memberApi.memberInfo()
                .map { return@map MemberViewModel.map(it) }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   SettingActivityPresenter
     * @Date   2018/3/9 上午9:38
     * @Note   分享应用
     */
    override fun appShare() {

    }

    /**
     * @author LDD
     * @From   SettingActivityPresenter
     * @Date   2018/3/9 上午9:38
     * @Note   清除缓存
     */
    override fun clearChche(context :Context) {
        Observable.create<String> {
            ClearUtils.getInstance().clearImageDiskCache(context)
            it.onNext("0KB")
        }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe {
                    providerView().renderCacheSize(it)
                }.joinManager(this)
    }

    override fun getCacheSize(context: Context) {
        Observable.create<String> {
            it.onNext(ClearUtils.getFormatSize(ClearUtils.getInstance().getFolderSize(File(context.externalCacheDir!!.path,"Glide")).toDouble()))
        }.compose(ThreadFromUtils.defaultSchedulers())
                .subscribe {
                    providerView().renderCacheSize(it)
                }.joinManager(this)
    }

    /**
     * @author LDD
     * @From   Presenter
     * @Date   2018/8/13 下午8:52
     * @Note   退出登录
     */
    override fun logout(uid :String) {
        memberApi.logout(uid)
                 .compose(ThreadFromUtils.defaultSchedulers())
                 .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   SettingActivityPresenter
     * @Date   2018/3/9 上午9:39
     * @Note   绑定Dagger
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }



}