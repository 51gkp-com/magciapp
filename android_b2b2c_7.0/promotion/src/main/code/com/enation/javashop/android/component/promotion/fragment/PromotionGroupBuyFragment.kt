package com.enation.javashop.android.component.promotion.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.adapter.PromotionGroupBuyGoodsAdapter
import com.enation.javashop.android.component.promotion.databinding.PromotionGroupbuyFragBinding
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.bind.BaseBindingHelper
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionGroupBuyFragContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.PromotionGroupBuyFragPresenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.promotion_groupbuy_frag.*

/**
 * @author LDD
 * @Date   2018/5/22 下午2:33
 * @From   com.enation.javashop.android.component.promotion.fragment
 * @Note   团购子页面
 */
class PromotionGroupBuyFragment : BaseFragment<PromotionGroupBuyFragPresenter,PromotionGroupbuyFragBinding>(),PromotionGroupBuyFragContract.View {

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
     * @Type  PromotionSecKillGoodsAdapter
     * @Note  商品适配器
     */
    private val adapter = PromotionGroupBuyGoodsAdapter(ArrayList())

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页查询
     */
    private var page = 1

    /**
     * @Name  pointId
     * @Type  Int
     * @Note  索引
     */
    var groupId = -1

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:42
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.promotion_groupbuy_frag
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:44
     * @Note   依赖注入
     */
    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:45
     * @Note   初始化
     */
    override fun init() {
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        refresh.setRefreshFooter(ClassicsFooter(activity))
        promotion_groupbuy_frag_rv.layoutManager = virtualLayoutManager
        promotion_groupbuy_frag_rv.adapter = delegateAdapter
        delegateAdapter.addAdapter(adapter)
        presenter.loadGroupBuy(groupId,page)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:45
     * @Note   绑定事件
     */
    override fun bindEvent() {
        refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadGroupBuy(groupId,page)
        }
        adapter.setOnItemClickListener { data, position ->
            push("/goods/detail",{
                it.withInt("goodsId",data.goodsId)
            })
        }
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:46
     * @Note   渲染团购
     */
    override fun renderGroupBuy(data: ArrayList<GoodsItemViewModel>) {
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

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:46
     * @Note   错误回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:46
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:47
     * @Note   开始回调
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:47
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyFragment
     * @Date   2018/5/22 下午2:47
     * @Note   销毁
     */
    override fun destory() {

    }

}