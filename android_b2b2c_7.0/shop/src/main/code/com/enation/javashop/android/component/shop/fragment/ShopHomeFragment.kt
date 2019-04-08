package com.enation.javashop.android.component.shop.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.adapter.ShopHomeHeaderAdapter
import com.enation.javashop.android.component.shop.adapter.ShopHomeItemAdapter
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopHomeFragLayBinding
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.middleware.logic.contract.shop.ShopHomeContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopHomePresenter
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.ShopFirstViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.shop_home_frag_lay.*

/**
 * @author LDD
 * @Date   2018/4/10 上午8:22
 * @From   com.enation.javashop.android.component.shop.fragment
 * @Note   商品首页
 */
class ShopHomeFragment :BaseFragment<ShopHomePresenter,ShopHomeFragLayBinding>(),ShopHomeContract.View, ShopActionAgreement {

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
     * @Name  adapters
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapters = ArrayList<DelegateAdapter.Adapter<*>>()

    /**
     * @Name shopId
     * @Type Int
     * @Note 店铺id
     */
    var shopId : Int = 0

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:20
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.shop_home_frag_lay
    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:20
     * @Note   依赖注入
     * @param
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:21
     * @Note   初始化
     */
    override fun init() {
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        /**配置到RecycleView*/
        shop_home_rv.layoutManager = virtualLayoutManager
        shop_home_rv.adapter = delegateAdapter
        presenter.loadFirstData(shopId)
    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:21
     * @Note   绑定事件
     */
    override fun bindEvent() {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:21
     * @Note   渲染店铺首页数据
     * @param  data 数据
     */
    override fun initFirstData(data: ShopFirstViewModel) {
        adapters.add(ShopHomeHeaderAdapter("新品上架"))
        adapters.add(ShopHomeItemAdapter( data.newGoods))
        adapters.add(ShopHomeHeaderAdapter("店铺热卖"))
        adapters.add(ShopHomeItemAdapter( data.hotGoods))
        adapters.add(ShopHomeHeaderAdapter("店家推荐"))
        adapters.add(ShopHomeItemAdapter( data.recommendGoods))
        delegateAdapter.addAdapters(adapters)

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:22
     * @Note   渲染优惠券UI
     * @param  data 优惠券信息
     */
    override fun initCoupon(data: List<CouponViewModel>) {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:22
     * @Note   错误回调
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:22
     * @Note   成功回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:23
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:23
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   ShopHomeFragment
     * @Date   2018/4/24 下午3:24
     * @Note   销毁
     */
    override fun destory() {

    }

    override fun toGoods(goodsId: Int) {
        println("商品页：${goodsId}")
    }
}