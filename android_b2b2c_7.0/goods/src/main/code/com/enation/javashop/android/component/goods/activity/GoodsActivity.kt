package com.enation.javashop.android.component.goods.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsActLayBinding
import com.enation.javashop.android.component.goods.fragment.GoodsCommentFragment
import com.enation.javashop.android.component.goods.fragment.GoodsInfoFragment
import com.enation.javashop.android.component.goods.fragment.GoodsIntroduceFragment
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.MenuVo
import com.enation.javashop.android.lib.widget.MenuPopWindow
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsActivityContract
import com.enation.javashop.android.middleware.logic.presenter.goods.GoodsActivityPresenter
import com.enation.javashop.android.middleware.model.GoodsViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import com.github.ielse.imagewatcher.ImageWatcher
import com.github.ielse.imagewatcher.ImageWatcherHelper
import kotlinx.android.synthetic.main.goods_act_lay.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author LDD
 * @Date   2018/3/27 上午9:31
 * @From   com.enation.javashop.android.component.goods.activity
 * @Note   商品详细页面
 */
@Router(path = "/goods/detail")
class GoodsActivity :BaseActivity<GoodsActivityPresenter,GoodsActLayBinding>(),GoodsActivityContract.View{

    val iwHelper : ImageWatcherHelper by lazy { ImageWatcherHelper.with(this,ImageWatchLoader()) }

    private lateinit var goods :GoodsViewModel

    /**
     * @Name  titles
     * @Type  ArrayList<String>
     * @Note  标题集合
     */
    private val titles = arrayListOf("商品","详情","评价")

    @Autowired(name= "goodsId",required = false)
    @JvmField var goodsId : Int = 0

    /**
     * @Name  fragments
     * @Type  ArrayList<Fragment>
     * @Note  页面集合
     */
    private lateinit var fragments :ArrayList<Fragment>

