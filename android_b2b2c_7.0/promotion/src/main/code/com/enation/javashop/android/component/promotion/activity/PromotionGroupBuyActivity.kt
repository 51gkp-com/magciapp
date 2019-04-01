package com.enation.javashop.android.component.promotion.activity

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.PromotionGroupbuyLayBinding
import com.enation.javashop.android.component.promotion.fragment.PromotionGroupBuyFragment
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionGroupBuyContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.PromotionGroupBuyPresenter
import com.enation.javashop.android.middleware.model.GroupPointViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.promotion_groupbuy_lay.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 * @author LDD
 * @Date   2018/5/22 下午3:01
 * @From   com.enation.javashop.android.component.promotion.activity
 * @Note   团购商城页面
 */
@Router(path = "/promotion/groupbuy/main")
class PromotionGroupBuyActivity : BaseActivity<PromotionGroupBuyPresenter, PromotionGroupbuyLayBinding>(), PromotionGroupBuyContract.View {

    /**
     * @Name  fragments
     * @Type  ArrayList<PromotionSecKillFragment>
     * @Note  页面集合
     */
    private val fragments = ArrayList<Fragment>()

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:10
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.promotion_groupbuy_lay
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:11
     * @Note   依赖注入
     */
    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:12
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.loadGroupBuy()
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:12
     * @Note   绑定事件
     */
    override fun bindEvent() {
        promotion_groupbuy_topbar.setLeftClickListener {
            pop()
        }.setTopHolderVisibility(false).setLineBgColor(Color.TRANSPARENT)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:12
     * @Note   渲染团购
     * @param  data 团购数据
     */
    override fun renderGroupBuy(data: ArrayList<GroupPointViewModel>) {
        data.forEach { item ->
            fragments.add(PromotionGroupBuyFragment().then { self ->
                self.groupId = item.id
            })
        }
        promotion_groupbuy_vp.setFragments(fragments, supportFragmentManager)
        initIndicator(data)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:27
     * @Note   初始化指示器
     * @param  data  数据
     */
    private fun initIndicator(data: ArrayList<GroupPointViewModel>) {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return data.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = Color.parseColor("#55ffffff")
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.text = data[index].title
                simplePagerTitleView.setOnClickListener {
                    promotion_groupbuy_vp.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(Color.WHITE)
                return linePagerIndicator
            }
        }
        promotion_groupbuy_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(promotion_groupbuy_indicator, promotion_groupbuy_vp)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:13
     * @Note   错误回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:13
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:14
     * @Note   开始回调
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:14
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyActivity
     * @Date   2018/5/22 下午3:14
     * @Note   销毁
     */
    override fun destory() {

    }

}