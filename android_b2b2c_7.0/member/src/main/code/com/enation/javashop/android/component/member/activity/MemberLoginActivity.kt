package com.enation.javashop.android.component.member.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.alipay.sdk.app.AuthTask
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberLoginLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.component.member.vm.MemberLoginViewModel
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.logic.contract.member.ImageVcodeContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberLoginContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberSendMessageContract
import com.enation.javashop.android.middleware.logic.presenter.member.ImageVcodePresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberLoginPresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberSendMessagePresenter
import com.enation.javashop.android.middleware.model.ConnectLoginModel
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.connectview.logic.UmengLogin
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import com.google.gson.Gson
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.member_login_lay.*
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/14 上午10:49
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员登录页面
 */
@Router(path = "/member/login/main")
class MemberLoginActivity :BaseActivity<MemberLoginPresenter,MemberLoginLayBinding>(),MemberLoginContract.View,ImageVcodeContract.View,MemberSendMessageContract.View,TextWatcher {

    /**
     * @Name  vcodePresenter
     * @Type  ImageVcodePresenter
     * @Note  验证码逻辑控制器
     */
    @Inject
    lateinit var vcodePresenter :ImageVcodePresenter

    /**
     * @Name  messagePresenter
     * @Type  MemberSendMessagePresenter
     * @Note  发信息逻辑控制器
     */
    @Inject
    lateinit var messagePresenter :MemberSendMessagePresenter

    /**
     * @Name  loginViewModel
     * @Type  MemberLoginViewModel
     * @Note  登录视图ViewModel
     */
    private val loginViewModel = MemberLoginViewModel(true)

    private var connectInfo :ConnectLoginModel? = null

