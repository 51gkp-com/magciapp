package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.api.PassportApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.member.MemberCheckVcodeContract
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.model.NetStateEvent
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/9 下午2:34
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员验证手机验证码逻辑控制器
 */
class MemberCheckVcodePresenter @Inject constructor() :RxPresenter<MemberCheckVcodeContract.View>() ,MemberCheckVcodeContract.Presenter {

    /**
     * @Name  Check
     * @Type  Int
     * @Note  检查
     */
    val Check = 2

    /**
     * @Name  passportApi
     * @Type  PassportApi
     * @Note  Api
     */
    @Inject
    protected lateinit var passportApi : PassportApi

    @Inject
    protected lateinit var memberApi: MemberApi

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  数据监听者
     */
    private val observer = object : ConnectionObserver<Any>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            if(result is Int){
                providerView().complete("验证成功",1)
            }else{
                providerView().complete("验证成功",Check)
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage,Check)
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
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:38
     * @Note   检查绑定手机号验证码
     * @param  vcode 验证码
     * @param  phoneNum 手机号
     */
    override fun checkBindPhoneNumVcode(vcode: String, phoneNum: String) {
        memberApi.checkExchangeBindCode(vcode).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:38
     * @Note   检查重新绑定手机号验证码
     * @param  vcode 验证码
     * @param  phoneNum 手机号
     */
    override fun checkUpDatePhoneNumVcode(vcode: String, phoneNum: String) {
        memberApi.bindMobile(phoneNum,vcode).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }



    /**
     * @author LDD
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:37
     * @Note   验证注册验证码
     * @param  vcode 验证码
     * @param  phoneNum 手机号
     */
    override fun checkRegisterVcode(vcode: String, phoneNum: String) {
        passportApi.checkMobileMessage(phoneNum,"REGISTER",vcode).map { return@map phoneNum }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:36
     * @Note   检查找回密码验证码
     * @param  vcode 验证码
     * @param  phoneNum 手机号
     */
    override fun checkFindPasswordVcode(vcode: String, phoneNum: String) {
        passportApi.validFindCode(vcode).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:36
     * @Note   验证修改密码验证码
     * @param  vcode 验证码
     * @param  phoneNum 手机号
     */
    override fun checkEditPasswordVcode(vcode: String, phoneNum: String) {
        memberApi.checkUpdatePwdCode(vcode).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    /**
     * @author LDD
     * @From   MemberCheckVcodePresenter
     * @Date   2018/5/9 下午2:35
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}