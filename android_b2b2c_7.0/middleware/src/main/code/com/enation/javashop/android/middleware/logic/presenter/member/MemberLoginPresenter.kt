package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.*
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberLoginContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.model.NetStateEvent
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.GsonHelper
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/14 上午10:08
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员登录逻辑控制器
 */
class MemberLoginPresenter @Inject constructor() : RxPresenter<MemberLoginContract.View>() ,MemberLoginContract.Presenter {

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  监听者
     */
    private val observer = object : ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {

            when(result){
                is MemberViewModel ->{
                    providerView().complete("登录成功")
                    MemberState.manager.updateMember(result)
                    providerView().refreshMemberInfo(result)
                }
                is String ->{
                    providerView().alipayAuthInfo(result)
                    providerView().complete()
                }
                else -> {
                    providerView().complete()
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
            if (error.customMessage.contains("该账号未绑定")){
                providerView().authLoginError()
            }
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }

        override fun onNoneNet() {
            getEventCenter().post(NetStateEvent(NetState.NONE))
        }
    }

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员Api
     */
    @Inject
    protected lateinit var passportApi: PassportApi

    /**
     * @author LDD
     * @From   MemberLoginPresenter
     * @Date   2018/5/14 上午10:15
     * @Note   普通账号登录
     * @param  username 账号
     * @param  password 密码
     * @param  vcode    验证码
     */
    override fun login(username: String, password: String, vcode: String) {
        passportApi.login(username, MD5Util.MD5Encode(password,null)!!,vcode)
                .flatMap {
                    return@flatMap memberApi.memberInfo()
                }
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { response ->
                    return@map MemberViewModel.map(response)
                }.subscribe(observer)
    }

    override fun authBind(type: String, openId: String, username: String, password: String, vcode: String) {
        passportApi.authNormalBind(openId,type,username,MD5Util.MD5Encode(password,null)!!,vcode)
                .flatMap {
                    return@flatMap memberApi.memberInfo()
                }
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { response ->
                    return@map MemberViewModel.map(response)
                }.subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberLoginPresenter
     * @Date   2018/8/14 上午11:19
     * @Note   手机登录
     * @param  phone 手机号
     * @param  vcode 验证码
     */
    override fun phoneLogin(phone: String, vcode: String) {
            passportApi.mobileLogin(phone,vcode)
                    .flatMap {
                        return@flatMap memberApi.memberInfo()
                    }
                    .compose(ThreadFromUtils.defaultSchedulers())
                    .map { response ->
                        return@map MemberViewModel.map(response)
                    }.subscribe(observer)
    }

    override fun authMobileBind(type: String, openId: String, phone: String, vcode: String) {
        passportApi.authMobileBind(vcode,openId,type,phone)
                .flatMap {
                    return@flatMap memberApi.memberInfo()
                }
                .compose(ThreadFromUtils.defaultSchedulers())
                .map { response ->
                    return@map MemberViewModel.map(response)
                }.subscribe(observer)
    }

    override fun getAlipayAuthInfo() {
           passportApi.getAlipayAuthInfo().map {
               it.getJsonString() }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    override fun authLogin(type: String, openId: String) {
        passportApi.authLogin(type,openId)
                .flatMap { responseBody ->
                    val isBind = responseBody.toJsonObject().valueBool("is_bind")
                    if(isBind){
                        return@flatMap memberApi.memberInfo()
                    }else{
                        return@flatMap RxExtra.createNetErrorObservable("该账号未绑定")
                    }
                }.map { response ->
                    return@map MemberViewModel.map(response)
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberLoginPresenter
     * @Date   2018/5/14 上午10:13
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}