package com.enation.javashop.android.component.shop.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.adapter.ShopAllGoodsAdapter
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopAllFragLayBinding
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.shop.ShopAllContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopAllPersenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.shop_all_frag_lay.*

/**
 * @author LDD
 * @Date   2018/4/10 上午8:10
 * @From   com.enation.javashop.android.component.shop.fragment
 * @Note   商店全部商品页面
 */
class ShopAllFragment : BaseFragment<ShopAllPersenter, ShopAllFragLayBinding>(), ShopAllContract.View, ShopActionAgreement {

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
    private var adapter = ShopAllGoodsAdapter(ArrayList())

    /**
     * @Name  priceFilter
     * @Type  Int
     * @Note  价格筛选
     */
    private var priceFilter = 0

    /**
     * @Name  filterFlag
     * @Type  String
     * @Note  筛选标识
     */
    private var filterFlag = "1"

    /**
     * @Name  keyword
     * @Type  String
     * @Note  关键字
     */
    var keyword: String? = null

    /**
     * @Name  category
     * @Type  String
     * @Note  分类
     */
    var category: String? = null

    /**
     * @Name shopId
     * @Type Int
     * @Note 店铺id
     */
    var shopId: Int = 0

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页
     */
    private var page = 1

    /**
     * @Name sort
     * @Type String
     * @Note 排序
     */
    var sort: String = "def_desc"

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
     * 加载flag
     */
    private var loadFlag = true

    /**
     * @Name  filterActionLayList
     * @Type  ArrayList<View>
     * @Note  筛选视图集合
     */
    private val filterActionLayList by lazy {
        arrayListOf(shop_colligate_lay, shop_new_lay, shop_recommend_lay, shop_volume_lay, shop_price_lay)
    }

    /**
     * @Name  reclyFlag
     * @Type  Boolean
     * @Note  列表样式标记
     */
    private var reclyFlag = true

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:11
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.shop_all_frag_lay
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:11
     * @Note   依赖注入
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:11
     * @Note   初始化
     */
    override fun init() {
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapter(adapter)

        /**配置到RecycleView*/
        shop_all_rv.layoutManager = virtualLayoutManager
        shop_all_rv.adapter = delegateAdapter

        /**为不同的Item设置不同的缓存*/
        val viewPool = RecyclerView.RecycledViewPool()
        shop_all_rv.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 10)
        viewPool.setMaxRecycledViews(1, 10)
        loadData()
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:12
     * @Note   绑定事件
     */
    override fun bindEvent() {
        /**绑定切换列表样式事件*/
        shop_rv_type_lay.setOnClickListener {
            /**切换列表事件 带动画*/
            reclyTypeAnim(shop_rv_type_iv)
        }

        shop_all_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val currentPostion = virtualLayoutManager.findLastVisibleItemPosition()
                if (currentPostion > adapter.data.size - 3 && loadFlag) {
                    page++
                    loadData()
                    loadFlag = false
                }
            }
        })

        filterActionLayList.forEach { lay ->
            initActionLayItem(lay)
        }
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/13 上午11:43
     * @Note   初始化绑定事件
     * @param  lay item视图
     */
    fun initActionLayItem(lay: View) {
        lay.setOnClickListener {
            filterActionLayList.forEach {
                if (it == lay) {
                    filterFlag = it.tag.to()
                    it.findViewWithTag<TextView>(TEXT)?.then {
                        it.setTextColor(context.getColorCompatible(R.color.javashop_color_selected_red))
                    }
                    if (it == shop_price_lay) {
                        it.findViewWithTag<ImageView>(IMAGE)?.then {
                            if (priceFilter == 0) {
                                priceFilter = 1
                                it.setImageResource(R.drawable.javashop_icon_double_desc)
                                sort = "price_asc"
                            } else if (priceFilter == 1) {
                                priceFilter = 2
                                it.setImageResource(R.drawable.javashop_icon_double_asc)
                                sort = "price_desc"
                            } else if (priceFilter == 2) {
                                priceFilter = 1
                                it.setImageResource(R.drawable.javashop_icon_double_desc)
                                sort = "price_asc"
                            }
                        }
                    } else if (it == shop_colligate_lay) {
                        sort = "def_desc"
                    } else if (it == shop_new_lay) {
                        sort = "grade_desc"
                    }else if(it == shop_recommend_lay){
                        sort = "def_desc"
                    }else if(it == shop_volume_lay){
                        sort = "buynum_desc"
                    }
                    reloadData()
                } else {
                    it.findViewWithTag<TextView>(TEXT)?.then {
                        it.setTextColor(Color.BLACK)
                    }
                    it.findViewWithTag<ImageView>(IMAGE)?.then {
                        priceFilter = 0
                        it.setImageResource(R.drawable.javashop_icon_double_nomal)
                    }
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:12
     * @Note   加载数据
     */
    private fun loadData() {
        var map = HashMap<String, Any>()
        map["seller_id"] = "${shopId}"
        map["sort"] = sort
        if (keyword != null && keyword != "") {
            map["keyword"] = keyword!!
        }
        if (category != null && category != "") {
            map["shop_cat_id"] = category!!
        }
        presenter.loadGoods(map, page)
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:13
     * @Note   刷新数据
     * @param  data 数据
     * @param  type 类型
     */
    fun refreshData(data: String, type: Int) {
        if (type == 1) {
            keyword = data
            page = 1
            loadData()
        } else {
            category = data
            page = 1
            loadData()
        }
    }

    /**
     *
     */
    fun reloadData(){
        adapter.data.clear()
        page = 1
        loadData()
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:13
     * @Note   加载数据
     * @param  data 数据
     */
    override fun initGoods(data: List<GoodsItemViewModel>) {
        if(page == 1){
            adapter.data.clear()
        }
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
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
            if (state == 2 && index == 1) {
                Do.prepare().doOnUI { onNext ->
                    reclyFlag = !reclyFlag
                    adapter.itemTransform()
                    virtualLayoutManager.setLayoutHelpers(arrayListOf(adapter.onCreateLayoutHelper()))
                    adapter.notifyDataSetChanged()
                    onNext()
                }.execute()
            }
            return@animSequentialStart false
        })
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:14
     * @Note   错误回调
     * @param  message 提示消息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:14
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        getUtils().dismissDialog()
        loadFlag = true
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:14
     * @Note   开始
     */
    override fun start() {
        getUtils().showDialog()
    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:15
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   ShopAllFragment
     * @Date   2018/4/24 下午3:15
     * @Note   销毁回调
     */
    override fun destory() {

    }

    override fun toGoods(goodsId: Int) {
        println("商品页：${goodsId}")
    }
}