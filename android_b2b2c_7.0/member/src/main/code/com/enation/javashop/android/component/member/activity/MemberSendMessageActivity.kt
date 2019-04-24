package com.enation.javashop.android.component.member.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberSendMessageLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.member.ImageVcodeContract
import com.enation.javashop.android.middleware.logic.contract.member.MemberSendMessageContract
import com.enation.javashop.android.middleware.logic.presenter.member.ImageVcodePresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberSendMessagePresenter
import com.enation.javashop.android.middleware.model.ConnectLoginModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import kotlinx.android.synthetic.main.member_login_lay.*
import kotlinx.android.synthetic.main.member_send_message_lay.*
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/10 下午1:56
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员发送信息页面
 */
@Router(path = "/member/security/phone/input")
class MemberSendMessageActivity :BaseActivity<MemberSendMessagePresenter,MemberSendMessageLayBinding>(),MemberSendMessageContract.View,TextWatcher ,ImageVcodeContract.View{

    @Autowired(name = "auth",required = false)
    @JvmField var connectInfo : ConnectLoginModel? = null

    /**
     * @Name  type
     * @Type  Int
     * @Note  类型
     */
    @Autowired(name = "type" ,required = true)
    @JvmField var type = GlobalState.BIND_PHONE


    /**
     * @Name  phoneNum
     * @Type  String
     * @Note  手机号
     */
    @Autowired(name = "newPhone",required = false)
    @JvmField var newPhone = false


    /**
     * @Name  vcodePresenter
     * @Type  ImageVcodePresenter
     * @Note  验证码逻辑控制器
     */
    @Inject
    lateinit var vcodePresenter : ImageVcodePresenter

