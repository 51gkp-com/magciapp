package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.adapter.PostCommentGoodsAdapter
import com.enation.javashop.android.component.member.adapter.PostCommentShopAdapter
import com.enation.javashop.android.component.member.databinding.MemberPostCommentLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.GalleryActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.OrderDetailDataChange
import com.enation.javashop.android.middleware.event.OrderListDataChange
import com.enation.javashop.android.middleware.logic.contract.member.PostCommentContract
import com.enation.javashop.android.middleware.logic.presenter.member.PostCommentPresenter
import com.enation.javashop.android.middleware.model.PostCommentViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.photoutils.model.TResult
import kotlinx.android.synthetic.main.member_post_comment_lay.*

/**
 * @author LDD
 * @Date   2018/4/26 下午3:02
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   评论编辑页
 */
@Router(path = "/member/comment/post")
class PostCommentActivity : GalleryActivity<PostCommentPresenter,MemberPostCommentLayBinding>() , PostCommentContract.View {

    @Autowired(name = "orderSn",required = true)
    @JvmField var orderSn :String = ""

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
     * @Name  photoListener
     * @Type  Block
     * @Note  图片选择回调
     */
    var photoListener :((String) -> Unit)? = null

    lateinit var postData : PostCommentViewModel

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:42
     * @Note   提供layoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_post_comment_lay
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:43
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:43
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化列表*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        post_comment_rv.layoutManager = virtualLayoutManager
        post_comment_rv.adapter = delegateAdapter
        presenter.loadCommentData(orderSn)
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:43
     * @Note   绑定事件
     */
    override fun bindEvent() {
        post_comment_topbar.setLeftClickListener {
            pop()
        }.setRightClickListener {
            presenter.commit(postData.getPostData())
        }
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:43
     * @Note   渲染评论视图
     */
    override fun renderCommentUi(data: PostCommentViewModel) {
        postData = data
        delegateAdapter.addAdapter(PostCommentGoodsAdapter(postData))
        delegateAdapter.addAdapter(PostCommentShopAdapter(postData))
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:45
     * @Note   请求错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:45
     * @Note   请求完成
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
        if (message == "评价成功"){
            getEventCenter().post(OrderListDataChange())
            getEventCenter().post(OrderDetailDataChange())
            pop()
        }
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:46
     * @Note   请求开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:46
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:46
     * @Note   销毁
     */
    override fun destory() {

    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:46
     * @Note   图片获取失败
     * @param  msg 错误信息
     */
    override fun takeFail(result: TResult?, msg: String?) {
        if (msg != null) {
            errorLog("ImageGetError",msg)
            showMessage(msg)
        }
    }

    /**
     * @author LDD
     * @From   PostCommentActivity
     * @Date   2018/5/2 下午4:47
     * @Note   图片获取成功
     * @param  result 返回体
     */
    override fun takeSuccess(result: TResult) {
        val filePath = result.image.compressPath
        presenter.uploader(filePath)
    }

    override fun imageUploadResult(url: String) {
        photoListener?.invoke(url)
    }
}