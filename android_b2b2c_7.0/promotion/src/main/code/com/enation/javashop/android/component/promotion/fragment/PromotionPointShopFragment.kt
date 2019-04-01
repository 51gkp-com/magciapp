package com.enation.javashop.android.component.promotion.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.adapter.PromotionPointShopGoodsAdapter
import com.enation.javashop.android.component.promotion.databinding.PromotionPointShopFragBinding
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionPointShopFragContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.PromotionPointShopFragPresenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.promotion_point_shop_frag.*

/**
 * @author LDD
 * @Date   2018/5/22 下午2:56
 * @From   com.enation.javashop.android.component.promotion.fragment
 * @Note   积分商城子页面
 */
class PromotionPointShopFragment : BaseFragment<PromotionPointShopFragPresenter, PromotionPointShopFragBinding>(), PromotionPointShopFragContract.View {

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
    private val adapter = PromotionPointShopGoodsAdapter(ArrayList())

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
    var pointId = -1


    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:42
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.promotion_point_shop_frag
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:44
     * @Note   依赖注入
     */
    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:45
     * @Note   初始化
     */
    override fun init() {
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mViewDataBinding.promotionPointShopFragRv.layoutManager = virtualLayoutManager
        mViewDataBinding.promotionPointShopFragRv.adapter = delegateAdapter
        mViewDataBinding.refresh.setRefreshFooter(ClassicsFooter(activity))
        delegateAdapter.addAdapter(adapter)
        presenter.loadPointShop(pointId,page)
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:45
     * @Note   绑定事件
     */
    override fun bindEvent() {
        mViewDataBinding.refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadPointShop(pointId,page)
        }
        adapter.setOnItemClickListener { data, position ->
            push("/goods/detail",{
                it.withInt("goodsId",data.goodsId)
            })
        }
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:46
     * @Note   渲染团购
     */
    override fun renderPointShop(data: ArrayList<GoodsItemViewModel>) {
        if (page == 1){
            mViewDataBinding.refresh.resetNoMoreData()
            if (data.size >0){
                adapter.data.clear()
                adapter.data.addAll(data)
                adapter.notifyDataSetChanged()
            }
            mViewDataBinding.refresh.finishLoadMore()
        }else {
            if (data.size > 0) {
                adapter.data.addAll(data)
                adapter.notifyDataSetChanged()
                mViewDataBinding.refresh.finishLoadMore()
            }else{
                mViewDataBinding.refresh.finishLoadMoreWithNoMoreData()
            }
        }
    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:46
     * @Note   错误回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:46
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:47
     * @Note   开始回调
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:47
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PromotionPointShopFragment
     * @Date   2018/5/22 下午2:47
     * @Note   销毁
     */
    override fun destory() {

    }

}