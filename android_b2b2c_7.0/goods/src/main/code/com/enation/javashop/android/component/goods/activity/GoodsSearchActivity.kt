package com.enation.javashop.android.component.goods.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.adapter.SearchGoodsAdapter
import com.enation.javashop.android.component.goods.databinding.GoodsSearchActLayBinding
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.component.goods.weiget.GoodsFirstMoreView
import com.enation.javashop.android.component.goods.weiget.GoodsSearchMoreView
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsSearchContract
import com.enation.javashop.android.middleware.logic.presenter.goods.GoodsSearchPresenter
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.goods_search_act_lay.*
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.fragment.GoodsSearchFilterFragment
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.lib.adapter.VlayoutHolderAdapter
import com.enation.javashop.android.lib.adapter.VlayoutItemType
import com.enation.javashop.android.middleware.model.GoodsFilterValue
import com.scwang.smartrefresh.layout.footer.ClassicsFooter


/**
 * @author LDD
 * @Date   2018/3/12 下午3:46
 * @From   com.enation.javashop.android.component.goods.activity
 * @Note   商品模块 商品搜索页面
 */
@Router(path = "/goods/list")
class GoodsSearchActivity : BaseActivity<GoodsSearchPresenter, GoodsSearchActLayBinding>(), GoodsSearchContract.View {

    /**
     * @Name  filterMoreListBg
     * @Type  ArrayList<View>
     * @Note  筛选过滤按钮背景列表
     */
    private val filterMoreListBg by lazy {
        return@lazy ArrayList<View>().then { self ->
            self.addAll(arrayOf(goods_search_more_filter_a_bg,
                    goods_search_more_filter_b_bg,
                    goods_search_more_filter_c_bg,
                    goods_search_more_filter_d_bg))
        }
    }

    /**
     * @Name  filterMoreList
     * @Type  ArrayList<ConstraintLayout>
     * @Note  筛选过滤按钮列表
     */
    private val filterMoreList by lazy {
        return@lazy ArrayList<ConstraintLayout>().then { self ->
            self.addAll(arrayOf(goods_search_more_filter_a_lay,
                    goods_search_more_filter_b_lay,
                    goods_search_more_filter_c_lay,
                    goods_search_more_filter_d_lay))
        }
    }

    /**
     * @Name  reclyFlag
     * @Type  Boolean
     * @Note  列表样式标记
     */
    private var reclyFlag = true

    private var sort = "def_desc"

    private var prop = ""

    @Autowired(name= "keyword",required = false)
    @JvmField
    var keyWord : String? = null

    @Autowired(name= "category",required = false)
    @JvmField
    var category : Int = 0

    @Autowired(name= "brand",required = false)
    @JvmField
    var brand :Int = 0

    @Autowired(name= "hint",required = false)
    @JvmField
    var hint :String? = null

    private var page = 1

    /**
     * @Name  colligate
     * @Type  Int
     * @Note  综合排序是否选中
     */
    private var colligate = 0

    /**
     * @Name  volume
     * @Type  Int
     * @Note  综合排序是否选中
     */
    private var volume = 0

    /**
     * @Name  price
     * @Type  Int
     * @Note  价格排序是否选中
     */
    private var price = 0

    /**
     * @Name  scrollFlag
     * @Type  Boolean
     * @Note  滑动标记 当toplay.y 为0时下滑不再赋值
     */
    private var scrollFlag = true

    /**
     * @Name  filterDatas
     * @Type  ArrayList<GoodsFilterViewModel>
     * @Note  筛选数据
     */
    private var filterDatas = ArrayList<GoodsFilterViewModel>()

    /**
     * @Name  IMAGE
     * @Type  String
     * @Note  ViewTag
     */
    private val IMAGE = "image"

    /**
     * @Name  TEXT
     * @Type  String
     * @Note  ViewTag
     */
    private val TEXT = "text"

