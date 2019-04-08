package com.enation.javashop.android.component.order.activity

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.AftersaleDetailActLayBinding
import com.enation.javashop.android.component.order.databinding.AftersaleTextItemBinding
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleGoodsItemBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.order.AftersaleDetailContract
import com.enation.javashop.android.middleware.logic.presenter.order.AftersaleDetailPresenter
import com.enation.javashop.android.middleware.model.AftersaleDetailModel
import com.enation.javashop.android.middleware.model.OrderDetailGoodsViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.aftersale_detail_act_lay.*

@Router(path = "/aftersale/detail")
class AftersaleDetailActivity :BaseActivity<AftersaleDetailPresenter,AftersaleDetailActLayBinding>(),AftersaleDetailContract.View{

    @Autowired(name = "sn",required = true)
    @JvmField var sn :String = ""

    override fun getLayId(): Int {
        return R.layout.aftersale_detail_act_lay
    }

    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.load(sn)
    }

    override fun bindEvent() {
        after_sale_detail_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun destory() {

    }

    override fun render(detail: AftersaleDetailModel) {
        mViewBinding.contentView.removeAllViews()
        buildText("订单编号",detail.sn,activity)
        buildText("申请时间",detail.time,activity)
        buildText("售后类型",detail.type,activity)
        buildText("售后状态",detail.state,activity)
        buildText("售后原因",detail.reson,activity)
        buildText("详细描述",detail.des,activity)
        buildText("退款方式",detail.refundWay,activity)
        buildText("退款金额",String.format("￥%.2f+%d积分",detail.price,(detail.point == -1).judge(0,detail.point)),activity)
        detail.goods.forEachIndexed { index, goods ->
            buildGoods(goods,activity,index == 0)
        }
    }

    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    override fun start() {
        showDialog()
    }

    override fun networkMonitor(state: NetState) {

    }

    fun buildText(title :String,content :String ,context: Context) {
        val childBinding :AftersaleTextItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aftersale_text_item, mViewBinding.contentView, false)
        childBinding.text.text = "$title : $content"
        childBinding.root.setBackgroundColor(Color.WHITE)
        mViewBinding.contentView.addView(childBinding.root)
    }

    fun buildGoods(goods :OrderDetailGoodsViewModel ,context: Context , isFirst :Boolean){

        val goodsBinding :OrderAfterSaleGoodsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_after_sale_goods_item, mViewBinding.contentView, false)

        goodsBinding.orderAfterSaleGoodsItemNameTv.text = goods.goodsName
        goodsBinding.orderAfterSaleGoodsItemInfoTv.text = goods.sepc
        GlideUtils.setImage(context,goods.goodsImg,goodsBinding.orderAfterSaleGoodsItemIv)
        goodsBinding.addBtn.gone()
        if (isFirst){
            (goodsBinding.root.layoutParams as LinearLayout.LayoutParams).setMargins(0,10.dpToPx(),0,0)
        }

        mViewBinding.contentView.addView(goodsBinding.root)
    }

}