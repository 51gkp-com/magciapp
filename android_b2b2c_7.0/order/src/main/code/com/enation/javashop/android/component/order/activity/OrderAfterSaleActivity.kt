package com.enation.javashop.android.component.order.activity

import android.databinding.ObservableField
import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.*
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.component.order.vo.OrderDescribeViewModel
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.OrderDetailDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderAfterSaleContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderAfterSalePresenter
import com.enation.javashop.android.middleware.model.AccountInfo
import com.enation.javashop.android.middleware.model.AfterSaleViewModel
import com.enation.javashop.android.middleware.model.ReturnData
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.order_after_sale_lay.*
import java.util.ArrayList

/**
 * @author LDD
 * @Date   2018/4/23 上午8:24
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单售后页面
 */
@Router(path = "/order/after-sale")
class OrderAfterSaleActivity :BaseActivity<OrderAfterSalePresenter,OrderAfterSaleLayBinding>(),OrderAfterSaleContract.View ,AfterSaleAgreement{

    @Autowired(name = "orderSn",required = true)
    @JvmField var orderSn :String = ""

    @Autowired(name = "skuId",required = true)
    @JvmField var skuId :Int = 0

    /**
     * @Name  adapters
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapters = ArrayList<DelegateAdapter.Adapter<*>>()

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


    private var type = 0

    private var des = OrderDescribeViewModel(ObservableField(""))

    private var reson = ""

    private var data = AfterSaleViewModel()

    private var account = AccountInfo()

    private val payTypeAdapter by lazy { OrderAfterSaleTypeAdapter(type,this) }

    private var payInfoAdapter = OrderAfterSalePayAdapter(account,this)

    private var resonAdapter =  OrderAfterSaleResonAdapter(reson,this)

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:51
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_after_sale_lay
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:52
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:52
     * @Note   初始化
     */
    override fun init() {
        type = (skuId > 0).judge(0,1)
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this,Color.BLACK)

        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        /**配置到RecycleView*/
        order_after_sale_rv.layoutManager = virtualLayoutManager

        order_after_sale_rv.adapter = delegateAdapter

        presenter.loadAfterSaleData(orderSn, skuId)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:52
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_after_sale_topbar.setLeftClickListener {
            pop()
        }
    }


    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:53
     * @Note   渲染UI
     */
    override fun renderAfterSaleUI(data: AfterSaleViewModel) {
        this.data = data
        adapters.clear()
        adapters.add(payTypeAdapter)
        adapters.add(OrderAfterSaleGoodsAdapter(skuId < 1,this.data,this))
        adapters.add(payInfoAdapter)
        adapters.add(resonAdapter)
        adapters.add(OrderAfterSaleDescribeAdapter(des,virtualLayoutManager.weak(),this))
        adapters.add(OrderAfterSaleConfrimAdapter(this))
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun serviceSelect(type: Int) {
        if(skuId > 0){
            this.type = type
            payTypeAdapter.type = type
            payTypeAdapter.notifyDataSetChanged()
        }
    }

    override fun payInfo(data: AccountInfo) {
        this.account = data
        payInfoAdapter.payData = data
        payInfoAdapter.notifyDataSetChanged()
    }

    override fun reson(reson: String) {
        this.reson = reson
        resonAdapter.reson = reson
        resonAdapter.notifyDataSetChanged()
    }

    override fun comfrim() {

        val result = ReturnData()

        result.orderSn = orderSn

        if (skuId > 0){
            result.skuId = skuId
            result.returnNum = data.returnGoodsList[0].buyNum
        }

        result.refundReason = reson
        result.customerRemark = des.text.get()
        result.accountType = account.accountType
        if (account.accountType == "BANKTRANSFER"){
            result.bankName = account.bankName.get()
            result.bankAccountNumber= account.bankAccountNumber.get()
            result.bankAccountName = account.bankAccountName.get()
            result.bankDepositName = account.bankDepositName.get()
        }else{
            result.returnAccount = account.returnAccount.get()
        }
        if (type == 0){
            presenter.commitGoods(result)
        }else{
            presenter.commitMoney(result)
        }

    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:53
     * @Note   错误回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
          showMessage(message)
          dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:54
     * @Note   成功回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
        if(message.contains("成功")){
            getEventCenter().post(OrderDetailDataChange())
            pop()
        }
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:54
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:54
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderAfterSaleActivity
     * @Date   2018/4/24 下午3:55
     * @Note   销毁
     */
    override fun destory() {

    }

}