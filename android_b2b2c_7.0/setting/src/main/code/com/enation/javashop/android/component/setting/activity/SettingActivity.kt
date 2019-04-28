package com.enation.javashop.android.component.setting.activity

import android.graphics.BitmapFactory
import android.graphics.Color
import com.enation.javashop.android.component.setting.R
import com.enation.javashop.android.component.setting.databinding.SettingActLayBinding
import com.enation.javashop.android.component.setting.launch.SettingLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.api.TokenManager
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.android.middleware.logic.contract.setting.SettingActivityContract
import com.enation.javashop.android.middleware.logic.presenter.setting.SettingActivityPresenter
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.connectview.logic.UmengShare
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.enation.javashop.utils.base.tool.CommonTool
import com.umeng.socialize.utils.Log
import io.reactivex.Observable
import kotlinx.android.synthetic.main.about_act_lay.*
import kotlinx.android.synthetic.main.setting_act_lay.*
import java.util.*

/**
 * @author LDD
 * @Date   2018/3/6 下午4:11
 * @From   com.enation.javashop.android.component.setting.activity
 * @Note   设置页面
 */
@Router(path = "/setting/main")
class SettingActivity :BaseActivity<SettingActivityPresenter,SettingActLayBinding>(),SettingActivityContract.View {

    /**
     * @Name  logoutDialog
     * @Type  Dialog
     * @Note  退出登录Dialog
     */
    private val logoutDialog by lazy {
        CommonTool.createVerifyDialog("退出登录？","取消","退出",this, object : CommonTool.DialogInterface{

            override fun yes() {
                TokenManager(activity).getUid()?.more { uid ->
                    presenter.logout(uid)
                }
            }

            override fun no() {

            }

        })
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:05
     * @Note   提供布局ID
     * @return 布局ID
     */
    override fun getLayId(): Int {
        return R.layout.setting_act_lay
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:05
     * @Note   依赖注入
     */
    override fun bindDagger() {
        SettingLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:05
     * @Note   初始化
     */
    override fun init() {
        /**沉浸式*/
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.getCacheSize(activity)
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:05
     * @Note   绑定事件
     */
    override fun bindEvent() {
        /**设置关于Item事件*/
        setting_action_about.setOnClickListener {
            push("/setting/about")
        }
        setting_action_userinfo.setOnClickListener {
            push("/member/privacy/info/edit")
        }
        setting_action_share.setOnClickListener {
            UmengShare.Init(activity.weak().get())
                    .web("https://sj.qq.com/myapp/detail.htm?apkName=com.bj.magic.shop")
                    .setWebTitle("玛吉克商城App下载")
                    .setWebImage(BitmapFactory.decodeResource(BaseApplication.appContext.resources,R.mipmap.launcher))
                    .setWebDescription("玛吉克商城")
                    .webShare()
        }
        /**设置返回*/
        setting_top_bar.setLeftClickListener {
            toolBack()
        }
        /**缓存清除*/
        setting_action_cache.setOnClickListener {
            CommonTool.createVerifyDialog("确认清除缓存 \n 清除缓存后，所有图片及缓存信息需要重新请求网络！","取消","确认",this,object :CommonTool.DialogInterface{
                override fun yes() {
                    presenter.clearChche(activity)
                }

                override fun no() {

                }
            }).show()
        }
        setting_action_logout.setOnClickListener {
            logoutDialog.show()
        }

        setting_top_bar.setLeftClickListener {
            pop()
        }
        getEventCenter().register(LoginEvent::class.java) {
            MemberState.manager.info()?.more { info ->
                mViewBinding.data = info
            }
        }.joinManager(disposableManager)
        presenter.loadMemberInfo()
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:06
     * @Note   展示会员信息
     * @param  member 会员信息数据
     */
    override fun renderInfo(member: MemberViewModel) {
        mViewBinding.data = member
    }

    /**
     * @author LDD
     * @From   View
     * @Date   2018/8/13 下午9:12
     * @Note   退出登录
     */
    override fun logout() {
        mViewBinding.data = null
        getEventCenter().post(LogoutEvent())
        pop()
    }

    override fun renderCacheSize(size: String) {
        cache_tv.text = size
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:06
     * @Note   错误展示
     * @param  message 错误信息提示
     */
    override fun onError(message: String, type: Int) {
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:06
     * @Note   操作完成时信息提示
     * @param  完成信息提示
     */
    override fun complete(message: String, type: Int) {
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:07
     * @Note   耗时请求开始
     */
    override fun start() {
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:07
     * @Note   网络实时监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {
    }

    /**
     * @author LDD
     * @From   SettingActivity
     * @Date   2018/3/6 下午4:07
     * @Note   页面销毁时调回
     */
    override fun destory() {
    }
}