package com.enation.javashop.android.component.shop.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.adapter.ShopHomeItemAdapter
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopTagFragLayBinding
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.middleware.logic.contract.shop.ShopTagContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopTagPresenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.shop_tag_frag_lay.*

/**
 * @author LDD
 * @Date   2018/4/10 上午8:38
 * @From   com.enation.javashop.android.component.shop.fragment
 * @Note   店铺标签页
 */
class ShopTagFragment : BaseFragment<ShopTagPresenter,ShopTagFragLayBinding>(),ShopTagContract.View, ShopActionAgreement {


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
     * @Type  GoodsMoreActionAdapter
     * @Note  筛选列表适配器
     */
    private var adapter = ShopHomeItemAdapter(ArrayList())

    /**
     * @Name  searchTag
     * @Type  String
     * @Note  标签
     */
    var searchTag = ""

    /**
     * @Name shopId
     * @Type Int
     * @Note 店铺id
     */
    var shopId : Int = 0

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:25
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.shop_tag_frag_lay
    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:25
     * @Note   依赖注入
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:25
     * @Note   初始化
     */
    override fun init() {
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        /**配置到RecycleView*/
        shop_tag_rv.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(adapter)
        shop_tag_rv.adapter = delegateAdapter

        presenter.loadGoods(searchTag,shopId)
    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:26
     * @Note   绑定事件
     */
    override fun bindEvent() {

    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:26
     * @Note   初始化商品数据
     * @param  data 商品数据
     */
    override fun initGoods(data: List<GoodsItemViewModel>) {
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:26
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:27
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:27
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:27
     * @Note   网络实施监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   ShopTagFragment
     * @Date   2018/4/24 下午3:27
     * @Note   错误回调
     */
    override fun destory() {

    }

    override fun toGoods(goodsId: Int) {
        println("商品页：${goodsId}")
    }
}