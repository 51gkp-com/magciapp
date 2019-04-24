package com.enation.javashop.android.component.shop.activity

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopActLayBinding
import com.enation.javashop.android.component.shop.fragment.ShopAllFragment
import com.enation.javashop.android.component.shop.fragment.ShopHomeFragment
import com.enation.javashop.android.component.shop.fragment.ShopTagFragment
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.component.shop.weiget.ShopSearchView
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.MenuVo
import com.enation.javashop.android.lib.widget.MenuPopWindow
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.logic.contract.shop.ShopActivityContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopActivityPersenter
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.shop_act_lay.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView


/**
 * @author LDD
 * @Date   2018/4/13 上午9:03
 * @From   com.enation.javashop.android.component.shop.activity
 * @Note   店铺页面
 */
@Router(path = "/shop/detail")
class ShopActivity :BaseActivity<ShopActivityPersenter,ShopActLayBinding>(),ShopActivityContract.View{

    /**
     * @Name  shopId
     * @Type  Int
     * @Note  店铺id（自动注入）
     */
    @Autowired(name= "shopId",required = true)
    @JvmField var shopId: Int = 0

    /**
     * @Name  titles
     * @Type  ArrayList<String>
     * @Note  标题集合
     */
    private val titles = arrayListOf("店铺首页","全部商品","热销","上新","推荐")

    /**
     * @Name  searchView
     * @Type  ShopSearchView
     * @Note  店铺内搜索POPWindow
     */
    private val searchView by lazy {
        ShopSearchView.build(this,ScreenTool.getScreenHeight(this).toInt())
                .setShowObserver {
                    shop_holder.setBackgroundColor(getColorCompatible(R.color.javashop_color_holder_color))
                }.setDismissObserver {
                    shop_holder.setBackgroundColor(getColorCompatible(R.color.javashop_color_transparent))
                }.setSearchObserver {
                    text ->
                    push("/shop/search", {postcard ->
                        postcard.withInt("shopId", shopId)
                        postcard.withString("keyword", text)
                    })
                }
    }

    /**
     * @Name  menuView
     * @Type  MenuPopWindow
     * @Note  菜单PopWindow
     */
    private val menuView by lazy {
        MenuPopWindow.build(this, ArrayList<MenuVo>().then {
            self ->
            self.add(MenuVo("首页",R.drawable.javashop_icon_home_white,1))
            self.add(MenuVo("分享",R.drawable.javashop_icon_share_white,2))
            self.add(MenuVo("店铺详情",R.drawable.javashop_icon_shop_white,3))
        }).setItemCallBack { item ->
            when(item.menuId){
                1 ->{
                    push("/home/main")
                }
                2 ->{
                    presenter.share(this,
                            JavaShopConfigCenter.INSTANCE.WAP_SELLER_URL+"$shopId",
                            mViewBinding.data.logo,"玛吉克商城网商店铺",mViewBinding.data.name)
                }
                3 ->{
                    push("/shop/info", {postcard ->
                        postcard.withInt("shopId", shopId)
                    })
                }
            }
        }
    }

