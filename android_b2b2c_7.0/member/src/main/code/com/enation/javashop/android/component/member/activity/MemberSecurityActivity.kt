package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberSecurityLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.member.MemberSecurityContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberSecurityPresenter
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import kotlinx.android.synthetic.main.member_security_lay.*

/**
 * @author LDD
 * @Date   2018/5/9 上午11:16
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员安全页面
 */
@Router(path = "/member/security/main")
class MemberSecurityActivity :BaseActivity<MemberSecurityPresenter,MemberSecurityLayBinding>(),MemberSecurityContract.View {

    private lateinit var member :MemberViewModel

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:17
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_security_lay
    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:17
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:17
     * @Note   初始化
=     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.load()
    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:18
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_security_editpass.setOnClickListener {
            push("/member/security/phone/check",{
                postcard ->  
                postcard.withInt("type",GlobalState.EDIT_PASSWORD)
                postcard.withString("phoneNum",member.mobile)
            })
        }
        member_security_bindphone.setOnClickListener {
            var message: String
            var action : () ->Unit
            if (member_security_bindphone_hint.text == "未绑定"){
                message = "您尚未绑定手机号，是否跳转界面进行手机号绑定"
                action = {
                    push("/member/security/phone/input",{
                        postcard ->
                        postcard.withInt("type",GlobalState.BIND_PHONE)
                    })
                }
            }else{
                message = "您已绑定手机号，是否更换绑定手机号！"
                action = {
                    push("/member/security/phone/check",{
                        postcard ->
                        postcard.withString("phoneNum",member.mobile)
                        postcard.withInt("type",GlobalState.UPDATE_PHONE)
                    })
                }
            }
            CommonTool.createVerifyDialog(message,"取消","确定",this,object :CommonTool.DialogInterface{
                override fun yes() {

                    action.invoke()
                }

                override fun no() {
                    showMessage("取消操作")
                }
            }).show()
        }
        member_security_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:19
     * @Note   渲染UI
     * @param  bindPhoneText 绑定状态
     */
    override fun renderUI(data: MemberViewModel) {
        member = data
        data.mobile?.then {mobile ->
            if(mobile.isEmpty()){
                member_security_bindphone_hint.text = "未绑定手机号码"
            }else{
                member_security_bindphone_hint.text = "已绑定$mobile"
            }
        }

    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:19
     * @Note   错误回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:19
     * @Note   完成回调
     * @param  message 回调信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:20
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:20
     * @Note   网络监听
     * @param  state 网络监听
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberSecurityActivity
     * @Date   2018/5/9 上午11:21
     * @Note   销毁回调
     */
    override fun destory() {

    }
}