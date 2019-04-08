package com.enation.javashop.android.component.order.adapter


import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleDescribeItemBinding
import com.enation.javashop.android.component.order.vo.OrderDescribeViewModel
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.VlayoutHelper
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后描述适配器
 */
class OrderAfterSaleDescribeAdapter(var data :OrderDescribeViewModel, private val virtualLayoutManager: WeakReference<VirtualLayoutManager>,agreement: AfterSaleAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSaleDescribeItemBinding>,OrderDescribeViewModel>() {

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:18
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:19
     * @Note   item过滤
     * @param  position Item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:19
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSaleDescribeItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_describe_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:20
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:21
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleDescribeAdapter
     * @Date   2018/4/24 下午4:21
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSaleDescribeItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.text = data
            binding.orderAfterSaleDescribeContentTv.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus){
                    virtualLayoutManager.get()?.scrollToPosition(virtualLayoutManager.get()?.itemCount!!-1)
                }
            }
        }
    }
}