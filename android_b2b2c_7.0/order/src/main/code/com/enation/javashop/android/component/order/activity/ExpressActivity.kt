package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.ExpressHeaderAdapter
import com.enation.javashop.android.component.order.adapter.ExpressItemAdapter
import com.enation.javashop.android.component.order.databinding.ExpressActLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.middleware.logic.contract.order.ExpressContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderExpressPresenter
import com.enation.javashop.android.middleware.model.OrderExpressHeaderModel
import com.enation.javashop.android.middleware.model.OrderExpressModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.express_act_lay.*
import kotlinx.android.synthetic.main.order_detail_lay.*

/**
 * Created by LDD on 2018/11/1.
 */
@Router(path = "/order/express")
class ExpressActivity :BaseActivity<OrderExpressPresenter,ExpressActLayBinding>(),ExpressContract.View {

    @Autowired(name = "orderSn",required = true)
    @JvmField var orderSn :String = ""

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

    override fun getLayId(): Int {
        return R.layout.express_act_lay
    }

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
        express_list.layoutManager = virtualLayoutManager
        express_list.adapter = delegateAdapter
        presenter.load(orderSn)
    }

    override fun bindEvent() {
        express_topbar.setLeftClickListener { pop() }
    }

    override fun render(headerInfo: OrderExpressHeaderModel, message: ArrayList<OrderExpressModel>) {
        adapters.add(ExpressHeaderAdapter(headerInfo))
        adapters.add(ExpressItemAdapter(message))
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    override fun start() {
        showDialog()
    }

    override fun networkMonitor(state: NetState) {

    }

    override fun destory() {

    }
}