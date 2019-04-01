package com.enation.javashop.android.component.promotion.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.adapter.PromotionSecKillGoodsAdapter
import com.enation.javashop.android.component.promotion.adapter.PromotionSecKillHeaderAdapter
import com.enation.javashop.android.component.promotion.databinding.PromotionSeckillFragBinding
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.TimeEngine
import com.enation.javashop.android.lib.utils.TimeHandle
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.android.middleware.logic.contract.promotion.PromotionSecKillFragContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.PromotionSecKillFragPresenter
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.SecKillHeaderViewModel
import com.enation.javashop.android.middleware.model.SecKillListViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.promotion_seckill_frag.*

/**
 * @author LDD
 * @Date   2018/5/21 下午4:32
 * @From   com.enation.javashop.android.component.promotion.fragment
 * @Note   秒杀Frag
 */
class PromotionSecKillFragment :BaseFragment<PromotionSecKillFragPresenter,PromotionSeckillFragBinding>() ,PromotionSecKillFragContract.View {

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
    private val adapter = PromotionSecKillGoodsAdapter(ArrayList())

    /**
     * @Name  adapter
     * @Type  PromotionSecKillHeaderAdapter
     * @Note  头部适配器
     */
    private val header by lazy {
        var title = "抢购中，先下单先得哦"
        var type  = "距结束"
        var time : Int = 0
        if (next == null){
            time = ((TimeHandle.getCurrentDay24Mill() - System.currentTimeMillis()) / 1000).toInt()
        }else{
            time = next!!.distanceTime
        }
        if (current.distanceTime !=  0){
            title = "即将开始 先下单先得哦"
            type = "距开始"
            time = current.distanceTime
        }
        PromotionSecKillHeaderAdapter(weak(), SecKillHeaderViewModel(title,type,time),complete)
    }

    lateinit var complete :()->Unit

    /**
     * @Name  current
     * @Type  SecKillListViewModel
     * @Note  当前秒杀时间段
     */
    lateinit var current : SecKillListViewModel

    /**
     * @Name  next
     * @Type  SecKillListViewModel
     * @Note  当前秒杀时间段
     */
    var next : SecKillListViewModel? = null

    /**
     * @Name  page
     * @Type  Int
     * @Note  分页查询
     */
    private var page = 1

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:35
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.promotion_seckill_frag
    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:36
     * @Note   依赖注入
     */
    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:38
     * @Note   初始化
     */
    override fun init() {
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        promotion_seckill_frag_rv.layoutManager = virtualLayoutManager
        promotion_seckill_frag_rv.adapter = delegateAdapter
        mViewDataBinding.refresh.setRefreshFooter(ClassicsFooter(activity))
        delegateAdapter.addAdapter(header)
        delegateAdapter.addAdapter(adapter)
        presenter.loadSecGoods(current.text,page)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:38
     * @Note   绑定事件
     */
    override fun bindEvent() {
        mViewDataBinding.refresh.setOnLoadMoreListener {
            page += 1
            presenter.loadSecGoods(current.text,page)
        }
    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:38
     * @Note   渲染秒杀商品
     * @param  data 商品数据
     */
    override fun renderGoodsList(data: ArrayList<GoodsItemViewModel>) {
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
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:39
     * @Note   错误
     * @param  message 信息
     * @param  type 类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:39
     * @Note   完成
     * @param  message 信息
     * @param  type 类型
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:39
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:39
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PromotionSecKillFragment
     * @Date   2018/5/21 下午4:40
     * @Note   销毁
     */
    override fun destory() {

    }
}