package com.enation.javashop.android.component.member.activity

import android.databinding.ObservableField
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.CollectGoodsAdapter
import com.enation.javashop.android.component.member.adapter.CollectShopAdapter
import com.enation.javashop.android.component.member.databinding.MemberCollectLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.component.member.vm.MemberCollectViewModel
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.VlayoutItemType
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.middleware.logic.contract.member.MemberCollectContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberCollectPresenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.member_collect_lay.*

/**
 * @author LDD
 * @Date   2018/5/4 下午4:10
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员收藏页面
 */
@Router(path = "/member/collect/main")
class MemberCollectActivity :BaseActivity<MemberCollectPresenter,MemberCollectLayBinding>(),MemberCollectContract.View{

    @Autowired(name = "type",required = true)
    @JvmField var type :Int = 0

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
    private val bindingData = MemberCollectViewModel(ObservableField(0))

    /**
     * @Name  goodsAdapter
     * @Type  CollectGoodsAdapter
     * @Note  商品适配器
     */
    private val goodsAdapter = CollectGoodsAdapter(ArrayList())

    /**
     * @Name  shopAdapter
     * @Type  CollectShopAdapter
     * @Note  商店适配器
     */
    private val shopAdapter = CollectShopAdapter(ArrayList())

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页
     */
    private var page = 1


    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:11
     * @Note   依赖注入
     */
    override fun getLayId(): Int {
        return R.layout.member_collect_lay
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:13
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:14
     * @Note   依赖注入
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.data = bindingData
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        collect_rv.layoutManager = virtualLayoutManager
        collect_rv.adapter = delegateAdapter
        collect_refresh.setRefreshHeader(ClassicsHeader(this))
        collect_refresh.setRefreshFooter(ClassicsFooter(this))
        /**为不同的Item设置不同的缓存*/
        val viewPool = RecyclerView.RecycledViewPool()
        collect_rv.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 10)
        viewPool.setMaxRecycledViews(1, 10)
        viewPool.setMaxRecycledViews(VlayoutItemType, 1)
        bindingData.state.set(type)
        loadData()
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:33
     * @Note   加载数据
     */
    private fun loadData(){
        presenter.loadData(page,bindingData.state.get())
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:14
     * @Note   绑定事件
     */
    override fun bindEvent() {
        collect_goods_tv.setOnClickListener {
            page = 1
            bindingData.state.set(0)
            loadData()
        }
        collect_shop_tv.setOnClickListener {
            page = 1
            bindingData.state.set(1)
            loadData()
        }
        collect_refresh.setOnRefreshListener {
            page = 1
            loadData()
        }
        collect_refresh.setOnLoadMoreListener {
            page++
            loadData()
        }
        collect_back_iv.setOnClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:15
     * @Note   渲染商品UI
     * @param  data 商品数据
     */
    override fun renderGoodsUI(data: ArrayList<GoodsItemViewModel>) {
        if (page!=1){
            if (data.size == 0){
                collect_refresh.finishLoadMoreWithNoMoreData()
            }else{
                collect_refresh.finishLoadMore()
            }
            goodsAdapter.data.addAll(data)
            goodsAdapter.notifyDataSetChanged()
        }else{
            collect_refresh.resetNoMoreData()
            collect_refresh.finishLoadMore()
            collect_refresh.finishRefresh()
            delegateAdapter.clear()
            delegateAdapter.notifyDataSetChanged()
            goodsAdapter.data.clear()
            goodsAdapter.data.addAll(data)
            delegateAdapter.addAdapter(goodsAdapter)
            delegateAdapter.notifyDataSetChanged()
        }
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:15
     * @Note   渲染店铺数据
     * @param  data 店铺数据
     */
    override fun renderShopUI(data: ArrayList<ShopViewModel>) {
        if (page!=1){
            if (data.size == 0){
                collect_refresh.finishLoadMoreWithNoMoreData()
            }else{
                collect_refresh.finishLoadMore()
            }
            shopAdapter.data.addAll(data)
            shopAdapter.notifyDataSetChanged()
        }else{
            collect_refresh.resetNoMoreData()
            collect_refresh.finishLoadMore()
            collect_refresh.finishRefresh()
            delegateAdapter.clear()
            delegateAdapter.notifyDataSetChanged()
            shopAdapter.data.clear()
            shopAdapter.data.addAll(data)
            delegateAdapter.addAdapter(shopAdapter)
            delegateAdapter.notifyDataSetChanged()
        }
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:15
     * @Note   错误回调
     * @param  message 错误数据
     */
    override fun onError(message: String, type: Int) {
        errorLog("asdadas",message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:16
     * @Note   完成回调
     * @param  message 数据
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:16
     * @Note   开始回调
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:17
     * @Note   网络实时监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberCollectActivity
     * @Date   2018/5/4 下午4:17
     * @Note   销毁
     */
    override fun destory() {

    }
}