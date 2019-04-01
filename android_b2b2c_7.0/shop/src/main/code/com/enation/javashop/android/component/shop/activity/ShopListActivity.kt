package com.enation.javashop.android.component.shop.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.adapter.ShopitemAdapter
import com.enation.javashop.android.component.shop.databinding.ShooListActLayBinding
import com.enation.javashop.android.component.shop.databinding.ShopActLayBinding
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.middleware.logic.contract.shop.ShopActivityContract
import com.enation.javashop.android.middleware.logic.contract.shop.ShopListContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopActivityPersenter
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopListPersenter
import com.enation.javashop.android.middleware.model.ShopItem
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.shoo_list_act_lay.*
import kotlinx.android.synthetic.main.shop_all_frag_lay.*
import java.util.logging.Handler

/**
 * Created by LDD on 2018/10/15.
 */
@Router(path = "/shop/list")
class ShopListActivity : BaseActivity<ShopListPersenter, ShooListActLayBinding>(), ShopListContract.View {

    private var page :Int = 1

    private val adapter = ShopitemAdapter(ArrayList())

    /**
     * @Name  shopId
     * @Type  Int
     * @Note  店铺id（自动注入）
     */
    @Autowired(name= "keyword",required = true)
    @JvmField var keyword: String = ""

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

    override fun getLayId(): Int {
        return  R.layout.shoo_list_act_lay
    }

    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)

        shop_list_title_tv.text = keyword

        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapter(adapter)
        mViewBinding.refresh.setRefreshFooter(ClassicsFooter(activity))
        /**配置到RecycleView*/
        listView.layoutManager = virtualLayoutManager
        listView.adapter = delegateAdapter
        presenter.loadData(keyword,page)
    }

    override fun bindEvent() {

        shop_list_cancel.setOnClickListener {
            pop()
        }

        refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadData(keyword,page)
        }
        adapter.setOnItemClickListener { data, _ ->
            push("/shop/detail",{postcard ->
                postcard.withInt("shopId", data.shopId)
            })
        }
    }

    override fun destory() {

    }

    override fun render(data: ArrayList<ShopItem>) {
        if (page == 1){
            refresh.resetNoMoreData()
            if (data.size >0){
                adapter.data.clear()
                adapter.data.addAll(data)
                adapter.notifyDataSetChanged()
            }
            refresh.finishLoadMore()
        }else {
            if (data.size > 0) {
                adapter.data.addAll(data)
                adapter.notifyDataSetChanged()
                refresh.finishLoadMore()
            }else{
                refresh.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun start() {
        showDialog()
    }

    override fun networkMonitor(state: NetState) {

    }
}