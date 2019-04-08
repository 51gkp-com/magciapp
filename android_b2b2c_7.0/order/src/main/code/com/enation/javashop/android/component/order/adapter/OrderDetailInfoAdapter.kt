package com.enation.javashop.android.component.order.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderDetailInfoItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.OrderDetailViewModel
import com.enation.javashop.utils.base.tool.ScreenTool


/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详细信息Item适配器
 */
class OrderDetailInfoAdapter(val data :OrderDetailViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailInfoItemBinding>,OrderDetailViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:52
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:53
     * @Note   Item过滤点击
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:54
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailInfoItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_info_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:55
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0, (ScreenTool.getScreenWidth(BaseApplication.appContext)/20).toInt(),0,0)
        }
    }

    /**
     * @author LDD
     * @From   OrderDetailInfoAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailInfoItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.copy.setOnClickListener {
                val cm = binding.root.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("JavaShop",data.orderSn)
                cm.primaryClip = clip
                showMessage("复制订单号成功")
            }
            if (data.payTime.isEmpty()){
                binding.orderDetailInfoLineB.visibility = View.GONE
                binding.orderDetailPayHeader.visibility = View.GONE
                binding.orderDetailPayTimeHeader.visibility = View.GONE
            }else{
                binding.orderDetailInfoLineB.visibility = View.VISIBLE
                binding.orderDetailPayHeader.visibility = View.VISIBLE
                binding.orderDetailPayTimeHeader.visibility = View.VISIBLE
                binding.orderDetailPayTv.text = data.getPayName()
                binding.orderDetailPayTimeTv.text = data.payTime
            }
            binding.orderDetailSnTv.text = data.orderSn
            binding.orderDetailCreateTimeTv.text = data.createTime
            if(data.logiName.isEmpty()){
                binding.orderDetailExpressHeader.visibility = View.GONE
                binding.orderDetailInfoLineC.visibility = View.GONE
            }else{
                binding.orderDetailExpressHeader.visibility = View.VISIBLE
                binding.orderDetailInfoLineC.visibility = View.VISIBLE
                binding.orderDetailExpressTv.text = data.logiName
            }
            if(data.receiptViewModel == null || data.receiptViewModel!!.receipt_content.isEmpty()){
                binding.orderDetailReceiptTypeTv.text = "不开发票"
                binding.orderDetailReceiptTitleHeader.visibility = View.GONE
                binding.orderDetailReceiptContentHeader.visibility = View.GONE
                binding.orderDetailReceiptDutyHeader.visibility = View.GONE
            }else{
                binding.orderDetailReceiptTitleHeader.visibility = View.VISIBLE
                binding.orderDetailReceiptContentHeader.visibility = View.VISIBLE
                binding.orderDetailReceiptDutyHeader.visibility = View.VISIBLE
                binding.orderDetailReceiptTypeTv.text = data.receiptViewModel!!.receipt_type
                binding.orderDetailReceiptTitleTv.text = data.receiptViewModel!!.receipt_title.get()
                binding.orderDetailReceiptContentTv.text = data.receiptViewModel!!.receipt_content
                binding.orderDetailReceiptDutyTv.text = data.receiptViewModel!!.duty_invoice.get()
            }
        }
    }
}