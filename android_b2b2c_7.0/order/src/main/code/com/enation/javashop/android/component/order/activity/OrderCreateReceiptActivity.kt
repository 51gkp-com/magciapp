package com.enation.javashop.android.component.order.activity

import android.databinding.ObservableField
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateReceiptLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateReceiptContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderCreateReceiptPresenter
import com.enation.javashop.android.middleware.model.ReceiptContentViewModel
import com.enation.javashop.android.middleware.model.ReceiptViewModel
import com.enation.javashop.android.middleware.model.SingleIntViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.order_create_receipt_lay.*

/**
 * @author LDD
 * @Date   2018/5/23 下午6:56
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单创建发票页面
 */
@Router(path = "/order/create/receipt")
class OrderCreateReceiptActivity : BaseActivity<OrderCreateReceiptPresenter, OrderCreateReceiptLayBinding>(), OrderCreateReceiptContract.View  {

    /**
     * @author LDD
     * @Date   2018/5/29 上午10:34
     * @From   OrderCreateReceiptActivity
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.component.order.activity.OrderCreateReceiptActivity.Companion.PARAMS_KEY_RECEIPT
         * @Type  String
         * @Note  发票KEY
         */
        const val PARAMS_KEY_RECEIPT ="PARAMS_KEY_RECEIPT"

    }

    /**
     * @Name  payData
     * @Type  ReceiptViewModel
     * @Note  发票VM
     */
    @Autowired(name = PARAMS_KEY_RECEIPT,required = true)
    @JvmField var receipt = ReceiptViewModel(1,"".bindingParams(),"","".bindingParams(),"")

    /**
     * @Name  contentViews
     * @Type  ArrayList<TextView>
     * @Note  发票内容子视图
     */
    private val contentViews  = ArrayList<TextView>()

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:31
     * @Note   初始化
     */
    override fun getLayId(): Int {
        return R.layout.order_create_receipt_lay
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:32
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
        JRouter.prepare().inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:32
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        if (receipt.receipt_content == ""){
            receiptConfig(order_create_receipt_none)
        }else{
            receiptConfig(order_create_receipt_paper)
            if (receipt.receipt_type == "person"){
                receiptTypeConfig(order_create_receipt_title_person_ck)
            }else{
                receiptTypeConfig(order_create_receipt_title_company_ck)
            }
        }
        mViewBinding.data = receipt
        presenter.loadReceiptContent()
        presenter.loadReceiptList()
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:33
     * @Note   绑定事件
     */
    override fun bindEvent() {
        order_create_receipt_topbar.setLeftClickListener {
            pop()
        }
        order_create_receit_confrim.setOnClickListener {
            if (receipt.receipt_type == "country" && (receipt.receipt_title.get() == "" || receipt.duty_invoice.get() == "")){
                showMessage("请完善公司发票信息！")
                return@setOnClickListener
            }
            presenter.setReceipt(receipt)
        }
        arrayOf(order_create_receipt_none,order_create_receipt_paper).forEach {
            it.setOnClickListener {
                receiptConfig(it)
                if (it != order_create_receipt_none){
                    receiptTypeConfig(order_create_receipt_title_person_ck)
                }
            }
        }
        arrayOf(order_create_receipt_title_person_ck,order_create_receipt_title_company_ck,order_create_receipt_title_person_tv,order_create_receipt_title_company_tv).forEach {
            item ->
            item.setOnClickListener {
                receiptTypeConfig(it)
            }
        }
        order_create_receipt_title_company_name_clear.setOnClickListener {
            receipt.receipt_title.set("")
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/30 上午10:07
     * @Note   发票类型设置
     */
    private fun receiptTypeConfig(view : View){
        if (view == order_create_receipt_title_person_ck || view == order_create_receipt_title_person_tv){
            order_create_receipt_title_company_name_et.visibility = View.GONE
            order_create_receipt_title_company_name_clear.visibility = View.GONE
            order_create_receipt_title_company_num_et.visibility = View.GONE
            receipt.duty_invoice.set("")
            receipt.receipt_title.set("")
            receipt.receipt_type = "person"
            order_create_receipt_title_person_ck.isChecked = true
            order_create_receipt_title_company_ck.isChecked = false
        }else{
            receipt.receipt_type = "country"
            order_create_receipt_title_company_name_et.visibility = View.VISIBLE
            order_create_receipt_title_company_name_clear.visibility = View.VISIBLE
            order_create_receipt_title_company_num_et.visibility = View.VISIBLE
            order_create_receipt_title_person_ck.isChecked = false
            order_create_receipt_title_company_ck.isChecked = true
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/30 上午10:05
     * @Note   开发票设置
     * @param  tv View
     */
    private fun receiptConfig(view :View){
        if (view == order_create_receipt_none){
            receipt = ReceiptViewModel(1,"".bindingParams(),"","".bindingParams(),"")
            mViewBinding.data = receipt
            hideView(true)
            order_create_receipt_none.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            order_create_receipt_none.setTextColor(Color.parseColor("#f83039"))
            order_create_receipt_paper.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
            order_create_receipt_paper.setTextColor(Color.parseColor("#cccccc"))
        }else{
            hideView(false)
            order_create_receipt_paper.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            order_create_receipt_paper.setTextColor(Color.parseColor("#f83039"))
            order_create_receipt_none.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
            order_create_receipt_none.setTextColor(Color.parseColor("#cccccc"))
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/30 上午11:37
     * @Note   隐藏显示
     * @param  hide 是否隐藏
     */
    fun hideView(hide :Boolean){
        var isVisable = View.VISIBLE
        if (hide){
            isVisable = View.GONE
        }
        order_create_receipt_bg_b.visibility = isVisable
        order_create_receipt_bg_c.visibility = isVisable
        order_create_receipt_title_company_tv.visibility = isVisable
        order_create_receipt_title_person_tv.visibility = isVisable
        order_create_receipt_title_holder.visibility = isVisable
        order_create_receipt_title.visibility = isVisable
        order_create_receipt_title_company_ck.visibility = isVisable
        order_create_receipt_title_person_ck.visibility = isVisable
        if (receipt.receipt_type == "person" && isVisable == View.VISIBLE){
            order_create_receipt_title_company_name_et.visibility = View.GONE
            order_create_receipt_title_company_num_et.visibility = View.GONE
            order_create_receipt_title_company_name_clear.visibility = View.GONE
        }else{
            order_create_receipt_title_company_name_et.visibility = isVisable
            order_create_receipt_title_company_num_et.visibility = isVisable
            order_create_receipt_title_company_name_clear.visibility = isVisable
        }
        order_create_receipt_content.visibility = isVisable
        order_create_receipt_content_holder.visibility = isVisable
        order_create_receipt_content_ll.visibility = isVisable
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:33
     * @Note   渲染内容类表
     * @param  data 数据
     */
    override fun renderReceiptContent(data: ArrayList<ReceiptContentViewModel>) {
        data.forEach {
            val tv = TextView(this).then {
                self ->
                if (receipt.receipt_content == it.name){
                    receipt.receipt_content = it.name
                    self.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                    self.setTextColor(Color.parseColor("#f83039"))
                }else{
                    self.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                    self.setTextColor(Color.parseColor("#cccccc"))
                }
                self.textSize = 13F
                self.gravity = Gravity.CENTER
                self.text = it.name
                self.setPadding(4.dpToPx(),4.dpToPx(),4.dpToPx(),4.dpToPx())
            }
            tv.setOnClickListener {
                receipt.receipt_content = tv.text.toString()
                for (contentView in contentViews) {
                    if (contentView == tv){
                        contentView.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                        contentView.setTextColor(Color.parseColor("#f83039"))
                    }else{
                        contentView.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                        contentView.setTextColor(Color.parseColor("#cccccc"))
                    }
                }
            }
            order_create_receipt_content_ll.addView(tv)
            contentViews.add(tv)
            tv.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT
                params.setMargins(0,0,10.dpToPx(),0)
            }
        }
        if (receipt.receipt_content == ""){
            resetReceiptContent()
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/30 上午10:34
     * @Note   初始化发票
     */
    private fun resetReceiptContent(){
        receipt.receipt_content = contentViews[0].text.toString()
        contentViews[0].setBackgroundResource(R.drawable.javashop_fillet_red_bg)
        contentViews[0].setTextColor(Color.parseColor("#f83039"))
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:34
     * @Note   渲染发票历史记录
     * @param  data 发票列表
     */
    override fun renderReceiptList(data: ArrayList<ReceiptViewModel>) {

    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:10
     * @Note   错误回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:10
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        setResult(GlobalState.ORDER_CREATE_RECEIPT,intent)
        pop()
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:10
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:10
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptActivity
     * @Date   2018/5/23 下午7:11
     * @Note   销毁回调
     */
    override fun destory() {

    }
}