    private var isBind :Boolean = false

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:49
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_login_lay
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:50
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 下午3:42
     * @Note   绑定视图
     */
    override fun attachView() {
        super.attachView()
        vcodePresenter.attachView(this)
        messagePresenter.attachView(this)
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 下午3:43
     * @Note   解绑视图
     */
    override fun detachView() {
        super.detachView()
        vcodePresenter.detachView()
        messagePresenter.detachView()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:53
     * @Note   初始化操作
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.data = loginViewModel
        member_login_agreement_tv.movementMethod = LinkMovementMethod.getInstance()
        member_login_agreement_tv.highlightColor = Color.TRANSPARENT
        loadVcodeImage()
    }

    override fun sendFindPwdMessage(mobile: String) {

    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:54
     * @Note   绑定事件
     */
    override fun bindEvent() {
        /**设置登录切换事件*/
        member_login_type_p_tv.setOnClickListener {
            loginViewModel.loginTypeObser.set(false)
            member_login_vcode_et.setText("")
            checkInput()
            loadVcodeImage()
        }
        member_login_type_t_tv.setOnClickListener {
            member_login_vcode_et.setText("")
            loginViewModel.loginTypeObser.set(true)
            checkInput()
            loadVcodeImage()
        }
        /**设置密码显示状态*/
        member_login_pass_show_iv.setOnClickListener {
            if (member_login_pass_et.transformationMethod is PasswordTransformationMethod){
                member_login_pass_et.transformationMethod = HideReturnsTransformationMethod.getInstance()
                member_login_pass_show_iv.setImageResource(R.drawable.javashop_icon_eye_close)
                member_login_pass_show_iv.requestFocus()
            }else{
                member_login_pass_et.transformationMethod = PasswordTransformationMethod.getInstance()
                member_login_pass_show_iv.setImageResource(R.drawable.javashop_icon_eye)
                member_login_pass_show_iv.requestFocus()
            }
        }
        /**清空密码*/
        member_login_pass_clear_iv.setOnClickListener {
            member_login_pass_et.setText("")
        }
        /**下一步*/
        member_login_confrim_tv.setOnClickListener { 
            next()
        }
        /**加载验证码*/
        member_login_vcode_iv.setOnClickListener {
            loadVcodeImage()
        }
        /**找回密码*/
        member_login_forget_tv.setOnClickListener {
            push("/member/security/phone/input",{
                postcard ->
                postcard.withInt("type",GlobalState.FIND_PASSWORD)
            })
            pop()
        }
        /**注册*/
        member_login_register_tv.setOnClickListener {
            push("/member/security/phone/input",{
                postcard ->
                postcard.withInt("type",GlobalState.REGISTER_USER)
            })
            pop()
        }
        /**第三方登录*/
        arrayOf(member_login_connect_qq,member_login_connect_wechat,member_login_connect_weibo,member_login_connect_alipay).forEach {
            imageView ->
            imageView.setOnClickListener {
                val listener = object :UMAuthListener{
                    override fun onComplete(p0: SHARE_MEDIA, p1: Int, p2: MutableMap<String, String>) {
                        when(imageView.id){
                            R.id.member_login_connect_qq ->{
                                connectInfo = ConnectLoginModel("qq",p2["unionid"]!!)
                                presenter.authLogin("qq", p2["unionid"]!!)
                            }
                            R.id.member_login_connect_wechat ->{
                                connectInfo = ConnectLoginModel("weixin",p2["unionid"]!!)
                                presenter.authLogin("weixin", p2["unionid"]!!)
                            }
                            R.id.member_login_connect_weibo ->{
                                connectInfo = ConnectLoginModel("weibo",p2["uid"]!!)
                                presenter.authLogin("weibo", p2["uid"]!!)
                            }
                        }
                    }

                    override fun onCancel(p0: SHARE_MEDIA, p1: Int) {
                        showMessage("操作取消")
                    }

                    override fun onError(p0: SHARE_MEDIA, p1: Int, p2: Throwable) {
                        errorLog("ConnectError",p2.localizedMessage)
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                        debugLog("onStart", "onStart")
                    }
                }
                when(imageView.id){
                    R.id.member_login_connect_qq ->{
                        UMShareAPI.get(activity).getPlatformInfo(activity,SHARE_MEDIA.QQ,listener)
                    }
                    R.id.member_login_connect_wechat ->{
                        UMShareAPI.get(activity).getPlatformInfo(activity,SHARE_MEDIA.WEIXIN,listener)
                    }
                    R.id.member_login_connect_weibo ->{
                        UMShareAPI.get(activity).getPlatformInfo(activity,SHARE_MEDIA.SINA,listener)
                    }
                    R.id.member_login_connect_alipay ->{
                        presenter.getAlipayAuthInfo()
                    }
                }
            }
        }
        /**监听输入状态*/
        member_login_phone_et.addTextChangedListener(this)
        member_login_user_et.addTextChangedListener(this)
        member_login_pass_et.addTextChangedListener(this)
        member_login_vcode_et.addTextChangedListener(this)
        /**设置平台协议相关*/
        val agreeHeader = "登录即代表您已同意"
        val agreeName = "《玛吉克商城隐私协议》"
        member_login_agreement_tv.text = SpannableString(agreeHeader+agreeName).then {
            self ->
            self.setSpan(object : ClickableSpan(){
                override fun onClick(widget: View?) {
                    showAgreement()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.parseColor("#e83437")
                    ds.isUnderlineText = false
                    ds.clearShadowLayer()
                }

            },agreeHeader.length,(agreeHeader+agreeName).length,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(ForegroundColorSpan(Color.parseColor("#e83437")),agreeHeader.length,(agreeHeader+agreeName).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        member_login_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 下午3:47
     * @Note   加载验证码
     */
    private fun loadVcodeImage(){
        /**加载验证码*/
        vcodePresenter.loadLoginImageVcode()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 下午3:44
     * @Note   渲染验证码
     * @param  bitmap 验证码
     */
    override fun renderVcode(bitmap: Bitmap) {
        member_login_vcode_iv.setImageBitmap(bitmap)
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:58
     * @Note   下一步操作
     */
    private fun next(){
        if (!member_login_confrim_tv.isSelected){
            return
        }
        if (loginViewModel.loginTypeObser.get()){
            if (isBind){
                presenter.authBind(connectInfo!!.type,connectInfo!!.unionId,member_login_user_et.text.toString().trim(),member_login_pass_et.text.toString().trim(),member_login_vcode_et.text.toString().trim())
            }else{
                presenter.login(member_login_user_et.text.toString().trim(),member_login_pass_et.text.toString().trim(),member_login_vcode_et.text.toString().trim())
            }
        }else{
            val phoneNum = member_login_phone_et.text.toString().trim()
            messagePresenter.sendPhoneLoginMessage(phoneNum,member_login_vcode_et.text.toString().trim())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 下午12:07
     * @Note   展示相关平台协议
     */
    private fun showAgreement(){
        push("/common/web",{
            //标题
            it.withString("title","《玛吉克商城隐私协议》")
            //URL
            it.withString("url","http://m.51gkp.com/privacy")
        })
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:54
     * @Note   刷新会员信息
     */
    override fun refreshMemberInfo(info: MemberViewModel) {
        getEventCenter().post(LoginEvent())
        pop()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/8/14 上午10:47
     * @Note   验证手机号
     * @param  mobile 手机号
     */
    override fun sendSuccess(mobile: String) {
        push("/member/security/phone/check",{
            postcard ->
            if (isBind){
                postcard.withObject("auth",connectInfo)
            }
            postcard.withInt("type",GlobalState.DYNAMIC_LOGIN)
            postcard.withString("phoneNum",mobile)
        })
        pop()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午11:05
     * @Note   字符改变之后
     */
    override fun afterTextChanged(s: Editable?) {

    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午11:05
     * @Note   字符改变之前
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午11:05
     * @Note   字符改变中
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        checkInput()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午11:09
     * @Note   检查输入是否合法
     */
    private fun checkInput(){
        if (loginViewModel.loginTypeObser.get()){
            member_login_pass_clear_iv.visibility = if(member_login_pass_et.text.isNotEmpty()){View.VISIBLE} else {View.GONE}
            member_login_confrim_tv.isSelected = member_login_user_et.text.toString().trim()!="" && member_login_pass_et.text.toString().trim()!="" && member_login_vcode_et.text.toString().trim().length == 4
        }else{
            member_login_confrim_tv.isSelected = CommonTool.isMobileNO(member_login_phone_et.text.toString().trim()) && member_login_vcode_et.text.toString().trim().length == 4
        }
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:55
     * @Note   错误回调
     * @param  message 错误信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {
        if (message != "该账号未绑定"){
            showMessage(message)
        }
        loadVcodeImage()
        dismissDialog()
    }

    override fun alipayAuthInfo(info: String) {
        Thread{
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(info.trim(), true)

            if (result["result"] == null){
                return@Thread
            }

            result["result"]!!.split("&").forEach {
                if (it.contains("alipay_open_id")){
                    val code = it.split("=")[1]
                    runOnUiThread {
                        connectInfo = ConnectLoginModel("alipay",code)
                        presenter.authLogin("alipay",code)
                    }
                }
            }

        }.start()
    }

    override fun authLoginError() {
        CommonTool.createVerifyDialog("该账号尚未绑定,请选择操作!","登录","注册",this,object : CommonTool.DialogInterface{
            override fun yes() {
                isBind = false
                push("/member/security/phone/input",{
                    postcard ->
                    postcard.withInt("type",GlobalState.REGISTER_USER)
                    postcard.withObject("auth",connectInfo)
                })
                pop()
            }

            override fun no() {
                isBind = true
            }
        }).show()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:55
     * @Note   完成回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:56
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:56
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {
        state.filter(onWifi = {

        },offline = {

        },onMobile = {

        })
    }

    /**
     * @author LDD
     * @From   MemberLoginActivity
     * @Date   2018/5/14 上午10:56
     * @Note   销毁回调
     */
    override fun destory() {

    }
}





