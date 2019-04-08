package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.api.PassportApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.member.MemberPasswordContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.model.NetStateEvent
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/11 下午1:33
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员关于密码操作
 */
class MemberPasswordPresenter @Inject constructor() :RxPresenter<MemberPasswordContract.View>() ,MemberPasswordContract.Presenter{

    /**
     * @Name  passportApi
     * @Type  PassportApi
     * @Note  Api
     */
    @Inject
    protected lateinit var passportApi : PassportApi

    /**
     * @Name  passportApi
     * @Type  PassportApi
     * @Note  Api
     */
    @Inject
    protected lateinit var memberApi : MemberApi

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  数据监听者
     */
    private val observer = object : ConnectionObserver<Int>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Int, connectionQuality: ConnectionQuality) {
            var message = ""
            if (result == GlobalState.REGISTER_USER){
                message = "注册成功"
            }else if (result == GlobalState.EDIT_PASSWORD){
                message = "修改成功,请重新登录！"
            }else if (result == GlobalState.FIND_PASSWORD){
                message = "找回成功,请重新登录！"
            }
            providerView().complete(message,result)
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }

        override fun onNoneNet() {
            getEventCenter().post(NetStateEvent(NetState.NONE))
        }
    }

    /**
     * @author LDD
     * @From   MemberPasswordPresenter
     * @Date   2018/5/11 下午1:33
     * @Note   修改密码
     * @param  password 密码
     * @param  vcode 图片验证码
     */
    override fun editPassword(password: String, vcode: String) {
        memberApi.updatePassword(UUID.uuid,vcode,MD5Util.MD5Encode(password,null)!!)
                .map {
                    return@map GlobalState.EDIT_PASSWORD }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberPasswordPresenter
     * @Date   2018/5/11 下午1:33
     * @Note   注册会员
     * @param  mobile 手机号
     * @param  password 密码
     */
    override fun registerUser(password: String, mobile: String) {
        passportApi.register(mobile, MD5Util.MD5Encode(password,null)!!)
                .flatMap { return@flatMap  memberApi.memberInfo()}
                .map {
                    MemberState.manager.updateMember(MemberViewModel.map(it))
                    return@map GlobalState.REGISTER_USER }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun registerConnectUser(type: String, uoionId: String, password: String, mobile: String, vcode: String) {
        passportApi.register(mobile, MD5Util.MD5Encode(password,null)!!)
                .flatMap {
                    return@flatMap passportApi.authNormalBind(uoionId,type,mobile,MD5Util.MD5Encode(password,null)!!,vcode)
                }
                .flatMap { return@flatMap  memberApi.memberInfo()}
                .map {
                    MemberState.manager.updateMember(MemberViewModel.map(it))
                    return@map GlobalState.REGISTER_USER }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberPasswordPresenter
     * @Date   2018/5/11 下午1:34
     * @Note   找回密码
     * @param  password 密码
     * @param  vcode  验证码
     */
    override fun findPassword(password: String, vcode: String) {
        passportApi.findPassword(UUID.uuid,MD5Util.MD5Encode(password,null)!!)
                .map {
                    return@map GlobalState.EDIT_PASSWORD }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberPasswordPresenter
     * @Date   2018/5/11 下午1:35
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}