    /**
     * @Name  fragment
     * @Type  Fragment[]
     * @Note  fragment集合
     */
    private val fragment by lazy { arrayListOf<Fragment>(
            ShopHomeFragment().then {
                it.shopId = shopId
            },
            ShopAllFragment().then {
                it.shopId = shopId
            },
            ShopTagFragment().then {
                it.searchTag = "hot"
                it.shopId = shopId
            },
            ShopTagFragment().then {
                it.searchTag = "new"
                it.shopId = shopId
            },
            ShopTagFragment().then {
                it.searchTag = "recommend"
                it.shopId = shopId
            })
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:02
     * @Note   获取LayoutID
     */
    override fun getLayId(): Int {
        return R.layout.shop_act_lay
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:02
     * @Note   依赖注入
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:02
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)

        presenter.loadShopInfo(shopId)
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:03
     * @Note   绑定事件
     */
    override fun bindEvent() {

        /**设置关注事件*/
        shop_collect_lay.setOnClickListener(OnClickListenerAntiViolence({
            if(mViewBinding.data != null){
                presenter.collectShop(shopId,!mViewBinding.data.favorited)
            }
        }))

        /**设置Fragment*/
        shop_vp.setFragments(fragment,supportFragmentManager)

        /**显示搜索视图*/
        shop_find_lay.setOnClickListener {
            searchView.show(shop_holder)
        }

        /**跳转店铺分类*/
        shop_category.setOnClickListener {
            push("/shop/category", {postcard ->
                postcard.withInt("shopId", shopId)
            })
        }

        shop_hot_category.setOnClickListener{
            push("/shop/category", {postcard ->
                postcard.withInt("shopId", shopId)
            })
        }

        /**显示菜单视图*/
        shop_more.setOnClickListener {
            menuView.show(it)
        }

        /**跳转店铺详情*/
        shop_info.setOnClickListener {
            push("/shop/info", {postcard ->
                postcard.withInt("shopId", shopId)
            })
        }


    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:05
     * @Note   初始化指示器
     */
    private fun initIndicator(shopViewModel :ShopViewModel){
        /**初始化适配器*/
        val commonNavigator = CommonNavigator(this)
        /**设置自动填充*/
        commonNavigator.isAdjustMode = true
        /**初始化*/
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val commonPagerTitleView = CommonPagerTitleView(context)
                val customLayout = layoutInflater.inflate(R.layout.shop_top_bar_item, null)
                val titleView = customLayout.findViewById<TextView>(R.id.shop_tab_item_title_tv)
                val numView = customLayout.findViewById<TextView>(R.id.shop_tab_item_num_tv)
                val iconView = customLayout.findViewById<ImageView>(R.id.shop_tab_item_icon_iv)
                if (index == 0){
                    iconView.visibility = View.VISIBLE
                    numView.visibility = View.GONE
                }else{
                    iconView.visibility = View.GONE
                    numView.visibility = View.VISIBLE
                    numView.text = when(index){
                        1 -> "${shopViewModel.goodsNum}"
                        2 -> "${shopViewModel.hotNum}"
                        3 -> "${shopViewModel.new_Num}"
                        4 -> "${shopViewModel.recommendNum}"
                        else -> "0"
                    }
                }
                titleView.text = titles[index]
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {

                    override fun onSelected(index: Int, totalCount: Int) {
                        if (iconView.visibility == View.VISIBLE){
                            iconView.setImageResource(R.drawable.javashop_icon_shop_red)
                        }
                        titleView.setTextColor(context.getColorCompatible(R.color.javashop_color_price_red))
                        numView.setTextColor(context.getColorCompatible(R.color.javashop_color_price_red))
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        if (iconView.visibility == View.VISIBLE){
                            iconView.setImageResource(R.drawable.javashop_icon_shop_black)
                        }
                        numView.setTextColor(Color.BLACK)
                        titleView.setTextColor(Color.parseColor("#5c5c5c"))
                    }

                    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
                        customLayout.scaleX = 1f + (0.85f - 1f) * leavePercent
                        customLayout.scaleY = 1f + (0.85f - 1f) * leavePercent
                        val colorPercenta = ArgbEvaluatorHolder.eval(leavePercent, context.getColorCompatible(R.color.javashop_color_price_red), Color.parseColor("#5c5c5c"))
                        val colorPercentb = ArgbEvaluatorHolder.eval(leavePercent, context.getColorCompatible(R.color.javashop_color_price_red), Color.BLACK)
                        numView.setTextColor(colorPercentb)
                        titleView.setTextColor(colorPercenta)
                    }

                    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                        customLayout.scaleX = 0.85f + (1f - 0.85f) * enterPercent
                        customLayout.scaleY = 0.85f + (1f - 0.85f) * enterPercent
                        val colorPercenta = ArgbEvaluatorHolder.eval(enterPercent, Color.parseColor("#5c5c5c"), context.getColorCompatible(R.color.javashop_color_price_red))
                        val colorPercentb = ArgbEvaluatorHolder.eval(enterPercent, Color.BLACK , context.getColorCompatible(R.color.javashop_color_price_red))
                        numView.setTextColor(colorPercentb)
                        titleView.setTextColor(colorPercenta)
                    }
                }

                commonPagerTitleView.setOnClickListener {
                    shop_vp.currentItem = index
                }

                return commonPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                /** 构建标识视图 */
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(context.getColorCompatible(R.color.javashop_color_price_red))
                linePagerIndicator.lineHeight = ScreenTool.dip2px(context,2f).toFloat()
                return linePagerIndicator
            }
        }
        /**设置适配器*/
        shop_tab_bar.navigator = commonNavigator
        /**事件互相绑定*/
        ViewPagerHelper.bind(shop_tab_bar, shop_vp)
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:06
     * @Note   初始化店铺数据
     * @param  shop 店铺数据
     */
    override fun initShop(shop: ShopViewModel) {
        mViewBinding.data = shop
        if (!shop.favorited){
            shop_collect_top.isSelected = false
            shop_collect_top_iv.isSelected = false
            shop_collect_top_tv.text = "关注"
            shop_collect_top_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_white))
        }else{
            shop_collect_top.isSelected = true
            shop_collect_top_iv.isSelected = true
            shop_collect_top_tv.text = "已关注"
            shop_collect_top_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_price_red))
        }
        initIndicator(shopViewModel = shop)
        (shop_tab_bar.navigator as CommonNavigator).adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:06
     * @Note   错误回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:06
     * @Note   请求成功回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        if(message == "关注成功"){
            mViewBinding.data.favorited = true
            shop_collect_top.isSelected = true
            shop_collect_top_iv.isSelected = true
            shop_collect_top_tv.text = "已关注"
            shop_collect_top_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_price_red))
        }else if(message == "取消关注成功"){
            mViewBinding.data.favorited = false
            shop_collect_top.isSelected = false
            shop_collect_top_iv.isSelected = false
            shop_collect_top_tv.text = "关注"
            shop_collect_top_tv.setTextColor(activity.getColorCompatible(R.color.javashop_color_white))
        }
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:06
     * @Note   请求开始
     */
    override fun start() {
        //showDialog()
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:07
     * @Note   网络实时监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {
    }

    /**
     * @author LDD
     * @From   ShopActivity
     * @Date   2018/4/13 上午9:07
     * @Note   销毁回调
     */
    override fun destory() {
    }
}