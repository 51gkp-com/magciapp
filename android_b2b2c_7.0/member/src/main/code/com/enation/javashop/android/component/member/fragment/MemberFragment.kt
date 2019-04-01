package com.enation.javashop.android.component.member.fragment

import android.content.Intent
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.MemberInfoAdapter
import com.enation.javashop.android.component.member.adapter.MemberRecommendGoodsAdapter
import com.enation.javashop.android.component.member.adapter.MemberRecommendTitleAdapter
import com.enation.javashop.android.component.member.databinding.MemberFragLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.android.middleware.logic.contract.member.MemberFragmentContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberFragmentPresenter
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RecommendGoodsViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.member_frag_lay.*

/**
 * @author LDD
 * @Date   2018/2/24 下午6:16
 * @From   com.enation.javashop.android.component.member.fragment
 * @Note   会员Fragment
 */
@Router(path = "/member/root")
class MemberFragment : BaseFragment<MemberFragmentPresenter, MemberFragLayBinding>(), MemberFragmentContract.View {
    /**
     * @Name  adapterList
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapterList: ArrayList<DelegateAdapter.Adapter<*>> = ArrayList()

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
     * @Name  topbarHeight
     * @Type  Int
     * @Note  标题栏高度 为当前设备屏幕高度的13%
     */
    private var topbarHeight = 0

    /**
     * @Name  page
     * @Type  Int
     * @Note  推荐商品分页
     */
    private var page = 1

