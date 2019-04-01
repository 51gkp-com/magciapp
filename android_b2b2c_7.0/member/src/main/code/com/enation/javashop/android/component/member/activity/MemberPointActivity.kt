package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import android.os.Handler
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.PointAdapter
import com.enation.javashop.android.component.member.databinding.MemberPointLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.middleware.logic.contract.member.MemberPointContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberPointPresenter
import com.enation.javashop.android.middleware.model.PointViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.member_point_lay.*

/**
 * @author LDD
 * @Date   2018/5/4 下午5:06
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员积分页面
 */
@Router(path = "/member/point/main")
class MemberPointActivity :BaseActivity<MemberPointPresenter,MemberPointLayBinding>(),MemberPointContract.View {

    private var page : Int = 1

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


    @Autowired(name = "title",required = false)
    @JvmField var title  :String = "积分明细"

    /**
     * @Name  adapter
     * @Type  PointAdapter
     * @Note  积分适配器
     */
    private val adapter =PointAdapter(ArrayList())

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:06
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_point_lay
    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:07
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:08
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        point_rv.layoutManager = virtualLayoutManager
        point_rv.adapter = delegateAdapter
        delegateAdapter.addAdapter(adapter)
        presenter.loadPointData(page,0)
        point_topbar.setTitleText(title)
        refresh.setRefreshFooter(ClassicsFooter(this))
        refresh.setRefreshHeader(ClassicsHeader(this))
    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:09
     * @Note   绑定事件
     */
    override fun bindEvent() {
        refresh.setOnRefreshListener {
            page = 1
            presenter.loadPointData(page,0)
        }
        refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadPointData(page,0)
        }
        point_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:09
     * @Note   渲染积分UI
     * @param  data 积分数据
     */
    override fun renderPointUi(data: ArrayList<PointViewModel>) {
        if (page == 1){
            adapter.data.clear()
            refresh.resetNoMoreData()
            refresh.finishRefresh()
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
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:10
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:10
     * @Note   完成信息
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:10
     * @Note   请求开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:10
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberPointActivity
     * @Date   2018/5/4 下午5:11
     * @Note   销毁回调
     */
    override fun destory() {

    }
}