package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import android.os.Handler
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberCommentListLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.ReflexHelper
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.middleware.logic.contract.member.CommentListContract
import com.enation.javashop.android.middleware.logic.presenter.member.CommentListPresenter
import com.enation.javashop.android.middleware.model.OrderItemViewModel
import com.enation.javashop.net.engine.model.NetState
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.member_comment_list_lay.*

/**
 * @author LDD
 * @Date   2018/4/24 下午6:17
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   评论中心
 */
@Router(path = "/member/comment/list")
class CommentListActivity : BaseActivity<CommentListPresenter,MemberCommentListLayBinding>(),CommentListContract.View {

    /**
     * @Name  data
     * @Type  ArrayList<OrderItemViewModel>
     * @Note  评论订单列表数据源
     */
    private val data = ArrayList<OrderItemViewModel>()

    /**
     * @Name  adapter
     * @Type  BaseDelegateAdapter
     * @Note  评论列表适配器
     */
    private val adapter by lazy {
        ReflexHelper.build("com.enation.javashop.android.component.order.adapter.OrderItemAdapter")
                .newInstance(data).getInstance<BaseDelegateAdapter<*,*>>()
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
     * @Name  page
     * @Type  Int
     * @Note  推荐商品分页
     */
    private var page = 1

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:18
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_comment_list_lay
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:18
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:18
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        memeber_commont_list_rv.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(adapter)
        memeber_commont_list_rv.adapter = delegateAdapter
        presenter.loadComment(page)
        refresh.setRefreshFooter(ClassicsFooter(this))
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:19
     * @Note   绑定事件
     */
    override fun bindEvent() {
        memeber_commont_list_topbar.setLeftClickListener {
            pop()
        }
        refresh.setEnableRefresh(false)
        refresh.setOnLoadMoreListener {
            presenter.loadComment(page)
        }
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:19
     * @Note   渲染评论列表UI
     * @param  data 需要评论的订单
     */
    override fun renderCommentUi(data: ArrayList<OrderItemViewModel>) {
            if(page == 1){
                refresh.resetNoMoreData()
                this.data.clear()
                this.data.addAll(data)
                adapter.notifyDataSetChanged()
            }else{
                this.data.addAll(data)
                adapter.notifyDataSetChanged()
            }
            if (data.size == 0){
                refresh.finishLoadMoreWithNoMoreData()
            }else{
                refresh.finishRefresh()
            }
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:19
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:19
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:20
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:20
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   CommentListActivity
     * @Date   2018/4/24 下午6:20
     * @Note   销毁回调
     */
    override fun destory() {

    }

}