    /**
     * @Name  firstFilterData
     * @Type  ArrayList<GoodsFirstMoreView.GoodsFirstMoreData>()
     * @Note  第一个筛选按钮数据列表
     */
    private var firstFilterData = ArrayList<GoodsFirstMoreView.GoodsFirstMoreData>().then { self ->
        self.add(GoodsFirstMoreView.GoodsFirstMoreData("综合排序", 1, true))
        self.add(GoodsFirstMoreView.GoodsFirstMoreData("新品优先", 2, false))
        self.add(GoodsFirstMoreView.GoodsFirstMoreData("评论数从高到低", 3, false))
    }

    /**
     * @Name  searchFilterList
     * @Type  ArrayList<ConstraintLayout>
     * @Note  除筛选按钮之外的按钮集合
     */
    private val searchFilterList by lazy {
        return@lazy ArrayList<ConstraintLayout>().then { self ->
            self.addAll(arrayOf(
                    goods_search_colligate_lay,
                    goods_search_sales_volume_lay,
                    goods_search_price_lay))
        }
    }

    /**
     * @Name  actionMoreView
     * @Type  GoodsSearchMoreView
     * @Note  更多筛选视图
     */
    private lateinit var actionMoreView: GoodsSearchMoreView

    /**
     * @Name  firstMoreDialog
     * @Type  GoodsFirstMoreView
     * @Note  第一个筛选按钮 条件切换视图
     */
    private lateinit var firstMoreDialog: GoodsFirstMoreView

    /**
     * @Name  currentScrollY
     * @Type  Int
     * @Note  rv 当前滑动Y坐标
     */
    private var currentScrollY: Int = 0

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
     * @Type  GoodsMoreActionAdapter
     * @Note  筛选列表适配器
     */
    private var adapter = SearchGoodsAdapter(ArrayList())


    /**
     * @Name  goodsFilterFragment
     * @Type  GoodsSearchFilterFragment
     * @Note  筛选条件Frag
     */
    var goodsFilterFragment :GoodsSearchFilterFragment? = null

