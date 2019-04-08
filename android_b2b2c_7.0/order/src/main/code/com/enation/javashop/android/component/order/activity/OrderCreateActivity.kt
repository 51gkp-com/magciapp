package com.enation.javashop.android.component.order.activity

import android.content.Intent
import android.databinding.ObservableField
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.*
import com.enation.javashop.android.component.order.databinding.OrderCreateLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.CartDataChange
import com.enation.javashop.android.middleware.event.OrderCreateDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderCreatePresenter
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.order_create_lay.*

/**
 * @author LDD
 * @Date   2018/5/23 上午10:03
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单创建页面
 */
@Router(path = "/order/create")
class OrderCreateActivity : BaseActivity<OrderCreatePresenter,OrderCreateLayBinding>(),OrderCreateContract.View {

    private var isNeedPay  = true

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private lateinit var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  adapters
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapters = ArrayList<DelegateAdapter.Adapter<*>>()

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:03
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_create_lay
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:03
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:04
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        /**配置到RecycleView*/
        order_create_rv.layoutManager = virtualLayoutManager
        order_create_rv.adapter = delegateAdapter

        presenter.loadData()
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:05
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_create_topbar.setLeftClickListener {
            pop()
        }
        order_create_confrim.setOnClickListener {
            presenter.createOrder()
        }
        getEventCenter().register(OrderCreateDataChange::class.java,{
            presenter.loadData()
        }).joinManager(disposableManager)
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/27 下午1:23
     * @Note   返回数值处理
     * @param  resultCode  返回码
     * @param  data  数据
     */
    override fun resultHandle(resultCode: Int, data: Intent?) {
        if (resultCode != 0){
            presenter.loadData()
        }
    }

    override fun renderUi(data: OrderCreateModel) {
        val price = data.price.payPrice.get().replace("￥","").toDouble().toInt()
        isNeedPay = price != 0 && data.params.paymentType == GlobalState.PAY_ONLINE
        adapters.clear()
        adapters.add(OrderCreateAddressAdapter(data.address!!))
        adapters.add(OrderCreateHintAdapter(SingleStringViewModel(data.address!!.addressInfoObser)))
        adapters.add(OrderCreateShopAdapter(data.goods))
        adapters.add(OrderCreatePayShipAdapter(PayShipTimeViewModel(ObservableField(GlobalState.asText(data.params.paymentType)), ObservableField(GlobalState.asText(data.params.receiveTime)))))
        adapters.add(OrderCreateReceiptAdapter(data.params.receipt))
        adapters.add(OrderCreateCouponAdapter(data.shopId,data.coupons))
        adapters.add(OrderCreatePriceAdapter(data.price))
        order_create_price_tv.text = SpannableStringBuilder(data.price.payPrice.get()).then { self ->
            self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(activity) / 7 * 0.6 * 0.6).toInt()) , 0 , 1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(activity) / 7 * 0.6 * 0.6).toInt()) ,data.price.payPrice.get().length-3  , data.price.payPrice.get().length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(activity) / 7 * 0.6 ).toInt()) , 1 , data.price.payPrice.get().length-3 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun createOrderResult(pay: OrderPayModel) {
        getEventCenter().post(CartDataChange())
        if (isNeedPay){
            push("/order/pay",{
                it.withObject("pay",pay)
            })
        }else{
            showMessage("订单提交完成")
        }
        finish()
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:09
     * @Note   错误回调
     * @param  message 错误信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:09
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:10
     * @Note   开始回调
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:11
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderCreateActivity
     * @Date   2018/5/23 上午10:11
     * @Note   销毁回调
     */
    override fun destory() {

    }

}