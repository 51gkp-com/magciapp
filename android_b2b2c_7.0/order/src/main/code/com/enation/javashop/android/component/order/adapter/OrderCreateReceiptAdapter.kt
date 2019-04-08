package com.enation.javashop.android.component.order.adapter

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.activity.OrderCreateReceiptActivity
import com.enation.javashop.android.component.order.databinding.OrderCreateReceiptItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.model.ReceiptViewModel

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单创建发票
 */
class OrderCreateReceiptAdapter(var data :ReceiptViewModel?) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateReceiptItemBinding>,ReceiptViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:30
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return ""
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:33
     * @Note   item过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:33
     * @Note   创建VM
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateReceiptItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_receipt_item)
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:35
     * @Note   item个数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:36
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx())
    }

    /**
     * @author LDD
     * @From   OrderCreateReceiptAdapter
     * @Date   2018/5/23 上午11:37
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateReceiptItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (data == null){
                binding.orderCreateReceiptTv.text = "不开发票"
            }else{
                binding.orderCreateReceiptTv.text = "${data!!.receipt_content}-${if (data!!.receipt_type == "个人") "个人" else data!!.receipt_title.get()}"
                if (binding.orderCreateReceiptTv.text == "-"){
                    binding.orderCreateReceiptTv.text = "不开发票"
                }
            }
            binding.root.setOnClickListener {
                (binding.root.context as AppCompatActivity).push("/order/create/receipt",_block = {
                    postcard ->
                    postcard.withObject(OrderCreateReceiptActivity.PARAMS_KEY_RECEIPT,data)
                },requstCode = GlobalState.ORDER_CREATE_RECEIPT)
            }
        }
    }
}