    /**
     * @Name  holderAdapter
     * @Type  VlayoutHolderAdapter
     * @Note  占位ViewAdapter
     */
    private val holderAdapter by lazy {
        VlayoutHolderAdapter(object : VlayoutHolderAdapter.HolderCallBack {
            override fun bindView(holder: RecyclerView.ViewHolder) {
                holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(ScreenTool.getScreenWidth(activity).toInt(), getHolderHeight())
                holder.itemView.setBackgroundResource(R.color.javashop_color_white)
            }

            override fun layoutHelperProvider(): LayoutHelper {
                return LinearLayoutHelper(0, 1)
            }

            override fun viewHolderProvider(parent: ViewGroup): RecyclerView.ViewHolder {
                return VlayoutHolderAdapter.PlaceHolder(View(parent.context))
            }
        })
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 上午11:46
     * @Note   提供layoutID
     */
    override fun getLayId(): Int {
        return R.layout.goods_search_act_lay
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 上午11:47
     * @Note   依赖注入
     */
    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 上午11:47
     * @Note   初始化操作
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        initRv()
        initSearchMoreDialog()
        initFirstMoreDialog()
        loadFilter()
        loadGoods()
        if (keyWord == null){
            javashop_goods_search_text_tv.text = hint
        }else{
            javashop_goods_search_text_tv.text = keyWord
        }
    }

    private fun loadFilter(){
        var map :HashMap<String,Any> = HashMap()

        if (keyWord != null){
            map.put("keyword",keyWord!!)
        }

        if (brand != 0){
            map.put("brand",brand)
        }

        if (category != 0){
            map.put("category",category)
        }

        presenter.loadFilter(map)
    }

    private fun loadGoods(){

        var map :HashMap<String,Any> = HashMap()

        if (keyWord != null){
            map.put("keyword",keyWord!!)
        }

        if (brand != 0){
            map.put("brand",brand)
        }

        if (category != 0){
            map.put("category",category)
        }

        if (prop.isNotEmpty()){
            map.put("prop",prop)
        }

        map.put("sort",sort)

        presenter.searchGoods(page,map)

    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 上午11:47
     * @Note   初始化RecycleView
     */
    private fun initRv() {

        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapter(holderAdapter)
        delegateAdapter.addAdapter(adapter)

        /**配置到RecycleView*/
        goods_search_rv.layoutManager = virtualLayoutManager
        goods_search_rv.adapter = delegateAdapter

        /**为不同的Item设置不同的缓存*/
        val viewPool = RecyclerView.RecycledViewPool()
        goods_search_rv.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 10)
        viewPool.setMaxRecycledViews(1, 10)
        viewPool.setMaxRecycledViews(VlayoutItemType, 1)
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:08
     * @Note   绑定事件
     */
    override fun bindEvent() {
        javashop_goods_search_text_lay.setOnClickListener {
            pop()
        }
        goods_search_refresh.setEnableRefresh(false)
        goods_search_refresh.setRefreshFooter(ClassicsFooter(this))
        goods_search_refresh.setFooterHeight((ScreenTool.getScreenWidth(this) * 0.2).pxToDp().toFloat())
        goods_search_refresh.setOnLoadMoreListener {
            page += 1
            loadGoods()
        }
        /**循环绑定更多筛选事件*/
        filterMoreListBg.forEach { item ->
            item.setOnClickListener {
                /**提高体验 延迟执行0.15秒*/
                AppTool.Time.delay(150) {
                    filterActionEvent(it)
                }
                /**显示更多筛选PopWindow*/
                showFilterMoreActionView(it, filterMoreList[filterMoreListBg.indexOf(item)], filterDatas[filterMoreListBg.indexOf(item)])
            }
        }

        /**循环绑定固定筛选事件*/
        searchFilterList.forEach { item ->
            item.setOnClickListener {
                /**固定筛选事件*/
                searchFilterEvent(it as ConstraintLayout)
            }
        }

        searchFilterEvent(searchFilterList[0])

        /**绑定切换列表样式事件*/
        goods_search_recly_type_touch.setOnClickListener {
            /**切换列表事件 带动画*/
            reclyTypeAnim(goods_search_recly_type_iv)
        }

        /**绑定返回事件*/
        goods_search_back_touch.setOnClickListener {
            toolBack()
        }

        /**打开筛选侧框*/
        goods_search_filter_lay.setOnClickListener(OnClickListenerAntiViolence({
            mViewBinding.root.to<DrawerLayout>().more {
                self ->
                self.openDrawer(Gravity.RIGHT)
            }
        }))

        /**设置滑动监听 添加动效*/
        goods_search_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                /**获取当前滑动Y轴距离*/
                currentScrollY = virtualLayoutManager.getScollYDistance()

                /**当前滑动y坐标小于等于最低滑动底线时 goods_search_top_lay Y轴跟随滑动 大于最低底线时不再跟随滑动 Y轴固定在最低底线处*/
                if (currentScrollY <= getScrollBaseLine()) {

                    /**判断滑动是否到顶！如果为-1f 证明是在用户手动下滑过程中显示 并非联动效果导致 只有非联动下 才可跟随RecycleView滑动 d=====(￣▽￣*)b*/
                    if (goods_search_top_lay.y != 1f) {
                        goods_search_top_lay.y = (-currentScrollY).toFloat()
                    }
                    /**当RecycleView滑动到顶部时 恢复滑动标记 开启视图纠正 防止滑动过快 视图错位 goods_search_top_layY轴归位*/
                    if (currentScrollY == 0) {
                        scrollFlag = true
                        goods_search_top_lay.y = 0f
                    }
                } else {
                    /**当滑动标记开启时 执行一次goods_search_top_lay视图y轴纠正 设置滑动标记 保证在RecycleView不滑动到顶部的情况下 只执行一次*/
                    if (scrollFlag) {
                        goods_search_top_lay.y = -getScrollBaseLine()
                        scrollFlag = false
                    } else {
                        /**当recycleView一次滑动 向上/向下 超过60px时 设置goods_search_top_lay显示或隐藏 显示设置1f 是为了区分 滑动显示 和滑动置顶显示*/
                        if (dy > 60) {
                            goods_search_top_lay.y = -getScrollBaseLine()
                        } else if (dy < -60) {
                            goods_search_top_lay.y = 1f
                        }
                    }
                }
            }
        })

