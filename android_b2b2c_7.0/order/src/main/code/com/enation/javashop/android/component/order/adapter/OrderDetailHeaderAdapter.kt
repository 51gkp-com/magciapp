package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderDetailHeaderItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.OrderDetailViewModel

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详细头部Item适配器
 */
class OrderDetailHeaderAdapter(val data :OrderDetailViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailHeaderItemBinding>,OrderDetailViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:52
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:53
     * @Note   Item过滤点击
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:54
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailHeaderItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_header_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:55
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   OrderDetailHeaderAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailHeaderItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            if (data.orderStatus.contains("完成")){
                binding.orderDetailHeaderStateIv.setImageResource(R.drawable.javashop_icon_complete_white)
            }else{
                binding.orderDetailHeaderStateIv.setImageResource(R.drawable.javashop_icon_wait_rog_white)
            }
            binding.data = data
        }
    }
}