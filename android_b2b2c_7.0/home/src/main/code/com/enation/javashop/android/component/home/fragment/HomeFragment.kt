package com.enation.javashop.android.component.home.fragment

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.activity.HomeActivity
import com.enation.javashop.android.component.home.adapter.*
import com.enation.javashop.android.component.home.agreement.FloorActionAgreement
import com.enation.javashop.android.component.home.binding.HomeFragmentBindHelper
import com.enation.javashop.android.component.home.databinding.HomeFragLayBinding
import com.enation.javashop.android.component.home.launch.HomeLaunch
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.VlayoutItemType
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.middleware.logic.contract.home.HomeFragmentContract
import com.enation.javashop.android.middleware.logic.presenter.home.HomeFragmentPresenter
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.home_frag_lay.*
import java.util.logging.Handler

/**
 * @author LDD
 * @Date   2018/1/19 下午5:27
 * @From   com.enation.javashop.android.component.home.fragment
 * @Note   主页Fragment
 */
class HomeFragment : BaseFragment<HomeFragmentPresenter,HomeFragLayBinding>(),HomeFragmentContract.View ,FloorActionAgreement{

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
     * @author LDD
     * @Date   2018/1/19 下午5:29
     * @From   com.enation.javashop.android.component.home.fragment.HomeFragment
     * @Note   databindingViewModel
     */
    private var bindHelper = HomeFragmentBindHelper()

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:32
     * @Note   提供layoutID
     * @return layoutId
     */
    override fun getLayId(): Int {
        return R.layout.home_frag_lay
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:33
     * @Note   初始化依赖注入
     */
    override fun bindDagger() {
        HomeLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:33
     * @Note   初始化操作
     */
    override fun init() {
        mViewDataBinding.homeData = bindHelper
        mViewDataBinding.homeFragToolbarSearchLay.setOnClickListener(OnClickListenerAntiViolence({
            push("/search/main")
        }))
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mViewDataBinding.homeFragFloorView.layoutManager = virtualLayoutManager
        mViewDataBinding.homeFragFloorView.adapter = delegateAdapter
        /**为不同的Item设置不同的缓存*/
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(1, 10)
        viewPool.setMaxRecycledViews(2, 10)
        viewPool.setMaxRecycledViews(3, 10)
        viewPool.setMaxRecycledViews(4, 10)
        viewPool.setMaxRecycledViews(5, 10)
        viewPool.setMaxRecycledViews(6, 10)
        viewPool.setMaxRecycledViews(7, 10)
        viewPool.setMaxRecycledViews(8, 10)
        viewPool.setMaxRecycledViews(9, 10)
        viewPool.setMaxRecycledViews(10, 10)
        viewPool.setMaxRecycledViews(11, 10)
        viewPool.setMaxRecycledViews(12, 10)
        viewPool.setMaxRecycledViews(13, 10)
        mViewDataBinding.homeFragFloorView.recycledViewPool = viewPool
        presenter.loadFloor()
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:33
     * @Note   绑定事件
     */
    override fun bindEvent() {
        home_frag_toolbar_my_lay.setOnClickListener {
            activity.to<HomeActivity>().toMember()
        }
        mViewDataBinding.homeFragFloorView.setOnScrollObserver { _, _, _, _ ->
            val scrollY = virtualLayoutManager.getScollYDistance()
            if (bindHelper.isHide.get() != scrollY <= topbarHeight){
                bindHelper.isHide.set(scrollY <= topbarHeight)
            }
            bindHelper.scrollY.set(if(scrollY < 0) 0 else scrollY)
            bindHelper.alpha.set(if (scrollY > 55) 200 else 255 - scrollY)
        }
        configRefresh()
        home_frag_toolbar_scan_lay.setOnClickListener {
            activity.to<HomeActivity>().toCategory()
        }
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:34
     * @Note   页面销毁回调
     */
    override fun destory() {
        debugLog("HomeFragment","Destory")
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/8/16 下午4:08
     * @Note   渲染楼层
     * @param  data  数据
     */
    override fun renderFloor(data: ArrayList<Any>) {
        home_frag_tangram_refreshlayout.finishRefresh()
        if (adapterList.count() > 0){
            adapterList.filter {
                return@filter it is FloorSecKillAdapter
            }.getOrNull(0)?.to<FloorSecKillAdapter>()?.timer?.destory()
        }
        adapterList.clear()
        adapterList.add(FloorBannerAdapter(this.weak(),data[0] as ArrayList<BannerModel>,this))
        adapterList.add(FloorMenuAdapter(data[1] as ArrayList<FloorMenuModel>,this))
        if (data[2].to<SecKillListViewModel>().text != ""){
            adapterList.add(FloorSecKillAdapter(weak(),{
                presenter.loadFloor()
            }, data[4] as ArrayList<GoodsItemViewModel>, data[2] as SecKillListViewModel,if(data[3] is String){null}else{data[3] as SecKillListViewModel}))
        }
        for (i in 5..(data.count() - 1)){
            val item = data[i] as FloorViewModel
            if (item.typeId == 23){
                adapterList.add(FloorSingleImageAdapter(item,this))
            }else if (item.typeId == 24){
                adapterList.add(FloorLeftOneRightTwoAdapter(item,this))
            }else if (item.typeId == 25){
                adapterList.add(FloorRightOneLeftTwoAdapter(item,this))
            }else if (item.typeId == 26){
                adapterList.add(FloorThreeImageAdapter(item,this))
            }else if (item.typeId == 27){
                adapterList.add(FloorFiveImageAdapter(item,this))
            }else if (item.typeId == 28){
                adapterList.add(FloorCornerBannerAdapter(this.weak(),ArrayList(item.mapBanner()),this))
            }else if (item.typeId == 29){
                adapterList.add(FloorFourImageAdapter(item,this))
            }else if (item.typeId == 30){
                adapterList.add(FloorTitleAdapter(item,this))
            }else if (item.typeId == 31){
                adapterList.add(FloorFourImageAdapter(item,this))
            }else if (item.typeId == 32){
                adapterList.add(FloorLeftRightAdapter(item,this))
            }else if (item.typeId == 37){
                val adapter = ReflexHelper.build("com.enation.javashop.android.component.member.adapter.MemberRecommendGoodsAdapter").newInstance(java.util.ArrayList<RecommendGoodsViewModel>().then {
                    self ->
                    self.add(item.itemList[0].getGoodsValue())
                    if (item.itemList.count() > 1) {
                        self.add(item.itemList[1].getGoodsValue())
                    }
                }).getInstance<BaseDelegateAdapter<*,RecommendGoodsViewModel>>()
                adapter.setOnItemClickListener { data, position ->
                    goods(data.id)
                }
                adapterList.add(adapter)
            }else if (item.typeId == 42){
                adapterList.add(FloorTextAdapter(item,this))
            }
        }
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapterList)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun pointMall() {
        push("/promotion/pointshop/main")
    }

    override fun secKill() {
        push("/promotion/seckill/main")
    }

    override fun groupMall() {
        push("/promotion/groupbuy/main")
    }

    override fun couponHall() {
        push("/promotion/coupon/hall")
    }

    override fun toShop(id: Int) {
        push("/shop/detail",{postcard ->
            postcard.withInt("shopId", id)
        })
    }

    override fun toWeb(url: String) {
        push("/common/web",{
            //标题
            it.withString("title","外部网页")
            //URL
            it.withString("url",url)
        })
    }

    override fun searchGoodsForKeyWord(keyword: String) {
        push("/goods/list",{
            it.withString("keyword",keyword)
            it.withString("hint",keyword)
        })
    }

    override fun searchGoodsForCatrgory(catId: Int, text: String) {
        push("/goods/list",{
            it.withInt("category",catId)
            it.withString("hint", text)
        })
    }

    override fun goods(goodsId: Int) {
        push("/goods/detail",{
            it.withInt("goodsId",goodsId)
        })
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:35
     * @Note   处理错误信息
     * @param  message  错误信息
     */
    override fun onError(message: String, type: Int) {
        getUtils().dismissDialog()
        errorLog("error",message)
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:36
     * @Note   操作完成
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:37
     * @Note   耗时加载开始
     */
    override fun start() {
        getUtils().showDialog()
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:37
     * @Note   配置刷新监听处理
     */
    private fun configRefresh(){
        mViewDataBinding.homeFragTangramRefreshlayout.setHeaderHeight((ScreenTool.getScreenWidth(activity) * 0.5).pxToDp().toFloat())
        mViewDataBinding.homeFragTangramRefreshlayout.setRefreshHeader(ClassicsHeader(activity))
        mViewDataBinding.homeFragTangramRefreshlayout.setOnMultiPurposeListener(SmartHideViewHelper({
            home_frag_toolbar.visibility = View.GONE
            home_frag_floor_view.setOnTouchListener { _, _ ->
                true
            }
        }, {
            home_frag_toolbar.visibility = View.VISIBLE
            home_frag_floor_view.setOnTouchListener { _, _ ->
                false
            }
        }))
        mViewDataBinding.homeFragTangramRefreshlayout.setOnRefreshListener {
            presenter.loadFloor()
        }
    }

    /**
     * @author LDD
     * @From   HomeFragment
     * @Date   2018/1/19 下午5:39
     * @Note   处理网络状态
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {
        state.filter(onWifi = {

        },onMobile = {

        },offline = {

        })
    }
}