        /**设置滑动监听*/
        mViewBinding.root.to<DrawerLayout>().addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View?) {
                /**筛选Frag关闭回调*/
                goodsFilterFragment?.colseCallBack()
                /**重新设置筛选按钮状态*/
                initFilterMoreLay()
                /**重新设置筛选固定按钮样式*/
                initFilterButtonBg()
            }

            override fun onDrawerOpened(drawerView: View?) {

            }
        })

        /**设置空事件 拦截上层事件*/
        right_layout.setOnClickListener {}

    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:15
     * @Note   获取Holder占位View高度
     */
    private fun getHolderHeight(): Int {
        /**
         * 当更多筛选视图显示时 站位图高度为整个top_lay的高度 也就是屏幕高度的26%
         * 当更多筛选视图隐藏时 占位图高度为整个top_lay高度的 75%
         */
        return if (goods_search_filter_more_action_lay.visibility == View.VISIBLE) {
            (ScreenTool.getStatusBarHeight(activity) + ScreenTool.getScreenWidth(activity) * 0.135 + ScreenTool.getScreenWidth(activity)/9*2).toInt()
        } else {
            (ScreenTool.getStatusBarHeight(activity) + ScreenTool.getScreenWidth(activity) * 0.135 + ScreenTool.getScreenWidth(activity)/9).toInt()
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:18
     * @Note   获取联动结束底线
     */
    private fun getScrollBaseLine(): Float {
        /**
         * 当更多筛选显示时 底线为 topbar的高度 加上固定筛选框高度 减去状态栏占位View高度
         * 当更多筛选隐藏时 底线为 topbar的高度 减去状态栏占位View高度
         */
        return if (goods_search_filter_more_action_lay.visibility == View.VISIBLE) {
            (goods_search_topbar.height + goods_search_action_bar.height - goods_search_toolbar_holder.height).toFloat()
        } else {
            (goods_search_topbar.height - goods_search_toolbar_holder.height).toFloat()
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:23
     * @Note   初始化更多筛选弹框视图
     */
    private fun initSearchMoreDialog() {
        /**获取goods_search_filter_more_action_lay的位置  通过屏幕高度 减去Y轴位置减去goods_search_filter_more_action_lay高度 获的弹出式图高度*/
        val location = IntArray(2)
        goods_search_filter_more_action_lay.getLocationOnScreen(location)
        actionMoreView = GoodsSearchMoreView(activity, (ScreenTool.getScreenHeight(activity) - (goods_search_filter_more_action_lay.height + location[1])).toInt())
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:23
     * @Note   初始化固定筛选切换弹框视图
     */
    private fun initFirstMoreDialog() {
        /**获取goods_search_action_bar的位置  通过屏幕高度 减去Y轴位置减去goods_search_action_bar高度 获的弹出式图高度*/
        val location = IntArray(2)
        goods_search_action_bar.getLocationOnScreen(location)
        firstMoreDialog = GoodsFirstMoreView(activity, (ScreenTool.getScreenHeight(activity) - (goods_search_action_bar.height + location[1])).toInt())
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:26
     * @Note   展示商品列表视图
     * @param  goodsList 商品数据
     */
    override fun showGoodsList(goodsList: ArrayList<GoodsItemViewModel>) {
        if (page == 1){
            adapter.dataList.clear()
            goods_search_refresh.resetNoMoreData()
        }

        if (page >= 1 && goodsList.size == 0){
            goods_search_refresh.finishLoadMoreWithNoMoreData()
        }else if (page >= 1 && goodsList.size > 0){
            goods_search_refresh.finishLoadMore()
        }

        adapter.dataList.addAll(goodsList)
        adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:26
     * @Note   初始化筛选相关
     * @param  filters 筛选数据
     */
    override fun initFilterPage(filters: ArrayList<GoodsFilterViewModel>) {
        filterDatas = filters
        initFilterMoreLay()
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:27
     * @Note   初始化筛选布局
     */
    @SuppressLint("SetTextI18n")
    private fun initFilterMoreLay() {

        /**当筛选Frag为空时执行*/
        goodsFilterFragment.haventDo {

            /**重新设置宽度 统一为屏幕宽度的0.85*/
            right_layout.reLayout<DrawerLayout.LayoutParams> {
                params ->
                params.width = (ScreenTool.getScreenWidth(activity)*0.85).toInt()
            }

            /**初始化筛选Frag*/
            goodsFilterFragment = GoodsSearchFilterFragment()
            goodsFilterFragment?.setFilterData(filterDatas)
            supportFragmentManager.beginTransaction().add(R.id.right_layout, goodsFilterFragment,"filter").commit()

            /**根据筛选数据决定 更多筛选视图是否显示*/
            if (filterDatas.size == 0) {
                goods_search_filter_more_action_lay.visibility = View.GONE
                return@haventDo
            }

            /**根据筛选数据 决定更多筛选视图显示几个  最多显示4个*/
            filterDatas.forEach { item ->
                if (filterDatas.indexOf(item) > 3){
                    return@forEach
                }
                val index = filterDatas.indexOf(item)
                filterMoreList[index].visibility = View.VISIBLE
                filterMoreListBg[index].visibility = View.VISIBLE
                filterMoreList[index].findViewWithTag<TextView>(TEXT).text = item.filterName
            }
        }

        /**当筛选视图不为空时执行 用来重新渲染更多筛选视图*/
        goodsFilterFragment.haveDo {

            /**循环渲染 最多渲染4个*/
            filterDatas.forEach {
                item ->
                /**获取position*/
                val index = filterDatas.indexOf(item)
                if (index > 3){
                    return@forEach
                }
                /**设置更多筛选Item布局内容*/
                initFilterMoreItemForState(item.selectedFilterValue,filterMoreList[index],item)

                /**设置更多筛选Item背景*/
                if (item.selectedFilterValue == null) {
                    filterMoreListBg[index].setBackgroundResource(R.drawable.javashop_goods_search_filter_btn_bg)
                } else {
                    filterMoreListBg[index].setBackgroundResource(R.drawable.javashop_goods_search_filter_btn_selected_bg)
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:29
     * @Note   切换列表布局 带动画效果
     * @param  imageView 动画视图
     */
    private fun reclyTypeAnim(imageView: ImageView) {
        val narrowAnimation = ScaleAnimation(1f, 0.1f, 1f, 0.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val enlargeanimation = ScaleAnimation(
                0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        /**0.6秒完成动画 */
        narrowAnimation.duration = 300
        enlargeanimation.duration = 300

        /**顺序执行动画，并且设置拦截器*/
        imageView.animSequentialStart(arrayListOf(narrowAnimation, enlargeanimation), interceptor = { index, state ->
            if (state == 2 && index == 0) {
                imageView.setImageResource(reclyFlag.judge(R.drawable.javashop_icon_grid, R.drawable.javashop_icon_list))
            }
            if (state == 2 && index ==1 ){
                Do.prepare().doOnUI {
                    onNext ->
                    reclyFlag = !reclyFlag
                    adapter.itemTransform()
                    virtualLayoutManager.setLayoutHelpers(arrayListOf(holderAdapter.onCreateLayoutHelper(), adapter.onCreateLayoutHelper()))
                    adapter.notifyDataSetChanged()
                    onNext()
                }.execute()
            }
            return@animSequentialStart false
        })
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:31
     * @Note   固定筛选事件
     * @param  view 点按的视图
     */
    private fun searchFilterEvent(view: ConstraintLayout) {
        /**循环设置状态*/
        searchFilterList.forEach { item ->

            /**根据tag获取View*/
            val text = item.findViewWithTag<TextView>(TEXT)
            val image = item.findViewWithTag<ImageView>(IMAGE)

            /**当tag == 2 代表 点击的Item是销量 直接设置销量标记为1 否则相反*/
            volume = (view.tag == "2").judge(1, 0)

            if (view.tag == "2") {
                if( volume == 1 ){
                    sort = "buynum_desc"
                    page = 1
                    loadGoods()
                }
            }

            /**查看tag是否相应 相等设置选中状态 不等设置未选中状态*/
            if (item.tag == view.tag) {
                text?.more {
                    it.setTextColor(activity.getColorCompatible(R.color.javashop_color_goods_search_action_item_selected))
                }
                /**1是综合 3是价格  根据条件设置选中图片*/
                image?.more {
                    when (item.tag) {
                        "1" -> {
                            if (colligate == 1) {
                                showFirstMoreView()
                            }
                            if (goods_search_colligate_tv.text.contains("综合")){
                                sort = "def_desc"
                            }else if (goods_search_colligate_tv.text.contains("评论")){
                                sort = "grade_desc"
                            }
                            it.setImageResource(R.drawable.javashop_icon_desc_red)
                            if (colligate != 1) {
                                page = 1
                                loadGoods()
                            }
                            colligate = 1
                        }
                        "3" -> {
                            when (price) {
                                0 -> {
                                    price = 1
                                    sort = "price_desc"
                                    it.setImageResource(R.drawable.javashop_icon_double_desc)
                                    page = 1
                                    loadGoods()
                                }
                                1 -> {
                                    price = 2
                                    sort = "price_asc"
                                    it.setImageResource(R.drawable.javashop_icon_double_asc)
                                    page = 1
                                    loadGoods()
                                }
                                else -> {
                                    price = 1
                                    sort = "price_desc"
                                    it.setImageResource(R.drawable.javashop_icon_double_desc)
                                    page = 1
                                    loadGoods()
                                }
                            }
                        }
                    }
                }
            } else {
                text?.more {
                    it.setTextColor(activity.getColorCompatible(R.color.javashop_color_black))
                }
                image?.more {
                    /**1是综合 3是价格  根据条件设置未选中图片*/
                    when (item.tag) {
                        "1" -> {
                            colligate = 0
                            it.setImageResource(R.drawable.javashop_icon_desc_gray)
                        }
                        "3" -> {
                            price = 0
                            it.setImageResource(R.drawable.javashop_icon_double_nomal)
                        }
                    }
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:45
     * @Note   更多筛选背景设置
     * @param  view 选中Item
     */
    private fun filterActionEvent(view: View) {
        /**根据底部对齐Id 判断是否是已打开状态*/
        ((view.layoutParams as ConstraintLayout.LayoutParams).bottomToBottom == goods_search_filter_more_action_lay.id).judge(trueDo = {
            /**当前是已打开状态 所以设置成未打开状态  重新设置底部对齐*/
            view.reLayout<ConstraintLayout.LayoutParams> { params ->
                params.bottomToBottom = goods_search_filter_more_line_b.id
            }
            /**根据item获取index 根据index获取查询数据 根据选中数据判断 状态  选中数据为空设置未选中背景 选中数据不为空设置选中背景*/
            if (filterDatas[filterMoreListBg.indexOf(view)].selectedFilterValue == null) {
                view.setBackgroundResource(R.drawable.javashop_goods_search_filter_btn_bg)
            } else {
                view.setBackgroundResource(R.drawable.javashop_goods_search_filter_btn_selected_bg)
            }
        }, falseDo = {
            /**重新设置底部对齐 设置打开背景*/
            view.reLayout<ConstraintLayout.LayoutParams> { params ->
                params.bottomToBottom = goods_search_filter_more_action_lay.id
            }
            view.setBackgroundResource(R.drawable.javashop_goods_search_filter_btn_open_bg)
        })

    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:50
     * @Note   打开固定筛选切换弹出视图
     */
    private fun showFirstMoreView() {
        firstMoreDialog.setConfirmObserable {
            it?.then {
                goods_search_colligate_tv.text = it.name.substring(0, 2)
                if (goods_search_colligate_tv.text.contains("综合")){
                    sort = "def_desc"
                }else if (goods_search_colligate_tv.text.contains("评论")){
                    sort = "grade_desc"
                }
                page = 1
                loadGoods()
            }
        }.setData(firstFilterData).show(goods_search_action_bar)
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:51
     * @Note   打开更多筛选 切换弹出视图
     * @param  item 选中的Item背景
     * @param  constraintLayout 选中Item
     * @param  filterData 筛选数据
     */
    private fun showFilterMoreActionView(item: View, constraintLayout: ConstraintLayout, filterData: GoodsFilterViewModel) {
        /**设置dimiss回调*/
        actionMoreView.setOnDismissObserable {
            filterActionEvent(item)
            initFilterButtonBg()
        }.setConfirmObserable { result ->
            initFilterMoreItemForState(result,constraintLayout,filterData)
            /**设置数据 显示*/
        }.setData(filterData).show(goods_search_filter_more_action_lay)
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/21 下午1:03
     * @Note   设置更多筛选Item背景
     * @param  result 选中
     * @param  constraintLayout 内容布局
     * @param  filterData       筛选数据
     */
    private fun initFilterMoreItemForState(result :GoodsFilterValue?, constraintLayout: ConstraintLayout, filterData: GoodsFilterViewModel){
        /**设置选中回调*/
        (result == null).judge(trueDo = {
            /**设置未选中布局*/
            constraintLayout.findViewWithTag<TextView>(TEXT).text = filterData.filterName
            constraintLayout.findViewWithTag<TextView>(TEXT).setTextColor(Color.BLACK)
            constraintLayout.findViewWithTag<TextView>(TEXT).reLayout<ConstraintLayout.LayoutParams> { params ->
                params.matchConstraintPercentWidth = 0.7f
                params.rightToRight = constraintLayout.findViewWithTag<ImageView>(IMAGE).id
            }
            constraintLayout.findViewWithTag<ImageView>(IMAGE).visibility = View.VISIBLE
        }, falseDo = {
            /**设置选中布局*/
            if (result?.name?.length!! > 5) {
                constraintLayout.findViewWithTag<TextView>(TEXT).text = result.name.substring(0, 5) + "..."
            } else {
                constraintLayout.findViewWithTag<TextView>(TEXT).text = result.name
            }
            constraintLayout.findViewWithTag<TextView>(TEXT).setTextColor(activity.getColorCompatible(R.color.javashop_color_goods_search_action_item_selected))
            constraintLayout.findViewWithTag<ImageView>(IMAGE).visibility = View.GONE
            constraintLayout.findViewWithTag<TextView>(TEXT).reLayout<ConstraintLayout.LayoutParams> { params ->
                params.rightToRight = constraintLayout.id
                params.matchConstraintPercentWidth = 0.97f
            }
        })
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/21 下午2:32
     * @Note   初始化固定筛选按钮背景
     */
    private fun initFilterButtonBg(){

        /**flag 标识筛选数据中是否存在已选中的数据  初始值为未选中*/
        var flag = false

        var brand = 0
        var category = this.category
        var prop = ""
        /**循环判断 是否有已选中的值 如果存在 设置Flag值*/
        filterDatas.forEach {
            item ->
            if (item.selectedFilterValue != null){
                flag = true

                if (item.type == "brand"){
                    brand = item.selectedFilterValue!!.value.toInt()
                }else if (item.type == "cat"){
                    category = item.selectedFilterValue!!.value.toInt()

                }else if (item.type == "prop"){
                    if (prop.contains("@")){
                        prop += "@${item.filterName}_${item.selectedFilterValue!!.name}"
                    }else{
                        prop += "${item.filterName}_${item.selectedFilterValue!!.name}"
                    }
                }
            }
        }
        if (this.brand != brand || this.category != category || this.brand != brand || this.prop != prop){
            this.brand = brand
            this.category = category
            this.prop = prop
            page = 1
            loadGoods()
        }



        /**根据flag 设置字体颜色及背景*/
        if (flag){
            goods_search_filter_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_goods_search_action_item_selected))
            goods_search_filter_iv.setImageResource(R.drawable.javashop_icon_filter_selected)
        }else{
            goods_search_filter_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_black))
            goods_search_filter_iv.setImageResource(R.drawable.javashop_icon_filter_nomal)
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:56
     * @Note   错误回调
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:56
     * @Note   完成回调
     */
    override fun complete(message: String, type: Int) {
        message.haveDo {
            showMessage(message)
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:57
     * @Note   开始回调
     */
    override fun start() {
    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:57
     * @Note   网络状态实时监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   GoodsSearchActivity
     * @Date   2018/3/20 下午12:58
     * @Note   销毁回调
     */
    override fun destory() {

    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag("value") != null){
            supportFragmentManager.popBackStack()
            return
        }
        if (mViewBinding.root.to<DrawerLayout>().isDrawerOpen(Gravity.RIGHT)){
            mViewBinding.root.to<DrawerLayout>().closeDrawer(Gravity.RIGHT)
            return
        }
        super.onBackPressed()
    }

}