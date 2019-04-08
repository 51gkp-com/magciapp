package com.enation.javashop.android.component.member.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberCheckVcodeLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.VcodeDialog
import com.enation.javashop.android.lib.widget.VerificationCodeView
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.android.middleware.logic.contract.member.ImageVcodeContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberCheckVcodeContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberLoginContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberSendMessageContract
import com.enation.javashop.android.middleware.logic.presenter.member.ImageVcodePresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberCheckVcodePresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberLoginPresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberSendMessagePresenter
import com.enation.javashop.android.middleware.model.ConnectLoginModel
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.member_check_vcode_lay.*
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/9 下午2:40
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员检查验证码页面
 */
@Router(path = "/member/security/phone/check")
class MemberCheckVcodeActivity :BaseActivity<MemberCheckVcodePresenter,MemberCheckVcodeLayBinding>(),MemberCheckVcodeContract.View,MemberSendMessageContract.View, MemberLoginContract.View ,ImageVcodeContract.View {

    /**
     * @Name  vcodePresenter
     * @Type  ImageVcodePresenter
     * @Note  验证码逻辑控制器
     */
    @Inject
    lateinit var vcodePresenter : ImageVcodePresenter

    /**
     * @Name  sendMessagePresenter
     * @Type  MemberSendMessagePresenter
     * @Note  发送短信逻辑控制器
     */
    @Inject
    lateinit var sendMessagePresenter: MemberSendMessagePresenter

    /**
     * @Name  sendMessagePresenter
     * @Type  MemberLoginPresenter
     * @Note  登录逻辑控制器
     */
    @Inject
    lateinit var loginPresenter : MemberLoginPresenter

    /**
     * @Name  timer
     * @Type  TimeEngine
     * @Note  计时器
     */
    private val timer by lazy { TimeEngine.build(60) }

    /**
     * @Name  type
     * @Type  Int
     * @Note  类型
     */
    @Autowired(name = "type",required = true)
    @JvmField var type = 1

    @Autowired(name = "auth",required = false)
    @JvmField var connectInfo : ConnectLoginModel? = null

    /**
     * @Name  phoneNum
     * @Type  String
     * @Note  手机号
     */
    @Autowired(name = "phoneNum",required = true)
    @JvmField var phoneNum = "13393362615"


