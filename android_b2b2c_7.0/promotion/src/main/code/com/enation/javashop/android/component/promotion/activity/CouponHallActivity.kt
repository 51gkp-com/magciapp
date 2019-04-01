package com.enation.javashop.android.component.promotion.activity

import android.graphics.Color
import android.os.Handler
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.adapter.CouponItemAdapter
import com.enation.javashop.android.component.promotion.databinding.CouponHallLayBinding
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.middleware.logic.contract.promotion.CouponHallContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.CouponHallPresenter
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.coupon_hall_lay.*
import kotlinx.android.synthetic.main.promotion_groupbuy_frag.*

@Router(path = "/promotion/coupon/hall")
class CouponHallActivity : BaseActivity<CouponHallPresenter,CouponHallLayBinding>(),CouponHallContract.View {

    private val adapter = CouponItemAdapter(ArrayList())

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

    private var page = 1

    override fun getLayId(): Int {
        return  R.layout.coupon_hall_lay
    }

    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    override fun init() {
        /**初始化列表*/
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        coupon_list.layoutManager = virtualLayoutManager
        coupon_list.adapter = delegateAdapter
        delegateAdapter.addAdapter(adapter)
        presenter.load(page)
        coupon_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun bindEvent() {
        mViewBinding.refresh.setOnLoadMoreListener {
           Handler().postDelayed({
               page += 1
               presenter.load(page)
               it.finishLoadMore()
           },1500)
        }
        adapter.setOnItemClickListener { data, position ->
            presenter.getCoupon(data.id)
        }
        coupon_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun render(list: ArrayList<CouponViewModel>) {
        adapter.data.addAll(list)
        adapter.notifyDataSetChanged()
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

    override fun destory() {

    }
}