    /**
     * @Name  menuView
     * @Type  MenuPopWindow
     * @Note  菜单PopWindow
     */
    private val menuView by lazy {
        MenuPopWindow.build(this, ArrayList<MenuVo>().then {
            self ->
            self.add(MenuVo("首页",R.drawable.javashop_icon_home_white,1))
//            if (JavaShopConfigCenter.INSTANCE.DISTRIBUTION_MODE){
//                self.add(MenuVo("分销",R.drawable.javashop_icon_share_white,2))
//            }
            self.add(MenuVo("店铺",R.drawable.javashop_icon_shop_white,3))
        }).setItemCallBack { menuVo ->
            when (menuVo.menuId){
                1 ->{
                    push("/home/main")
                }
                2 ->{

                }
                3 ->{
                    push("/shop/detail",{
                        it.withInt("shopId",goods.shopId)
                    })
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午9:32
     * @Note   获取布局ID
     */
    override fun getLayId(): Int {
        return R.layout.goods_act_lay
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午9:32
     * @Note   注入依赖
     */
    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    fun collect(goodsId :Int,collect: Boolean){
        presenter.collectGoods(goodsId,collect)
    }

    fun collectState(state :Boolean){
            goods.collect = state
            goods_act_collect_goods_iv.setImageResource(state.judge(R.drawable.javashop_icon_heart_selected,R.drawable.javashop_icon_heart_nomal))
            goods_act_collect_goods_tv.text = state.judge("已关注","关注")
    }

    override fun collect(collect: Boolean) {
        collectState(collect)
        fragments[0].to<GoodsInfoFragment>().goodsInfoAdapter.goods.collect = collect
        fragments[0].to<GoodsInfoFragment>().goodsInfoAdapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午9:33
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.loadGoods(goodsId)
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:55
     * @Note   绑定事件
     */
    override fun bindEvent() {
        goods_act_collect_goods.setOnClickListener {
            presenter.collectGoods(goodsId,!goods.collect)
        }
        goods_act_to_cart.setOnClickListener {
            push("/cart/main")
        }
        goods_act_to_shop.setOnClickListener {
            push("/shop/detail",{
                it.withInt("shopId",goods.shopId)
            })
        }
        goods_act_add_cart.setOnClickListener {
            (fragments[0] as GoodsInfoFragment).specShow()
        }
        goods_act_share.setOnClickListener {
            presenter.shareGoods(this,JavaShopConfigCenter.INSTANCE.WAP_GOODS_URL+"${goodsId}",goods.goodsImage,goods.name,"JavaShop网上商城,选你所选爱你所爱!")
        }
        goods_act_more.setOnClickListener {
            menuView.show(it)
        }
        goods_act_buy_now.setOnClickListener {
            (fragments[0] as GoodsInfoFragment).buyNow()
        }
    }

    fun toComment(){
        goods_act_fragment_pager.setCurrentItem(2,false)
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:56
     * @Note   初始化指示器
     */
    private fun initIndicator(){
        fragments =  java.util.ArrayList<Fragment>().then {
            self ->
            self.add(GoodsInfoFragment().then {
                it.shopId = goods.shopId
                it.goodsId = goods.goodsId
            })
            self.add(GoodsIntroduceFragment().then {
                it.intro = goods.intro
                it.params = goods.params
            })
            self.add(GoodsCommentFragment().then {
                it.goodsId = goods.goodsId
            })
        }
        goods_act_fragment_pager.setFragments(fragments,supportFragmentManager)
        goods_act_fragment_pager.offscreenPageLimit = 2
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
                /**构建指示器 并重写部分方法 实现过渡动画*/
                val simplePagerTitleView = object : ColorTransitionPagerTitleView(context){
                    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
                        scaleX = 1f + (0.8f - 1f) * leavePercent
                        scaleY = 1f + (0.8f - 1f) * leavePercent
                        super.onLeave(index, totalCount, leavePercent, leftToRight)
                    }

                    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                        scaleX = 0.8f + (1f - 0.8f) * enterPercent
                        scaleY = 0.8f + (1f - 0.8f) * enterPercent
                        super.onEnter(index, totalCount, enterPercent, leftToRight)
                    }
                }
                /**设置未选中颜色*/
                simplePagerTitleView.normalColor = getColorCompatible(R.color.javashop_color_navy)
                /**设置选中颜色*/
                simplePagerTitleView.selectedColor = getColorCompatible(R.color.javashop_color_select_color_red)
                /**设置标题*/
                simplePagerTitleView.text = titles[index]
                /**设置文字大小*/
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ScreenTool.getScreenWidth(context)*0.05).toFloat())
                /**设置点击事件*/
                simplePagerTitleView.setOnClickListener { goods_act_fragment_pager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                /** 构建标识视图 */
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(getColorCompatible(R.color.javashop_color_select_color_red))
                linePagerIndicator.lineHeight = ScreenTool.dip2px(context,4f).toFloat()
                return linePagerIndicator
            }
        }
        /**设置适配器*/
        goods_act_indicator.navigator = commonNavigator
        /**事件互相绑定*/
        ViewPagerHelper.bind(goods_act_indicator, goods_act_fragment_pager)
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:56
     * @Note   加载Fragment
     * @param  goods 商品数据
     */
    override fun initFragment(goods: GoodsViewModel) {
        this.goods = goods
        initIndicator()
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:56
     * @Note   加载错误
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
    }

    fun showGallay(images :List<Uri>,position:Int){
        iwHelper.show(images,position)
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:57
     * @Note   加载成功
     */
    override fun complete(message: String, type: Int) {
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:57
     * @Note   家在开始
     */
    override fun start() {
    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:57
     * @Note   网络实时监听
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   GoodsActivity
     * @Date   2018/3/27 上午10:58
     * @Note   销毁
     */
    override fun destory() {
    }


    override fun onBackPressed() {
        if (!iwHelper.handleBackPressed()) {
            super.onBackPressed()
        }
    }

}
