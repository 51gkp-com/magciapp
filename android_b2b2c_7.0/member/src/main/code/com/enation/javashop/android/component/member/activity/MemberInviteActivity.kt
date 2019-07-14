package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.MemberInviteAdapter
import com.enation.javashop.android.component.member.databinding.MemberInviteLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.AddressListDataChange
import com.enation.javashop.android.middleware.logic.contract.member.MemberInviteContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberInvitePresenter
import com.enation.javashop.android.middleware.model.InviteViewModel
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.member_invite_lay.*

/**
 * @author LDD
 * @Date   2018/5/7 下午4:44
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   邀请列表页面
 */
@Router(path = "/member/invite/list")
class MemberInviteActivity :BaseActivity<MemberInvitePresenter, MemberInviteLayBinding>() , MemberInviteContract.View {

    @Autowired(name = "uname",required = true)
    @JvmField var uname :String? = null

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
     * @Name  adapter
     * @Type  MemberAddressAdapter
     * @Note  地址列表适配器
     */
    val adapter = MemberInviteAdapter(ArrayList())

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页查询
     */
    private var page = 1

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:47
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_invite_lay
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:47
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:48
     * @Note   初始化操作
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        member_invite_rv.layoutManager = virtualLayoutManager
        member_invite_rv.adapter = delegateAdapter
        delegateAdapter.addAdapter(adapter)
        presenter.loadInviteList(page, uname!!)
        refresh.setRefreshFooter(ClassicsFooter(this))
        refresh.setEnableRefresh(false)
        refresh.setEnableLoadMore(false)
        refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadInviteList(page, uname!!)
        }
    }

    fun getApi() :MemberInvitePresenter{
        return presenter
    }


    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:48
     * @Note   绑定事件
     */
    override fun bindEvent() {
        member_invite_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:48
     * @Note   渲染地址列表UI
     * @param  data 地址数据
     */
    override fun renderAddress(data: ArrayList<InviteViewModel>) {
        if(page == 1){
            refresh.resetNoMoreData()
            adapter.data.clear()
        }
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
        if(adapter.data.size == 0){
            refresh.finishLoadMoreWithNoMoreData()
        }else{
            refresh.finishLoadMore()
        }
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:49
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:50
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:50
     * @Note   开始回调
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:53
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberAddressActivity
     * @Date   2018/5/7 下午4:53
     * @Note   销毁
     */
    override fun destory() {

    }
}