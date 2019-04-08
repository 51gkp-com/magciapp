package com.enation.javashop.android.component.promotion.activity

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.widget.TextView
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.PromotionSeckillLayBinding
import com.enation.javashop.android.component.promotion.fragment.PromotionSecKillFragment
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.getColorCompatible
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionSecKillContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.PromotionSecKillPresenter
import com.enation.javashop.android.middleware.model.SecKillListViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.promotion_seckill_lay.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

/**
 * @author LDD
 * @Date   2018/5/21 下午5:09
 * @From   com.enation.javashop.android.component.promotion.activity
 * @Note   秒杀页面
 */
@Router(path = "/promotion/seckill/main")
class PromotionSecKillActivity : BaseActivity<PromotionSecKillPresenter, PromotionSeckillLayBinding>(), PromotionSecKillContract.View {

    /**
     * @Name  fragments
     * @Type  ArrayList<PromotionSecKillFragment>
     * @Note  页面集合
     */
    private val fragments = ArrayList<Fragment>()

    /**
     * @Name  complete
     * @Type  ()->Unit
     * @Note  刷新回调
     */
    private val complete :()->Unit = {
        presenter.loadSecKill()
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:11
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.promotion_seckill_lay
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:12
     * @Note   依赖注入
     */
    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }



    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:12
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.loadSecKill()
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:12
     * @Note   绑定事件
     */
    override fun bindEvent() {
        promotion_seckill_topbar.setLeftClickListener {
            pop()
        }.setTopHolderVisibility(false).setLineBgColor(Color.TRANSPARENT)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:13
     * @Note   渲染秒杀数据
     * @param  data  数据
     */
    override fun renderSecKill(data: ArrayList<SecKillListViewModel>) {
        fragments.clear()
        data.forEach { item ->
            val index = data.indexOf(item)
            fragments.add(PromotionSecKillFragment().then { self ->
                self.current = item
                self.next = data.getOrNull(index + 1)
                self.complete = complete
            })
        }
        promotion_seckill_vp.setFragments(fragments, supportFragmentManager)
        initIndicator(data)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:37
     * @Note   初始化指示器
     * @param  data 秒杀数据
     */
    private fun initIndicator(data: ArrayList<SecKillListViewModel>) {
        /**初始化适配器*/
        val commonNavigator = CommonNavigator(this)
        /**设置自动填充*/
        commonNavigator.isAdjustMode = true
        /**初始化*/
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val commonPagerTitleView = CommonPagerTitleView(activity)
                val customLayout = layoutInflater.inflate(R.layout.prmotion_seckill_bar_item, null)
                val titleView = customLayout.findViewById<TextView>(R.id.promotion_seckill_bar_item_time)
                val numView = customLayout.findViewById<TextView>(R.id.promotion_seckill_bar_item_title)
                titleView.text = data[index].text + ":00"
                numView.text = if (index == 0) {
                    "抢购中"
                } else {
                    "即将开始"
                }
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {

                    override fun onSelected(index: Int, totalCount: Int) {
                        titleView.setTextColor(context.getColorCompatible(R.color.javashop_color_white))
                        numView.setTextColor(context.getColorCompatible(R.color.javashop_color_white))
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        numView.setTextColor(Color.parseColor("#55ffffff"))
                        titleView.setTextColor(Color.parseColor("#55ffffff"))
                    }

                    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
                        titleView.scaleX = 1f + (0.8f - 1f) * leavePercent
                        titleView.scaleY = 1f + (0.8f - 1f) * leavePercent
                        val colorPercenta = ArgbEvaluatorHolder.eval(leavePercent, context.getColorCompatible(R.color.javashop_color_white), Color.parseColor("#55ffffff"))
                        val colorPercentb = ArgbEvaluatorHolder.eval(leavePercent, context.getColorCompatible(R.color.javashop_color_white), Color.parseColor("#55ffffff"))
                        numView.setTextColor(colorPercentb)
                        titleView.setTextColor(colorPercenta)
                    }

                    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                        titleView.scaleX = 0.8f + (1f - 0.8f) * enterPercent
                        titleView.scaleY = 0.8f + (1f - 0.8f) * enterPercent
                        val colorPercenta = ArgbEvaluatorHolder.eval(enterPercent, Color.parseColor("#55ffffff"), context.getColorCompatible(R.color.javashop_color_white))
                        val colorPercentb = ArgbEvaluatorHolder.eval(enterPercent, Color.parseColor("#55ffffff"), context.getColorCompatible(R.color.javashop_color_white))
                        numView.setTextColor(colorPercentb)
                        titleView.setTextColor(colorPercenta)
                    }
                }

                commonPagerTitleView.setOnClickListener {
                    promotion_seckill_vp.currentItem = index
                }

                return commonPagerTitleView
            }

            override fun getCount(): Int {
                return data.size
            }

            override fun getIndicator(p0: Context?): IPagerIndicator? {
                return null
            }

        }
        /**设置适配器*/
        promotion_seckill_indicator.navigator = commonNavigator
        /**事件互相绑定*/
        promotion_seckill_vp.clearOnPageChangeListeners()
        ViewPagerHelper.bind(promotion_seckill_indicator, promotion_seckill_vp)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:13
     * @Note   错误回调
     * @param  message 错误信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:13
     * @Note   完成
     * @param  message 完成回调
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:14
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:14
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillActivity
     * @Date   2018/5/21 下午5:14
     * @Note   销毁
     */
    override fun destory() {

    }
}