    /**
     * @Name  phoneNum
     * @Type  String
     * @Note  手机号
     */
    @Autowired(name = "newPhone",required = false)
    @JvmField var newPhone = false

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:43
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_check_vcode_lay
    }


    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:43
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/10 下午3:22
     * @Note   绑定Presenter 使用多个Presenter时重写
     */
    override fun attachView() {
        super.attachView()
        sendMessagePresenter.attachView(this)
        loginPresenter.attachView(this)
        vcodePresenter.attachView(this)
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/10 下午3:22
     * @Note   解除Presenter 使用多个Presenter时重写
     */
    override fun detachView() {
        super.detachView()
        sendMessagePresenter.detachView()
        loginPresenter.detachView()
        vcodePresenter.detachView()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/8/14 上午11:46
     * @Note   加载验证码
     */
    private fun loadVcode(){
        when(type){
            GlobalState.REGISTER_USER ->{
                vcodePresenter.loadRegisterImageVcode()
            }
            GlobalState.BIND_PHONE ->{

            }
            GlobalState.FIND_PASSWORD ->{
                vcodePresenter.loadFindImageVcdoe()
            }
            GlobalState.UPDATE_PHONE ->{
                if(newPhone){
                    vcodePresenter.loadBindImageVcode()
                }else{
                    vcodePresenter.loadValidateMobileVcodde()
                }
            }
            GlobalState.EDIT_PASSWORD ->{
                vcodePresenter.loadValidateMobileVcodde()
            }
        }
    }

    override fun renderVcode(bitmap: Bitmap) {

    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:45
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        member_check_vocde_resend_tv.movementMethod = LinkMovementMethod.getInstance()
        member_check_vocde_resend_tv.highlightColor = Color.TRANSPARENT
        if (type == GlobalState.EDIT_PASSWORD || (type == GlobalState.UPDATE_PHONE && !newPhone)){
            sendMessage()
        }else{
            timeStart()
        }
    }


    override fun sendFindPwdMessage(mobile: String) {

    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:46
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_check_vocde_topbar.setLeftClickListener {
            pop()
        }
        member_check_vocde_vc.setInputCompleteListener(object:VerificationCodeView.InputCompleteListener{

            override fun inputComplete() {
                check()
            }

            override fun deleteContent() {

            }
        })
    }


    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/10 上午10:48
     * @Note   开始执行
     */
    private fun timeStart(){
        timer.execute ({ _, _, _, sec ->
            val header = "已发送到手机 $phoneNum "
            var time = "(重新发送 $sec)"
            if (sec == 0){
                time = "重新发送"
            }
            var str = header+time
            runOnUiThread {
                member_check_vocde_resend_tv.text = SpannableStringBuilder(str).then {
                    self ->
                    if (time == "重新发送"){
                        self.setSpan(object : ClickableSpan(){
                            override fun onClick(widget: View?) {
                                sendMessage()
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.color = Color.parseColor("#e83437")
                                ds.isUnderlineText = false
                                ds.clearShadowLayer()
                            }

                        },str.length-time.length,str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        self.setSpan(ForegroundColorSpan(Color.parseColor("#e83437")),str.length-time.length,str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                }
            }
        })
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/14 上午10:46
     * @Note   刷新会员信息
     * @param  info 会员信息
     */
    override fun refreshMemberInfo(info: MemberViewModel) {
        getEventCenter().post(LoginEvent())
        pop()
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/10 上午10:27
     * @Note   发送信息
     */
    private fun sendMessage(){
        VcodeDialog.build(this)
                .config({
            code ->
            when (type){
                GlobalState.REGISTER_USER ->{
                    sendMessagePresenter.sendRegisterVcode(phoneNum,code)
                }
                GlobalState.BIND_PHONE ->{

                }
                GlobalState.FIND_PASSWORD ->{
                    sendMessagePresenter.sendFindPasswordVcode(phoneNum,code)
                }
                GlobalState.UPDATE_PHONE ->{
                    if(newPhone){
                        sendMessagePresenter.sendBindPhoneNumVcode(phoneNum,code)
                    }else{
                        sendMessagePresenter.sendEditPasswordNumVcode(phoneNum,code)
                    }                }
                GlobalState.EDIT_PASSWORD ->{
                    sendMessagePresenter.sendEditPasswordNumVcode(phoneNum,code)
                }
                GlobalState.DYNAMIC_LOGIN ->{
                    sendMessagePresenter.sendPhoneLoginMessage(phoneNum,code)
                }
            }
            timeStart()
        },{
            iv ->
            vcodePresenter.imageLoader = { image ->
                run {
                    iv.setImageBitmap(image)
                }
            }
            loadVcode()
        },{
            if (type == GlobalState.EDIT_PASSWORD){
                pop()
            }
        }).show()
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/10 上午10:27
     * @Note   检验验证码
     */
    private fun check(){
        val vcode = member_check_vocde_vc.getInputContent()
        when (type){
            GlobalState.REGISTER_USER ->{
                presenter.checkRegisterVcode(vcode,phoneNum)
            }
            GlobalState.BIND_PHONE ->{
            }
            GlobalState.FIND_PASSWORD ->{
                presenter.checkFindPasswordVcode(vcode,phoneNum)
            }
            GlobalState.EDIT_PASSWORD ->{
                presenter.checkEditPasswordVcode(vcode,phoneNum)
            }
            GlobalState.UPDATE_PHONE ->{
                if(newPhone){
                    presenter.checkUpDatePhoneNumVcode(vcode,phoneNum)
                }else{
                    presenter.checkBindPhoneNumVcode(vcode,phoneNum)
                }
            }
            GlobalState.DYNAMIC_LOGIN ->{
                if (connectInfo != null){
                    loginPresenter.authMobileBind(connectInfo!!.type,connectInfo!!.unionId,phoneNum,vcode)
                }else{
                    loginPresenter.phoneLogin(phoneNum,vcode)
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/8/14 上午11:27
     * @Note   发送短信
     * @param  mobile 手机号
     */
    override fun sendSuccess(mobile: String) {
        showMessage("发送成功")
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/11 下午2:27
     * @Note   下一步
     */
    private fun next(){
        if (type == GlobalState.UPDATE_PHONE){
            if (newPhone){
                showMessage("更改手机号成功，请重新登录！")
                pop()
            }else{
                push("/member/security/phone/input",{
                    postcard ->
                    postcard.withInt("type",type)
                    postcard.withBoolean("newPhone",true)
                })
                pop()
            }
        }else if(type == GlobalState.BIND_PHONE) {
            pop()
        }else if(type == GlobalState.DYNAMIC_LOGIN) {
            getEventCenter().post(LoginEvent())
            pop()
        }else{
            push("/member/security/password/main",{
                postcard ->
                postcard.withString("phoneNum",phoneNum)
                postcard.withInt("type",type)
                if (connectInfo != null){
                    postcard.withObject("auth",connectInfo)
                }
            })
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:46
     * @Note   错误回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
        timeStart()
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:46
     * @Note   完成回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
        when(type){
            sendMessagePresenter.Send ->{
                if (type == GlobalState.EDIT_PASSWORD){
                    timeStart()
                }
                timer.reset()
                timeStart()
            }
            presenter.Check ->{
                next()
            }
        }
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:47
     * @Note   开始加载
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:47
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    override fun alipayAuthInfo(info: String) {
        // 不需要实现
    }

    override fun authLoginError() {
        // 不需要实现
    }

    /**
     * @author LDD
     * @From   MemberCheckVcodeActivity
     * @Date   2018/5/9 下午3:47
     * @Note   销毁回调
     */
    override fun destory() {
        timer.destory()
    }
}