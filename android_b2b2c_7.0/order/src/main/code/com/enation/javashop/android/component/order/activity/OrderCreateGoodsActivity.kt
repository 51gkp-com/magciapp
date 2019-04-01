package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.OrderCreateGiftCouponItemAdapter
import com.enation.javashop.android.component.order.adapter.OrderCreateGiftItemAdapter
import com.enation.javashop.android.component.order.adapter.OrderCreateGoodsItemAdapter
import com.enation.javashop.android.component.order.adapter.OrderCreatePromotionAdapter
import com.enation.javashop.android.component.order.databinding.OrderCreateGoodsLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.VlayoutHolderAdapter
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateGoodsContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderCreateGoodsPresenter
import com.enation.javashop.android.middleware.model.CartGoodsItemViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.OrderShopModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import com.google.gson.Gson
import kotlinx.android.synthetic.main.order_create_goods_lay.*

/**
 * @author LDD
 * @Date   2018/5/23 下午6:56
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单创建商品详细页面
 */
@Router(path = "/order/create/goods")
class OrderCreateGoodsActivity : BaseActivity<OrderCreateGoodsPresenter, OrderCreateGoodsLayBinding>(), OrderCreateGoodsContract.View  {


    @Autowired(name = "shop",required = true)
    @JvmField var shopData : OrderShopModel = OrderShopModel()


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
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:24
     * @Note   获取LayoutID
     */
    override fun getLayId(): Int {
        return R.layout.order_create_goods_lay
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:25
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
        JRouter.prepare().inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:25
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        if (shopData.promotionTitle.isNotEmpty()){
            delegateAdapter.addAdapter(OrderCreatePromotionAdapter(shopData.promotionTitle))
        }

        var promotionGoods = ArrayList<CartGoodsItemViewModel>()

        for (cartGood in shopData.cartGoods) {
            if (cartGood.groupPromotionId > 0){
                promotionGoods.add(cartGood)
            }
        }

        if (promotionGoods.isNotEmpty()){
            delegateAdapter.addAdapter(OrderCreateGoodsItemAdapter(promotionGoods))
        }

        if (shopData.gift.count() > 0){
            delegateAdapter.addAdapter(OrderCreateGiftItemAdapter(shopData.gift.get(0)))
        }
        if (shopData.coupon.count() > 0){
            delegateAdapter.addAdapter(OrderCreateGiftCouponItemAdapter(shopData.coupon.get(0)))
        }

        val holderView = VlayoutHolderAdapter(object : VlayoutHolderAdapter.HolderCallBack{
            override fun bindView(holder: RecyclerView.ViewHolder) {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.dip2px(holder.itemView.context,20f)))
            }

            override fun layoutHelperProvider(): LayoutHelper {
                return LinearLayoutHelper(0,1)
            }

            override fun viewHolderProvider(parent : ViewGroup): RecyclerView.ViewHolder {
                return VlayoutHolderAdapter.PlaceHolder(View(parent.context))
            }

        })

        if (delegateAdapter.adaptersCount > 0) {
            delegateAdapter.addAdapter(holderView)
        }


        var normalGoods = ArrayList<CartGoodsItemViewModel>()

        for (cartGood in shopData.cartGoods) {
            if (cartGood.groupPromotionId <= 0){
                normalGoods.add(cartGood)
            }
        }

        delegateAdapter.addAdapter(OrderCreateGoodsItemAdapter(normalGoods))

        /**配置到RecycleView*/
        order_create_goods_rv.layoutManager = virtualLayoutManager
        order_create_goods_rv.adapter = delegateAdapter
        order_create_goods_rv.setBackgroundColor(Color.WHITE)
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:25
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_create_goods_topbar.setLeftClickListener {
            pop()
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:10
     * @Note   错误回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:10
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:10
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:10
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsActivity
     * @Date   2018/5/23 下午7:11
     * @Note   销毁回调
     */
    override fun destory() {

    }
}