package com.enation.javashop.android.middleware.logic.presenter.member

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.ConnectionObserver
import com.enation.javashop.android.lib.utils.joinManager
import com.enation.javashop.android.middleware.api.BaseApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.member.ImageVcodeContract
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/14 下午3:19
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   图片验证码逻辑控制
 */
class ImageVcodePresenter @Inject constructor() :RxPresenter<ImageVcodeContract.View>(),ImageVcodeContract.Presenter {


    /**
     * @Name  baseApi
     * @Type  BaseApi
     * @Note  基础Api
     */
    @Inject
    protected lateinit var baseApi: BaseApi

    var imageLoader :((Bitmap) ->Unit)? = null

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   登录验证码
     */
    override fun loadLoginImageVcode() {
        loadVcode(GlobalState.LOGIN)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   注册验证码
     */
    override fun loadRegisterImageVcode() {
        loadVcode(GlobalState.REGISTER_USER)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   找回密码验证码
     */
    override fun loadFindImageVcdoe() {
        loadVcode(GlobalState.FIND_PASSWORD)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   绑定手机验证码
     */
    override fun loadBindImageVcode() {
        loadVcode(GlobalState.BIND_PHONE)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   更改面膜验证码
     */
    override fun loadEditImageVcode() {
        loadVcode(GlobalState.EDIT_PASSWORD)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/8/14 上午9:43
     * @Note   加载动态登录验证码
     */
    override fun loadValidateMobileVcodde() {
        loadVcode(GlobalState.VALIDATE_MOBILE)
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:27
     * @Note   加载Vcode
     * @param  type 类型
     */
    private fun loadVcode(type :Int){
        var param = ""
        when (type){
            GlobalState.LOGIN ->{
                param = "LOGIN"
            }
            GlobalState.REGISTER_USER ->{
                param = "REGISTER"
            }
            GlobalState.FIND_PASSWORD ->{
                param = "FIND_PASSWORD"
            }
            GlobalState.BIND_PHONE ->{
                param = "BIND_MOBILE"
            }
            GlobalState.EDIT_PASSWORD ->{
                param = "MODIFY_PASSWORD"
            }
            GlobalState.VALIDATE_MOBILE ->{
                param = "VALIDATE_MOBILE"
            }
        }
        baseApi.loadVcode(scene = param).map { BitmapFactory.decodeStream(it.byteStream())  }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(object : ConnectionObserver<Bitmap>(){
            override fun onStartWithConnection() {

            }

            override fun onNextWithConnection(result: Bitmap, connectionQuality: ConnectionQuality) {
                imageLoader?.invoke(result)
                providerView().renderVcode(result)
            }

            override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {

            }

            override fun attachSubscribe(var1: Disposable) {
                addDisposable(var1)
            }
        })
    }

    /**
     * @author LDD
     * @From   ImageVcodePresenter
     * @Date   2018/5/14 下午3:29
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}