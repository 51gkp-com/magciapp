package com.enation.javashop.android.component.goods.fragment

import android.graphics.Color
import android.os.Handler
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.adapter.GoodsCommentItemAdapter
import com.enation.javashop.android.component.goods.databinding.GoodsCommentLayBinding
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.utils.to
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsCommentContract
import com.enation.javashop.android.middleware.logic.presenter.goods.GoodsCommentPresenter
import com.enation.javashop.android.middleware.model.CommentNumViewModel
import com.enation.javashop.android.middleware.model.GoodsCommentViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.goods_comment_lay.*

/**
 * @author LDD
 * @Date   2018/3/30 下午2:56
 * @From   com.enation.javashop.android.component.goods.fragment
 * @Note   商品评论页面
 */
@Router(path = "/goods/comment")
class GoodsCommentFragment :BaseFragment<GoodsCommentPresenter,GoodsCommentLayBinding>(),GoodsCommentContract.View {

    var goodsId = 0

    var page = 1

    var state = ""

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
     * @Type  GoodsCommentItemAdapter
     * @Note  商品评论item适配器
     */
    private lateinit var adapter :GoodsCommentItemAdapter
    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午2:59
     * @Note   提供布局ID
     */
    override fun getLayId(): Int {
        return R.layout.goods_comment_lay
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:00
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:01
     * @Note   初始化操作
     */
    override fun init() {
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        /**初始化列表视图*/
        goods_comment_rv.layoutManager = virtualLayoutManager
        goods_comment_rv.adapter = delegateAdapter
        adapter = GoodsCommentItemAdapter(this.weak(), ArrayList())
        delegateAdapter.addAdapter(adapter)

        /**加载评论日期*/
        presenter.loadComment(goodsId,state,page)

    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:02
     * @Note   绑定事件
     */
    override fun bindEvent() {
        goods_comment_refresh.setOnLoadMoreListener {
            Handler().postDelayed({
                page += 1
                presenter.loadComment(goodsId,state,page)
                it.finishLoadMore()
            },1500)
        }
        arrayListOf(goods_comment_all_btn,goods_comment_good_btn,goods_comment_difference_btn,goods_comment_image_btn,goods_comment_secondary_btn).forEach {
            it.setOnClickListener {
                goods_comment_all_tv.setTextColor(Color.parseColor("#636363"))
                goods_comment_good_tv.setTextColor(Color.parseColor("#636363"))
                goods_comment_secondary_tv.setTextColor(Color.parseColor("#636363"))
                goods_comment_difference_tv.setTextColor(Color.parseColor("#636363"))
                goods_comment_image_tv.setTextColor(Color.parseColor("#636363"))
                when(it.tag.to<String>().toInt()){
                    1 ->{
                        state = ""
                        goods_comment_all_tv.setTextColor(Color.parseColor("#fa5055"))
                    }
                    2 ->{
                        state = "good"
                        goods_comment_good_tv.setTextColor(Color.parseColor("#fa5055"))
                    }
                    3 ->{
                        state = "neutral"
                        goods_comment_secondary_tv.setTextColor(Color.parseColor("#fa5055"))
                    }
                    4 ->{
                        state = "bad"
                        goods_comment_difference_tv.setTextColor(Color.parseColor("#fa5055"))
                    }
                    5 ->{
                        state = "image"
                        goods_comment_image_tv.setTextColor(Color.parseColor("#fa5055"))
                    }
                }
                page = 1
                presenter.loadComment(goodsId,state,page)
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:02
     * @Note   显示评论
     * @param  comments 评论数据
     */
    override fun showComment(comments: ArrayList<GoodsCommentViewModel>) {
        if (page == 1){
            adapter.data.clear()
        }
        adapter.data.addAll(comments)
        adapter.notifyDataSetChanged()
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:03
     * @Note   显示评论数量
     * @param  commentNum 评论数
     */
    override fun showCommentNum(commentNum: CommentNumViewModel) {
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:03
     * @Note   加载错误
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
        getUtils().dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:03
     * @Note   加载成功
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        getUtils().dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:04
     * @Note   开始加载
     */
    override fun start() {
        getUtils().showDialog()
    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:04
     * @Note   网络实施监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   GoodsCommentFragment
     * @Date   2018/3/30 下午3:05
     * @Note   销毁回调
     */
    override fun destory() {

    }
}