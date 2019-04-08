package com.enation.javashop.android.component.member.activity

import android.databinding.ObservableField
import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.CouponAdapter
import com.enation.javashop.android.component.member.databinding.MemberCouponLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.component.member.vm.MemberCoupoonViewModel
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.middleware.logic.contract.member.MemberCouponConract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberCouponPresenter
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.member_coupon_lay.*

/**
 * @author LDD
 * @Date   2018/5/3 下午4:32
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员优惠券页面
 */
@Router(path = "/member/coupon/list")
class MemberCouponActivity :BaseActivity<MemberCouponPresenter,MemberCouponLayBinding>(),MemberCouponConract.View {

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private lateinit var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @Name  bindingData
     * @Type  MemberCoupoonViewModel
     * @Note  VM
     */
    private val bindingData = MemberCoupoonViewModel(ObservableField(0))

    private var page :Int = 1

    private var state :Int = 1

    /**
     * @Name  adapter
     * @Type  CouponAdapter
     * @Note  优惠券适配器
     */
    private val adapter = CouponAdapter(ArrayList())

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:35
     * @Note   获取layoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_coupon_lay
    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:36
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:37
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.data = bindingData
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        member_coupon_rv.layoutManager = virtualLayoutManager
        member_coupon_rv.adapter = delegateAdapter
        delegateAdapter.addAdapter(adapter)
        presenter.loadCoupon(page,state)
        refresh.setEnableRefresh(false)
        refresh.setRefreshFooter(ClassicsFooter(this))
        refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadCoupon(page,state)
        }
    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:38
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_coupon_use_lay.setOnClickListener {
            bindingData.state.set(1)
            page = 1
            state = 2
            presenter.loadCoupon(page,state)
        }
        member_coupon_unuse_lay.setOnClickListener {
            bindingData.state.set(0)
            page = 1
            state = 1
            presenter.loadCoupon(page,state)
        }
        member_coupon_dateed_lay.setOnClickListener {
            bindingData.state.set(2)
            page = 1
            state = 3
            presenter.loadCoupon(page,state)
        }
        member_coupon_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:38
     * @Note   渲染优惠券
     * @param  data 数据
     */
    override fun renderCoupon(data: ArrayList<CouponViewModel>) {
        if (page == 1){
            adapter.data.clear()
            refresh.resetNoMoreData()
        }
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
        if (data.size == 0){
            refresh.finishLoadMoreWithNoMoreData()
        }else{
            refresh.finishLoadMore()
        }
    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:38
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:39
     * @Note   完成回调
     * @param  信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:39
     * @Note   开始加载
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:39
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberCouponActivity
     * @Date   2018/5/3 下午4:40
     * @Note   销毁回调
     */
    override fun destory() {

    }
}