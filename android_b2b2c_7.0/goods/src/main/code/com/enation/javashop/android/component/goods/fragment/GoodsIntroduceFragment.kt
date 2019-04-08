package com.enation.javashop.android.component.goods.fragment

import android.annotation.SuppressLint
import android.webkit.WebSettings
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsIntroduceLayBinding
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.getColorCompatible
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsIntroduceContract
import com.enation.javashop.android.middleware.logic.presenter.goods.GoodsIntroducePresenter
import com.enation.javashop.android.middleware.model.GoodsParamParent
import com.enation.javashop.android.middleware.utils.HtmlUtils
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.goods_introduce_lay.*


/**
 * @author LDD
 * @Date   2018/3/30 上午10:17
 * @From   com.enation.javashop.android.component.goods.fragment
 * @Note   商品介绍Fragment
 */
class GoodsIntroduceFragment :BaseFragment<GoodsIntroducePresenter,GoodsIntroduceLayBinding>(),GoodsIntroduceContract.View {

    var intro :String = ""

    var params :ArrayList<GoodsParamParent> = ArrayList()

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:22
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.goods_introduce_lay
    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:22
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:23
     * @Note   初始化操作
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        val localWebSettings = goods_introduce_web.getSettings()
        localWebSettings.useWideViewPort = true
        localWebSettings.loadWithOverviewMode = true
        localWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        localWebSettings.javaScriptEnabled = true
        localWebSettings.setSupportZoom(true)
        localWebSettings.builtInZoomControls = true
        goods_introduce_web.isVerticalScrollBarEnabled = false
        goods_introduce_web.isHorizontalScrollBarEnabled = false
        errorLog("IMAGE",HtmlUtils.get.fitImageSize(intro))
        goods_introduce_web.loadData(HtmlUtils.get.fitImageSize(intro), "text/html; charset=UTF-8", null);
    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:23
     * @Note   绑定事件
     */
    override fun bindEvent() {
        goods_introduce_intro.setOnClickListener {
            goods_introduce_intro.setTextColor(context.getColorCompatible(R.color.javashop_color_select_color_red))
            goods_introduce_spec.setTextColor(context.getColorCompatible(R.color.javashop_color_nomal_color_gray))
            goods_introduce_web.loadData(HtmlUtils.get.fitImageSize(intro), "text/html; charset=UTF-8", null);
        }
        goods_introduce_spec.setOnClickListener {
            goods_introduce_spec.setTextColor(context.getColorCompatible(R.color.javashop_color_select_color_red))
            goods_introduce_intro.setTextColor(context.getColorCompatible(R.color.javashop_color_nomal_color_gray))
            goods_introduce_web.loadData(HtmlUtils.get.buildParamsTable(params), "text/html; charset=UTF-8", null);
        }
    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:24
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:24
     * @Note   完成回调
     * @param  完成信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:25
     * @Note   开始回调
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:25
     * @Note   网络实时监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   GoodsIntroduceFragment
     * @Date   2018/3/30 上午10:25
     * @Note   销毁回调
     */
    override fun destory() {

    }

}