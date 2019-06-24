package com.enation.javashop.android.component.home.activity

import android.graphics.Color
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.databinding.HomeActLayBinding
import com.enation.javashop.android.component.home.fragment.CategoryFragment
import com.enation.javashop.android.component.home.fragment.HomeFragment
import com.enation.javashop.android.component.home.launch.HomeLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.lib.widget.UpdateDialog
import com.enation.javashop.android.middleware.logic.contract.home.HomeActivityContract
import com.enation.javashop.android.middleware.logic.presenter.home.HomeActivityPresenter
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.widget.navigationview.NavigationModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import kotlinx.android.synthetic.main.home_act_lay.*

/**
 * @author  LDD
 * @Date   2018/1/16 下午1:35
 * @From   com.enation.javashop.android.component.home.activity
 * @Note   首页Activity
 */
@Router(path = "/home/main")
class HomeActivity : BaseActivity<HomeActivityPresenter,HomeActLayBinding>(),HomeActivityContract.View {

    /**
     * @Name  clickTime
     * @Type  Long
     * @Note  返回键点击间隔时间
     */
    private var clickTime :Long = 0


    private lateinit var updateDialog: UpdateDialog

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:37
     * @Note   提供LayoutId
     * @return LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.home_act_lay
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:38
     * @Note   依赖注入
     */
    override fun bindDagger() {
        HomeLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:38
     * @Note   页面初始化
     */
    override fun init() {
        homePageDo()
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this,Color.BLACK)
        mViewBinding.homeActViewpager.setFragments(arrayListOf(HomeFragment(),CategoryFragment(), acquireInstance("/cart/fragment"), acquireInstance("/member/root")),supportFragmentManager)
        mViewBinding.homeActNavigationview.then {
            self ->
            self.setNomalColor(R.color.javashop_color_navigation_nomal)
            self.setSelectColor(R.color.javashop_color_navigation_select)
            self.withViewPager(mViewBinding.homeActViewpager)
        }
        presenter.isLogin()
        presenter.checkUpdate()
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:39
     * @Note   绑定事件
     */
    override fun bindEvent() {
        mViewBinding.homeActNavigationview.then {
            self ->
            val home = NavigationModel("首页",R.drawable.javashop_navigation_home_select,R.drawable.javashop_navigation_home_nomal)
            val category = NavigationModel("分类",R.drawable.javashop_navigation_category_select,R.drawable.javashop_navigation_category_nomal)
            val cart = NavigationModel("购物车",R.drawable.javashop_navigation_cart_select,R.drawable.javashop_navigation_cart_nomal)
            val person = NavigationModel("我的",R.drawable.javashop_navigation_person_select,R.drawable.javashop_navigation_person_nomal)
            self.setData(arrayListOf(home,category,cart,person)) { index ->
                home_act_viewpager.setCurrentItem(index,false)
            }
        }
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:39
     * @Note   页面销毁回调
     */
    override fun destory() {
        debugLog("HomeActivity","Destory")
    }

    fun toMember(){
        home_act_viewpager.setCurrentItem(3,false)
    }

    fun toCategory(){
        home_act_viewpager.setCurrentItem(1,false)
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:40
     * @Note   判断用户登录状态
     * @param  userInfo  用户信息
     */
    override fun onUserState(userInfo: MemberViewModel) {

    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:41
     * @Note   显示错误信息
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:41
     * @Note   显示完成信息
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   HomeActivity
     * @Date   2018/1/16 下午1:42
     * @Note   操作开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From    HomeActivity
     * @Date   2018/1/29 上午9:21
     * @Note   ...
     * @param  ...
     */
    override fun onBackPressed() {
        if(System.currentTimeMillis() - clickTime < 1000){
            super.onBackPressed()
        }else{
            clickTime = System.currentTimeMillis()
            showMessage("再按一次退出玛吉克商城")
        }
    }

    /**
     * @author LDD
     * @From   WelcomeActivity
     * @Date   2018/1/16 下午1:45
     * @Note   网络变化回调
     * @param  state  网络状态
     */
    override fun networkMonitor(state: NetState) {
        state.filter(onMobile = {

        },onWifi = {

        },offline = {

        })
    }

    override fun showUpdate() {
        updateDialog = UpdateDialog(this, object : CommonTool.DialogInterface{
            override fun yes() {
                updateDialog.update(10)
            }

            override fun no() {
            }

        })
        updateDialog.show()
    }
}