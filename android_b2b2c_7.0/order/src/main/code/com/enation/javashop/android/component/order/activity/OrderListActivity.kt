package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import android.os.Handler
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.OrderCancleResonAdapter
import com.enation.javashop.android.component.order.adapter.OrderItemAdapter
import com.enation.javashop.android.component.order.agreement.OrderListAgreement
import com.enation.javashop.android.component.order.databinding.OrderListActLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.component.order.vo.OrderCancleResonViewModel
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.TextViewDelegateAdapter
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopCommonView
import com.enation.javashop.android.middleware.bind.DataBindingAction
import com.enation.javashop.android.middleware.event.OrderListDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderListContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderListPresenter
import com.enation.javashop.android.middleware.model.OrderItemViewModel
import com.enation.javashop.android.middleware.model.OrderPayModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.order_list_act_lay.*

/**
 * @author LDD
 * @Date   2018/4/16 下午4:48
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单列表页面
 */
@Router(path = "/order/list")
class OrderListActivity :BaseActivity<OrderListPresenter,OrderListActLayBinding>() , OrderListContract.View , OrderListAgreement {

    @Autowired(name= "state",required = true)
    @JvmField var state : Int = 0

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
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @Name  adapter
     * @Type  OrderItemAdapter
     * @Note  订单适配器
     */
    private val adapter = OrderItemAdapter(ArrayList(),this)

    /**
     * @Name  cancleOrderView
     * @Type  PopCommonView
     * @Note  去掉订单PopWindow
     */
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
                }
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:48
     * @Note   获取布局ID
     */
    override fun getLayId(): Int {
        return R.layout.order_list_act_lay
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:50
     * @Note   依赖注入
     */
    override fun bindDagger() {
        return OrderLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:50
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapter(adapter)

        /**配置到RecycleView*/
        order_list_rv.layoutManager = virtualLayoutManager
        order_list_rv.adapter = delegateAdapter

        if (state == DataBindingAction.ORDER_ALL){
            updateState(order_list_filter_all)
        }else if(state == DataBindingAction.ORDER_WAIT_PAY){
            updateState(order_list_filter_wait_pay)
        }else if(state == DataBindingAction.ORDER_WAIT_ROG){
            updateState(order_list_filter_wait_rog)
        }
        loadData()
    }

    fun loadData(){
        var stateParam = ""

        when(state){
            DataBindingAction.ORDER_ALL ->{
                stateParam = "ALL"
            }
            DataBindingAction.ORDER_WAIT_PAY ->{
                stateParam = "WAIT_PAY"
            }
            DataBindingAction.ORDER_WAIT_ROG ->{
                stateParam = "WAIT_ROG"
            }
            DataBindingAction.ORDER_COMPLTE ->{
                stateParam = "COMPLETE"
            }
            DataBindingAction.ORDER_CANCEL ->{
                stateParam = "CANCELLED"
            }
        }
        refresh.setRefreshFooter(ClassicsFooter(this))
        presenter.loadOrderList(stateParam,page)
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:50
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_list_topbar.setLeftClickListener {
            pop()
        }
        val stateList = arrayListOf(order_list_filter_all,
                order_list_filter_wait_pay,
                order_list_filter_wait_rog,
                order_list_filter_complete,
                order_list_filter_cancle)
        for (autoSizeTextView in stateList) {
            autoSizeTextView.setOnClickListener {
                updateState(it)
                page = 1
                loadData()
            }
        }
        refresh.setOnLoadMoreListener {
            page += 1
            loadData()
        }
        getEventCenter().register(OrderListDataChange::class.java) {
            page = 1
            loadData()
        }.joinManager(disposableManager)
    }

    fun updateState(it : View){
        order_list_filter_all.setTextColor(Color.parseColor("#666666"))
        order_list_filter_wait_pay.setTextColor(Color.parseColor("#666666"))
        order_list_filter_wait_rog.setTextColor(Color.parseColor("#666666"))
        order_list_filter_complete.setTextColor(Color.parseColor("#666666"))
        order_list_filter_cancle.setTextColor(Color.parseColor("#666666"))
        order_list_filter_all_indicator.gone()
        order_list_filter_wap_pay_indicator.gone()
        order_list_filter_wap_rog_indicator.gone()
        order_list_filter_complete_indicator.gone()
        order_list_filter_cancle_indicator.gone()
        when(it){
            order_list_filter_all ->{
                order_list_filter_all.setTextColor(Color.parseColor("#e1373b"))
                order_list_filter_all_indicator.visable()
                state = DataBindingAction.ORDER_ALL
            }
            order_list_filter_wait_pay ->{
                order_list_filter_wait_pay.setTextColor(Color.parseColor("#e1373b"))
                order_list_filter_wap_pay_indicator.visable()
                state = DataBindingAction.ORDER_WAIT_PAY
            }
            order_list_filter_wait_rog ->{
                order_list_filter_wait_rog.setTextColor(Color.parseColor("#e1373b"))
                order_list_filter_wap_rog_indicator.visable()
                state = DataBindingAction.ORDER_WAIT_ROG
            }
            order_list_filter_complete ->{
                order_list_filter_complete.setTextColor(Color.parseColor("#e1373b"))
                order_list_filter_complete_indicator.visable()
                state = DataBindingAction.ORDER_COMPLTE
            }
            order_list_filter_cancle ->{
                order_list_filter_cancle.setTextColor(Color.parseColor("#e1373b"))
                order_list_filter_cancle_indicator.visable()
                state = DataBindingAction.ORDER_CANCEL
            }
        }
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:51
     * @Note   订单列表渲染
     */
    override fun initOrderList(orderList: ArrayList<OrderItemViewModel>) {
        if (page == 1){
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            refresh.resetNoMoreData()
        }
        if (orderList.size > 0){
            refresh.finishLoadMore()
        }else{
            refresh.finishLoadMoreWithNoMoreData()
        }
        adapter.data.addAll(orderList)
        adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:51
     * @Note   请求错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }


    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:51
     * @Note   请求完成
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
        if (type == 3){
            page = 1
            loadData()
        }
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:52
     * @Note   请求开始
     */
    override fun start() {
        showDialog()
    }

    override fun comment(sn: String) {
        push("/member/comment/post",{
            it.withString("orderSn",sn)
        })
    }

    override fun cancel(sn: String) {
        cancleOrderView.setConfirmListener {
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
            presenter.cancal(sn,reson)
        }.show(order_list_topbar.holderViewProvider())
    }

    override fun applyServerCancel(sn: String) {
        push("/order/after-sale",{
            it.withString("orderSn",sn)
        })
    }

    override fun express(sn: String) {
        push("/order/express",{
            it.withString("orderSn",sn)
        })
    }

    override fun pay(data: OrderPayModel) {
        push("/order/pay",{
            it.withObject("pay",data)
        })
    }

    override fun rog(sn: String) {
        CommonTool.createVerifyDialog("请收到货后，再确认收货！否则您可能钱货两空！","我再想想","确认收货",this,object : CommonTool.DialogInterface{
            override fun yes() {
                presenter.rog(sn)
            }

            override fun no() {

            }
        }).show()
    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:52
     * @Note   网络试试监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderListActivity
     * @Date   2018/4/16 下午4:52
     * @Note   销毁回调
     */
    override fun destory() {

    }
}