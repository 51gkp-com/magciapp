package com.enation.javashop.android.component.order.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateSelectCouponLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.OrderCreateDataChange
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateCouponContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderCreateCouponPresenter
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.order_create_select_coupon_lay.*

/**
 * @author LDD
 * @Date   2018/5/23 下午6:56
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单创建优惠券页面
 */
@Router(path = "/order/create/coupon")
class OrderCreateCouponActivity :BaseActivity<OrderCreateCouponPresenter,OrderCreateSelectCouponLayBinding>(),OrderCreateCouponContract.View  {

    @Autowired(name = "ids" , required = true)
    @JvmField var ids : String = ""

    /**
     * @author LDD
     * @Date   2018/5/29 上午10:24
     * @From   OrderCreateCouponActivity
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.component.order.activity.OrderCreateCouponActivity.Companion.PARAMS_KEY_COUPON
         * @Type  String
         * @Note  参数Key
         */
        const val PARAMS_KEY_COUPON = "PARAMS_KEY_COUPON"

    }

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
     * @Name  data
     * @Type  ArrayList
     * @Note  优惠券集合
     */
    private var data :ArrayList<CouponViewModel>  = ArrayList()

    /**
     * @Name  adapter
     * @Type  Adapter
     * @Note  优惠券适配器
     */
    private val adapter by lazy {
        ReflexHelper.build("com.enation.javashop.android.component.member.adapter.CouponAdapter")
                .newInstance(data).getInstance<BaseDelegateAdapter<*,CouponViewModel>>()
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:05
     * @Note   获取layoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_create_select_coupon_lay
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:07
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:07
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
        order_create_select_coupon_rv.layoutManager = virtualLayoutManager
        order_create_select_coupon_rv.adapter = delegateAdapter

        presenter.loadCoupon(ids)
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:09
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_create_select_coupon_topbar.setLeftClickListener {
            pop()
        }
        adapter.setOnItemClickListener { data, position ->
            var id = data.id
            if (data.isSelect.get() == 1){
                id = 0
            }
            presenter.useCoupon(data.shopId,id)
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:10
     * @Note   渲染优惠券
     */
    override fun renderCoupon(coupons: ArrayList<CouponViewModel>) {
        this.data.clear()
        this.data.addAll(coupons)
        adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:10
     * @Note   错误回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:10
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {
        if (message != ""){
            presenter.loadCoupon("")
            showMessage("操作成功")
            getEventCenter().post(OrderCreateDataChange())
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:10
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:10
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderCreateCouponActivity
     * @Date   2018/5/23 下午7:11
     * @Note   销毁回调
     */
    override fun destory() {

    }
}