    /**
     * @Name  phoneNum
     * @Type  String
     * @Note  手机号
     */
    private var phoneNum = ""
    
    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午1:59
     * @Note   获取layoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_send_message_lay
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:01
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/14 下午3:42
     * @Note   绑定视图
     */
    override fun attachView() {
        super.attachView()
        vcodePresenter.attachView(this)
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/14 下午3:43
     * @Note   解绑视图
     */
    override fun detachView() {
        super.detachView()
        vcodePresenter.detachView()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:02
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        var title = ""
        when(type){
            GlobalState.BIND_PHONE ->{
                member_send_message_title.text = "请输入需要绑定的手机号"
                title = "绑定手机号"
                member_send_message_register_tv.visibility = View.GONE
            }
            GlobalState.REGISTER_USER ->{
                member_send_message_title.text = "欢迎注册玛吉克商城账号"
                title = "注册"
            }
            GlobalState.FIND_PASSWORD ->{
                member_send_message_title.text = "请输入已绑定手机号"
                member_send_message_register_tv.visibility = View.GONE
                title = "忘记密码"
                member_send_message_send_tv.text = "验证账户"
            }
            GlobalState.UPDATE_PHONE ->{
                title = "更换手机号"
                if (newPhone){
                    member_send_message_title.text = "请输入需要绑定的手机号"
                    member_send_message_register_tv.visibility = View.GONE
                }else{
                    member_send_message_title.text = "请输入当前解绑手机号"
                    member_send_message_register_tv.visibility = View.GONE
                }
            }
        }
        member_send_message_topbar.setLeftClickListener {
            pop()
        }.setTitleText(title)
        loadVcode()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:02
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_send_message_iphone_et.addTextChangedListener(this)
        member_send_message_vcode_et.addTextChangedListener(this)
        member_send_message_send_tv.setOnClickListener {
            if (it.isSelected){
                sendMessage()
            }
        }
        /**设置平台协议相关*/
        val agreeHeader = "点击按钮即代表您已同意"
        val agreeName = "《玛吉克商城使用协议》"
        member_send_message_register_tv.text = SpannableString(agreeHeader+agreeName).then {
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
        member_send_message_register_tv.setOnClickListener {
            showAgreement()
        }
        member_send_message_vcode_iv.setOnClickListener {
            loadVcode()
        }
        member_send_message_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun sendFindPwdMessage(mobile: String) {
        member_send_message_send_tv.text = "发送验证码"
        member_send_message_iphone_et.text.clear()
        member_send_message_vcode_et.text.clear()
        loadVcode()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/14 下午12:07
     * @Note   展示相关平台协议
     */
    private fun showAgreement(){
        push("/common/web",{
            it.withString("title","《玛吉克商城使用协议》")
            it.withString("url","http://m.buyer.javamall.com.cn/")
        })
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:36
     * @Note   发送短信
     */
    private fun sendMessage(){
        phoneNum = member_send_message_iphone_et.text.toString().trim()
        val sendVcode = member_send_message_vcode_et.text.toString().trim()
        when(type){
            GlobalState.REGISTER_USER ->{
                presenter.sendRegisterVcode(phoneNum,sendVcode)
            }
            GlobalState.BIND_PHONE ->{
                presenter.sendBindPhoneNumVcode(phoneNum,sendVcode)
            }
            GlobalState.FIND_PASSWORD ->{
                if (member_send_message_send_tv.text.contains("验证账户")){
                    presenter.checkFindPwdAccount(phoneNum,sendVcode)
                }else{
                    presenter.sendFindPasswordVcode(phoneNum,sendVcode)
                }
            }
            GlobalState.UPDATE_PHONE ->{
                presenter.sendBindPhoneNumVcode(phoneNum,sendVcode)
            }
        }
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
                vcodePresenter.loadBindImageVcode()
            }
            GlobalState.FIND_PASSWORD ->{
                vcodePresenter.loadFindImageVcdoe()
            }
            GlobalState.UPDATE_PHONE ->{
                vcodePresenter.loadBindImageVcode()
            }
        }
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/8/14 上午11:44
     * @Note   渲染验证码
     * @param  bitmap 图片
     */
    override fun renderVcode(bitmap: Bitmap) {
        member_send_message_vcode_iv.setImageBitmap(bitmap)
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:41
     * @Note   下一步
     */
    private fun next(){
        push("/member/security/phone/check",{
            postcard ->
            postcard.withString("phoneNum",phoneNum)
            postcard.withInt("type",type)
            postcard.withBoolean("newPhone",newPhone)
            if(connectInfo!=null){
                postcard.withObject("auth",connectInfo)
            }
        })
        pop()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:02
     * @Note   错误
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        loadVcode()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:03
     * @Note   完成
     * @param  message 信息
     * @param  type 类型
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
        if (type == presenter.Send){
            next()
        }
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/8/14 上午11:28
     * @Note   发送成功
     * @param  mobile 手机号
     */
    override fun sendSuccess(mobile: String) {
        showMessage("发送成功")
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:03
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:04
     * @Note   ET更改后调用
     */
    override fun afterTextChanged(s: Editable?) {

    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:05
     * @Note   ET更改之前调用
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:05
     * @Note   ET正在更改
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        member_send_message_send_tv.isSelected = CommonTool.isMobileNO(member_send_message_iphone_et.text.toString().trim())
        if (CommonTool.isMobileNO(member_send_message_iphone_et.text.toString().trim())){
            member_send_message_vcode_baseline.visibility = View.VISIBLE
            member_send_message_vcode_et.visibility = View.VISIBLE
            member_send_message_vcode_iv.visibility = View.VISIBLE
            member_send_message_vcode_header.visibility = View.VISIBLE
        }
        if (CommonTool.isMobileNO(member_send_message_iphone_et.text.toString().trim()) && member_send_message_vcode_et.text.toString().trim().length == 4){
            member_send_message_send_tv.isSelected = true
            member_send_message_send_tv.setTextColor(Color.WHITE)
        }else{
            member_send_message_send_tv.isSelected = false
            member_send_message_send_tv.setTextColor(Color.parseColor("#ccffffff"))
        }
    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:03
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberSendMessageActivity
     * @Date   2018/5/10 下午2:03
     * @Note   销毁
     */
    override fun destory() {

    }
}