package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberPasswordLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.widget.VcodeDialog
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.android.middleware.logic.contract.member.MemberPasswordContract
import com.enation.javashop.android.middleware.logic.presenter.member.ImageVcodePresenter
import com.enation.javashop.android.middleware.logic.presenter.member.MemberPasswordPresenter
import com.enation.javashop.android.middleware.model.ConnectLoginModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.member_password_lay.*
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/11 下午1:44
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员密码页面
 */
@Router(path = "/member/security/password/main")
class MemberPasswordActivity :BaseActivity<MemberPasswordPresenter,MemberPasswordLayBinding>(),MemberPasswordContract.View,TextWatcher {


    @Autowired(name = "auth",required = false)
    @JvmField var connectInfo : ConnectLoginModel? = null

    /**
     * @Name  vcodePresenter
     * @Type  ImageVcodePresenter
     * @Note  验证码逻辑控制器
     */
    @Inject
    lateinit var vcodePresenter : ImageVcodePresenter

    /**
     * @Name  type
     * @Type  Int
     * @Note  类型
     */
    @Autowired(name = "type",required = true)
    @JvmField var type = 1

    /**
     * @Name  phoneNum
     * @Type  String
     * @Note  手机号
     */
    @Autowired(name = "phoneNum",required = true)
    @JvmField var phoneNum = "13393362615"

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:47
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_password_lay
    }

    /**
     * @author LDD
     * @From    MemberPassworActivity
     * @Date   2018/5/11 下午1:47
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:48
     * @Note   初始化
=     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        member_password_et.addTextChangedListener(this)
        var topbarTitle = ""
        when(type){
            GlobalState.REGISTER_USER ->{
                topbarTitle = "账号注册"
            }
            GlobalState.FIND_PASSWORD ->{
                topbarTitle = "找回密码"
            }
            GlobalState.EDIT_PASSWORD ->{
                topbarTitle = "修改密码"
            }
        }

        member_password_topbar.setTitleText(topbarTitle)
                              .setLeftClickListener {
                                    pop()
                              }
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:48
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_password_confrim.setOnClickListener {
            confrim()
        }
        member_password_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/10 下午3:22
     * @Note   绑定Presenter 使用多个Presenter时重写
     */
    override fun attachView() {
        super.attachView()
        vcodePresenter.attachView(this)
    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/10 下午3:22
     * @Note   解除Presenter 使用多个Presenter时重写
     */
    override fun detachView() {
        super.detachView()
        vcodePresenter.detachView()
    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/14 下午1:30
     * @Note   提交
     */
    private fun confrim(){
        val password = member_password_et.text.toString().trim()
        when(type){
            GlobalState.REGISTER_USER ->{
                if (connectInfo != null){
                    VcodeDialog.build(this).config({vcode ->
                        presenter.registerConnectUser(connectInfo!!.type,connectInfo!!.unionId,password,phoneNum,vcode)
                    },{imageView ->
                        vcodePresenter.imageLoader = {
                            imageView.setImageBitmap(it)
                        }
                        vcodePresenter.loadRegisterImageVcode()
                    })
                }else{
                    presenter.registerUser(password,phoneNum)
                }
            }
            GlobalState .EDIT_PASSWORD ->{
                VcodeDialog.build(this).config({
                  vcode ->
                    presenter.editPassword(password,vcode)
                },{
                    iv ->
                    vcodePresenter.imageLoader = { image ->
                        run {
                            iv.setImageBitmap(image)
                        }
                    }
                    loadVcode()
                    /**加载验证码操作*/
                }).show()
            }
            GlobalState.FIND_PASSWORD ->{
                presenter.findPassword(password,"")
            }
        }
    }

    private fun loadVcode(){
        vcodePresenter.loadEditImageVcode()
    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/14 下午1:32
     * @Note   文字修改后
     */
    override fun afterTextChanged(s: Editable?) {

    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/14 下午1:33
     * @Note   文字修改前
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    /**
     * @author LDD
     * @From   MemberPasswordActivity
     * @Date   2018/5/14 下午1:33
     * @Note   文字修改中
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        member_password_confrim.isSelected = member_password_et.text.toString().trim().isNotEmpty()
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:48
     * @Note   错误回调
     * @param  message 错误信息
     * @param  type 类型
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:49
     * @Note   完成回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        when(type){
            GlobalState.FIND_PASSWORD ->{

            }
            GlobalState.EDIT_PASSWORD ->{
                getEventCenter().post(LogoutEvent())
            }
            GlobalState.REGISTER_USER ->{
                getEventCenter().post(LoginEvent())
            }
        }
        pop()
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:49
     * @Note   开始请求
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:50
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberPassworActivity
     * @Date   2018/5/11 下午1:50
     * @Note   销毁回调
     */
    override fun destory() {

    }
}