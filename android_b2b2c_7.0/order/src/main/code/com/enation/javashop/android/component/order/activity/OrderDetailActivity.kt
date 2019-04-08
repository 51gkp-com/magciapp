package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.*
import com.enation.javashop.android.component.order.agreement.OrderDetailAgreement
import com.enation.javashop.android.component.order.databinding.OrderDetailLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.component.order.vo.OrderCancleResonViewModel
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.TextViewDelegateAdapter
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopCommonView
import com.enation.javashop.android.middleware.event.OrderDetailDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderDetailContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderDetailPresenter
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import kotlinx.android.synthetic.main.order_detail_lay.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author LDD
 * @Date   2018/4/18 下午4:27
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单详细
 */
@Router(path = "/order/detail")
class OrderDetailActivity : BaseActivity<OrderDetailPresenter,OrderDetailLayBinding>(),OrderDetailContract.View,OrderDetailAgreement {

    @Autowired(name = "orderSn",required = true)
    @JvmField var orderSn :String = ""

    var order = OrderDetailViewModel()

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页查询索引
     */
    private var page = 1

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

    private var resons = OrderCancleResonViewModel.createResons()

    /**
     * @Name  cancleOrderView
     * @Type  PopCommonView
     * @Note  去掉订单PopWindow
     */
    private val cancleOrderView by lazy {
        PopCommonView.build(this)
                .setTitle("取消订单")
                .setConfrimTitle("提交")
                .setConfirmBackgroundColor(getColorCompatible(R.color.javashop_color_line_gray))
                .setAdapters(TextViewDelegateAdapter("温馨提示 \n1.限时特价，预约资格等购买优惠可能一并取消\n2.如遇到订单拆分，优惠券将换成同等价值京东返还\n3.支付券不予返还，支付优惠一并取消\n4.订单一旦取消，无法恢复",23,Color.BLACK,10.dpToPx(),10.dpToPx(),10.dpToPx(),10.dpToPx()))
                .setAdapters(TextViewDelegateAdapter("请选择取消订单的原因（必选）:",27,Color.BLACK,20.dpToPx(),5.dpToPx(),10.dpToPx(),10.dpToPx()))
                .then {
                    self ->
                    self.setAdapters(OrderCancleResonAdapter(resons) { state ->
                        if (state == 1){
                            self.setConfirmBackgroundColor(getColorCompatible(R.color.javashop_color_pay_red))
                        }else{
                            self.setConfirmBackgroundColor(getColorCompatible(R.color.javashop_color_line_gray))
                        }
                    })
                }.setConfirmListener {
                    var reson = ""
                    resons.forEach {
                        if(it.selectObservable.get()){
                            reson = it.title
                        }
                    }
                    if (reson == ""){
                        showMessage("请选择原因")
                        return@setConfirmListener
                    }
                    presenter.cancel(orderSn,reson)
                }
    }

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:56
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_detail_lay
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:57
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        /**配置到RecycleView*/
        order_detail_rv.layoutManager = virtualLayoutManager
        order_detail_rv.adapter = delegateAdapter
        presenter.loadOrder(orderSn)
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:58
     * @Note   绑定事件
     */
    override fun bindEvent() {

        getEventCenter().register(OrderDetailDataChange::class.java) {
            presenter.loadOrder(orderSn)
        }.joinManager(disposableManager)

        order_detail_action_cancle.setOnClickListener(OnClickListenerAntiViolence({
            if (order.orderActionModel!!.allowServiceCancel){
                push("/order/after-sale",{
                    it.withString("orderSn",orderSn)
                })
            }else{
                cancleOrderView.show(order_detail_topbar.holderViewProvider())
            }
        }))

        order_detail_action_comment.setOnClickListener {
            push("/member/comment/post",{
                it.withString("orderSn",orderSn)
            })
        }

        order_detail_action_rog.setOnClickListener {
            CommonTool.createVerifyDialog("请收到货后，再确认收货！否则您可能钱货两空！","我再想想","确认收货",this,object :CommonTool.DialogInterface{
                override fun yes() {
                    presenter.rog(orderSn)
                }

                override fun no() {

                }
            })
        }

        order_detail_action_logistics.setOnClickListener {
            push("/order/express",{
                it.withString("orderSn",orderSn)
            })
        }

        order_detail_action_after.setOnClickListener {
            push("/order/after-sale",{
                it.withString("orderSn",orderSn)
            })
        }

        order_detail_action_pay.setOnClickListener {
            val data = OrderPayModel()
            data.tradeType = TradeType.Order
            data.sn = orderSn
            data.payPrice = order.needPayPrice
            push("/order/pay",{
                it.withObject("pay",data)
            })
        }
        order_detail_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun notification() {
        presenter.loadOrder(orderSn)
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:58
     * @Note   渲染订单
     * @param  order 订单数据
     */
    override fun renderOrder(data :ArrayList<Any>) {

        adapters.clear()

        order = data[0] as OrderDetailViewModel
        val goods = data[1] as ArrayList<RecommendGoodsViewModel>

        mViewBinding.data = order.orderActionModel
        adapters.add(OrderDetailHeaderAdapter(order))
        if (order.lastExpressText.isNotEmpty()){
            adapters.add(OrderDetailExpressAdapter(LogisticsViewModel(order.lastModify,order.lastExpressText,0)).then {
                it.setOnItemClickListener { data, position ->
                    push("/order/express",{
                        it.withString("orderSn",orderSn)
                    })
                }
            })
        }
        adapters.add(OrderDetailAddressAdapter(order))
        adapters.add(OrderDetailShopAdapter(order))
        adapters.add(OrderDetailGoodsAdapter(this,order.goodsList).then {
            it.setOnItemClickListener { data, position ->
                push("/goods/detail",{
                    it.withInt("goodsId",data.goodsId)
                })
            }
        })
        if(order.gifts.size > 0){
            adapters.add(OrderCreateGiftItemAdapter(order.gifts[0]))
        }
        if(order.coupons.size > 0){
            adapters.add(OrderCreateGiftCouponItemAdapter(order.coupons[0]))

        }
        adapters.add(OrderDetailInfoAdapter(order))
        adapters.add(OrderDetailPriceAdapter(order))

        val adapter = ReflexHelper.build("com.enation.javashop.android.component.member.adapter.MemberRecommendGoodsAdapter").newInstance(goods).getInstance<BaseDelegateAdapter<*,RecommendGoodsViewModel>>()
        adapter.setOnItemClickListener { data, position ->
            push("/goods/detail",{
                it.withInt("goodsId",data.id)
            })
        }
        val titleAdapter = ReflexHelper.build("com.enation.javashop.android.component.member.adapter.MemberRecommendTitleAdapter").newInstance(ReflexHelper.ReflexFieldShell(0)).getInstance<DelegateAdapter.Adapter<*>>()
        adapters.add(titleAdapter)
        adapters.add(adapter)
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }



    override fun addCart(skuId: Int) {
        presenter.addCart(skuId)
    }

    override fun toAfterSale(skuId: Int) {
        push("/order/after-sale",{
            it.withString("orderSn",orderSn)
            it.withInt("skuId",skuId)
        })
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:59
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午3:59
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午4:00
     * @Note   开始请求
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午4:00
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderDetailActivity
     * @Date   2018/4/24 下午4:00
     * @Note   销毁
     */
    override fun destory() {

    }
}