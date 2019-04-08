package com.enation.javashop.android.component.welcome.activity

import android.Manifest
import android.graphics.Color
import android.view.View
import com.enation.javashop.android.component.welcome.launch.WelcomeLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.welcome.WelcomeContract
import com.enation.javashop.android.middleware.logic.presenter.welcome.WelcomePresenter
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.model.AdViewModel
import com.enation.javashop.android.welcome.R
import com.enation.javashop.android.welcome.databinding.ActivityWelcomeBinding
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.plugin.permission.RxPermissions
import com.enation.javashop.net.engine.plugin.permission.RxPermissionsFragment
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.enation.javashop.utils.base.tool.CommonTool
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*


/**
 * @author LDD
 * @Date   2018/1/11 下午12:05
 * @From   com.enation.javashop.android.component.welcome.activity
 * @Note   欢迎页面
 */
@Router(path = "/welcome/into")
class WelcomeActivity :BaseActivity<WelcomePresenter,ActivityWelcomeBinding>(), WelcomeContract.View {
    /**
     * @Name  isToSetting
     * @Type  bool
     * @Note  是否已经跳转过设置页面
     */
    private var isToSetting = false

    /**
     * @author LDD
     * @From   WelcomeActivity
     * @Date   2018/1/11 下午12:06
     * @Note   提供LayoutId
     * @return LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.activity_welcome
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:07
     * @Note   依赖注入
     */
    override fun bindDagger() {
        WelcomeLaunch.component.inject(this)
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:08
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.showNavigationBar(this,false)
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this,Color.BLACK)
        requestPermissions()
    }

    private fun requestPermissions(){
        Observable.just("").compose(RxPermissions(this).ensure(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE)).subscribe {
                if (it){
                    toHome()
                }else{
                    showMessage("请重新授权，进入App")
                    requestPermissions()
                }
        }.joinManager(disposableManager)
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:08
     * @Note   跳转首页
     */
    override fun toHome() {
        AppTool.Time.delay(500) {
            push("/home/main")
            finish()
        }
    }

    /**
     * @author LDD
     * @From   WelcomeActivity
     * @Date   2018/1/11 下午12:10
     * @Note   展示广告
     * @param  data 广告信息
     */
    override fun showAd(data: ArrayList<AdViewModel>) {
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:10
     * @Note   显示错误信息
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:11
     * @Note   网络请求完成
     * @param  message 相关信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:11
     * @Note   网络请求开始
     */
    override fun start() {


    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:11
     * @Note   绑定事件
     */
    override fun bindEvent() {

    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:12
     * @Note   页面显示时调用 这判断网络是否正常
     */
    override fun onResume() {
        super.onResume()
        reloadWithVoiNet()
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:12
     * @Note   页面销毁时调用
     */
    override fun destory() {
        debugLog("WelcomeActivity","Destory")
    }

    /**
     * @author LDD
     * @From   WelcomeActivity
     * @Date   2018/1/11 下午12:13
     * @Note   网络变化回调
     * @param  state  网络状态
     */
    override fun networkMonitor(state: NetState) {
        state.filter(onMobile = {
            debugLog("Welcome","mobile")
        },onWifi = {
            debugLog("Welcome","wifi")
        },offline = {
            noneNetDo()
        })
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:15
     * @Note   没有网时，执行以下操作
     */
    private fun noneNetDo(){
        CommonTool.createVerifyDialog("无网络","退出程序","设置网络",activity,object : CommonTool.DialogInterface {
            override fun yes() {
                AppTool.Time.delay(300) {
                    AppTool.Setting.systemNetSetting(this@WelcomeActivity)
                    isToSetting = true
                }
            }

            override fun no() {
                AppTool.Time.delay(300) {
                    finish()
                }
            }
        }).show()
    }

    /**
     * @author  LDD
     * @From    WelcomeActivity
     * @Date   2018/1/11 下午12:16
     * @Note   重新加载 并且验证网络
     */
    private fun reloadWithVoiNet(){
        if (isToSetting){
            isToSetting = false
            showDialog()
            AppTool.Time.delay(1000) {
                dismissDialog()
                presenter.loadAd()
            }
        }
    }

    /**
     * 重写返回监听事件 直接销毁页面
     */
    override fun onBackPressed() {
        finish()
    }
}