    /**
     * @Name  rightImageClick
     * @Type  Block
     * @Note  右侧图标点击事件
     */
    private val rightImageClick = { _: View ->
        push("/setting/main")
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:16
     * @Note   获取layoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_frag_lay
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:17
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:17
     * @Note   执行初始化操作
     */
    override fun init() {
        /**初始化TopBar*/
        mViewDataBinding.memberTopbar
                .setTitleText("我的")
                .setTitleVisibility(false)
                .setRightImage(R.drawable.javashop_setting_white)
                .setBackgroundAlpha(0)
                .setRightClickListener(rightImageClick)
                .setTopHolderVisibility(false)
                .setRightImageSize(7f)

        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mViewDataBinding.memberReclyLayout.layoutManager = virtualLayoutManager
        mViewDataBinding.memberReclyLayout.adapter = delegateAdapter

        /**添加用户信息Adapter*/
        adapterList.add(MemberInfoAdapter(weak(),MemberViewModel()).then {
            self ->
            self.tag = "member"
        })
        delegateAdapter.addAdapters(adapterList)
        presenter.loadMemberInfo()
        presenter.loadGuessLike(page)
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:17
     * @Note   绑定事件或数据
     */
    override fun bindEvent() {
        initRefreshView()
        initRecyclerView()
        getEventCenter().register(LoginEvent::class.java) {
            MemberState.manager.info()?.more {
                showMember(it)
            }
        }.joinManager(disposableManager)
        getEventCenter().register(LogoutEvent::class.java) {
            showMember(MemberViewModel())
        }.joinManager(disposableManager)
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/3/15 下午4:24
     * @Note   initRecyclerView
     */
    private fun initRecyclerView(){
        mViewDataBinding.memberReclyLayout.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val scrollY = virtualLayoutManager.getScollYDistance()
                if (scrollY > topbarHeight) {
                    mViewDataBinding.memberTopbar.setRightImage(R.drawable.javashop_setting_black)
                    mViewDataBinding.memberTopbar.setTitleVisibility(true)
                } else {
                    mViewDataBinding.memberTopbar.setRightImage(R.drawable.javashop_setting_white)
                    mViewDataBinding.memberTopbar.setTitleVisibility(false)
                }
                mViewDataBinding.memberTopbar.setBackgroundAlpha(if (scrollY < 0) 0 else (scrollY <= 255).judge(scrollY, 255))
                if (scrollY > 10) {
                    mViewDataBinding.memberTopbar.setTopHolderVisibility(true)
                } else {
                    mViewDataBinding.memberTopbar.setTopHolderVisibility(false)
                }
            }
        })
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/3/15 下午4:25
     * @Note   初始化刷新视图
     */
    private fun initRefreshView() {

        /**设置下拉监听*/
        mViewDataBinding.memberRefreshLayout.setOnMultiPurposeListener(SmartHideViewHelper({
            member_topbar.setVisibility(false)
            member_recly_layout.setOnTouchListener { _, _ ->
                true
            }
        }, {
            member_topbar.setVisibility(true)
            member_recly_layout.setOnTouchListener { _, _ ->
                false
            }
        }))

        /**开启刷新*/
        mViewDataBinding.memberRefreshLayout.setHeaderHeight((ScreenTool.getScreenWidth(activity) * 0.5).pxToDp().toFloat())
        mViewDataBinding.memberRefreshLayout.setEnableLoadMore(true)
        mViewDataBinding.memberRefreshLayout.setEnableRefresh(true)
        mViewDataBinding.memberRefreshLayout.setRefreshHeader(ClassicsHeader(activity))
        mViewDataBinding.memberRefreshLayout.setRefreshFooter(ClassicsFooter(activity))
        /**设置刷新监听事件*/
        mViewDataBinding.memberRefreshLayout.setOnLoadMoreListener {
            page += 1
            presenter.loadGuessLike(page)
        }
        mViewDataBinding.memberRefreshLayout.setOnRefreshListener {
            page = 1
            presenter.loadGuessLike(page)
        }
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:17
     * @Note   渲染会员数据
     * @param  member 会员信息数据
     */
    override fun showMember(member: MemberViewModel) {
        adapterList.getAdapterByTag<MemberInfoAdapter>("member")?.member = member
        adapterList.getAdapterByTag<MemberInfoAdapter>("member")?.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:18
     * @Note   渲染猜你喜欢数据
     * @param  goods 猜你喜欢数据
     */
    override fun showGuessLike(goods: List<RecommendGoodsViewModel>) {
        if (page == 1){
            member_refresh_layout.finishRefresh()
            member_refresh_layout.resetNoMoreData()
        }
        if (page == 1 && goods.isNotEmpty()){
            if (adapterList.getAdapterByTag<MemberRecommendGoodsAdapter>("goods") == null){
                adapterList.add(MemberRecommendTitleAdapter(topbarHeight).then {
                    delegateAdapter.addAdapter(it)
                })
                adapterList.add(MemberRecommendGoodsAdapter(ArrayList()).then {
                    it.tag = "goods"
                    it.setOnItemClickListener { data, position ->
                        push("/goods/detail",{
                            it.withInt("goodsId",data.id)
                        })
                    }
                    delegateAdapter.addAdapter(it)
                })
                delegateAdapter.notifyDataSetChanged()
            }else{
                adapterList.getAdapterByTag<MemberRecommendGoodsAdapter>("goods")?.goodsList?.clear()
            }
        }
        if (page != 1 && goods.isEmpty()){
            /**该处为 暂未更多状态 等待修改 TODO 2018年03月13日13:31:13 */
            member_refresh_layout.finishLoadMoreWithNoMoreData()
        }else{
            member_refresh_layout.finishLoadMore()
            adapterList.getAdapterByTag<MemberRecommendGoodsAdapter>("goods")?.goodsList?.then {
                self ->
                self.addAll(goods)
            }
            adapterList.getAdapterByTag<MemberRecommendGoodsAdapter>("goods")?.notifyDataSetChanged()
        }
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:19
     * @Note   逻辑操作失败时回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:20
     * @Note   逻辑操作成功时回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:20
     * @Note   逻辑操作开始回调
     */
    override fun start() {
        getUtils().showDialog()
    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:21
     * @Note   网络状态实时监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberFragment
     * @Date   2018/2/24 下午6:21
     * @Note   fragment销毁时回调该方法
     */
    override fun destory() {

    }

}