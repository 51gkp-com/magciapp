package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import android.os.Handler
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.adapter.AftersaleItemAdapter
import com.enation.javashop.android.component.order.databinding.AftersaleListActLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.middleware.logic.contract.order.AftersaleListContract
import com.enation.javashop.android.middleware.logic.presenter.order.AftersaleListPresenter
import com.enation.javashop.android.middleware.model.AftersaleListModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.aftersale_list_act_lay.*

@Router(path = "/aftersale/list")
class AftersaleListActivity :BaseActivity<AftersaleListPresenter,AftersaleListActLayBinding>(),AftersaleListContract.View {

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
    private val adapter = AftersaleItemAdapter(ArrayList())


    override fun getLayId(): Int {
        return R.layout.aftersale_list_act_lay
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
        delegateAdapter.addAdapter(adapter)

        /**配置到RecycleView*/
        after_sale_list_list.layoutManager = virtualLayoutManager
        after_sale_list_list.adapter = delegateAdapter
        after_sale_detail_refresh.setRefreshFooter(ClassicsFooter(this))
        presenter.load(page)
    }

    override fun bindEvent() {
        after_sale_detail_refresh.setOnLoadMoreListener {
            page += 1
            presenter.load(page)
        }
        adapter.setOnItemClickListener { data, position ->
            push("/aftersale/detail",{
                it.withString("sn",data.sn)
            })
        }
        after_sale_list_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun destory() {

    }

    override fun render(datas: ArrayList<AftersaleListModel>) {
        if (page == 1){
            adapter.data.clear()
            adapter.data.addAll(datas)
            adapter.notifyDataSetChanged()
            after_sale_detail_refresh.resetNoMoreData()
            after_sale_detail_refresh.finishLoadMore()
        }else{
            adapter.data.addAll(datas)
            adapter.notifyDataSetChanged()
            if (datas.size > 0){
                after_sale_detail_refresh.finishLoadMore()
            }else{
                after_sale_detail_refresh.finishLoadMoreWithNoMoreData()
            }
        }
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
}