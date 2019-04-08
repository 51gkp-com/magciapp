package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.PassportApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberSendMessageContract
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.model.NetStateEvent
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import java.util.ArrayList
import javax.inject.Inject


/**
 * @author LDD
 * @Date   2018/5/10 下午1:23
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   发送信息逻辑控制器
 */
class MemberSendMessagePresenter @Inject constructor() :RxPresenter<MemberSendMessageContract.View>(),MemberSendMessageContract.Presenter {

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员Api
     */
    @Inject
    protected lateinit var passportApi: PassportApi

    /**数据监听者*/
    private val observer = object : ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if (result is ArrayList<*>){
                providerView().complete("验证成功")
                providerView().sendFindPwdMessage(result.get(1) as String)
            }else{
                providerView().complete("发送成功",Send)
                providerView().sendSuccess(result as String)
            }
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
     * @Name  RefreshSendUI
     * @Type  Int
     * @Note  更新发送UI标记
     */
    val Send = 1

    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/10 上午10:09
     * @Note   发送绑定手机号短信
     * @param  phoneNum 手机号
     * @param  sendVcode 发送短信验证码
     */
    override fun sendBindPhoneNumVcode(phoneNum: String,sendVcode :String) {
        passportApi.sendBindPassword(phoneNum,sendVcode)
                .map { return@map phoneNum }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/10 上午10:09
     * @Note   发送修改密码短信
     * @param  phoneNum 手机号
     * @param  sendVcode 发送短信验证码
     */
    override fun sendEditPasswordNumVcode(phoneNum: String,sendVcode :String) {
        passportApi.sendEditMessage(sendVcode)
                .map { return@map phoneNum }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/10 上午10:09
     * @Note   发送注册短信
     * @param  phoneNum 手机号
     * @param  sendVcode 发送短信验证码
     */
    override fun sendRegisterVcode(phoneNum: String,sendVcode :String) {
        passportApi.sendMessageRegister(captcha = sendVcode,mobile = phoneNum)
                .map { return@map phoneNum }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/10 上午10:10
     * @Note   发送找回密码短信
     * @param  phoneNum 手机号
     * @param  sendVcode 发送短信验证码
     */
    override fun sendFindPasswordVcode(phoneNum: String,sendVcode :String) {
        providerView().start()
        passportApi.sendMessageFindPassword(sendVcode)
                .map { return@map phoneNum }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/14 上午10:02
     * @Note   发送手机号快捷登录信息
     * @param  phone 手机号
     * @param  vcode 验证码
     */
    override fun sendPhoneLoginMessage(phone:String,vcode:String){
        passportApi.sendMessageLogin(captcha = vcode,mobile = phone)
                .map { return@map phone }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun checkFindPwdAccount(account: String, vcode: String) {
        passportApi.checkFindPwdAccount(account,vcode)
                .map {
                    val uuid = it.toJsonObject().valueString("uuid")
                    if(uuid.isNotEmpty()){
                        UUID.refreshUUID(uuid)
                    }
                    var a = ArrayList<String>()
                    a.add("Find")
                    a.add(account)
                    return@map  a}
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }


    /**
     * @author LDD
     * @From   MemberSendMessagePresenter
     * @Date   2018/5/14 上午10:08
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}