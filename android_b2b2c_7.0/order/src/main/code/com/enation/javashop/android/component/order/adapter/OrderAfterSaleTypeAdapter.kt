package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleTypeItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.VlayoutHelper
import com.enation.javashop.android.lib.utils.getColorCompatible

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后类别Item适配器
 */
class OrderAfterSaleTypeAdapter(var type :Int,val agreement: AfterSaleAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSaleTypeItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:36
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }
    
    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:38
     * @Note   item过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:38
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSaleTypeItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_type_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:39
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:40
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 0)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleTypeAdapter
     * @Date   2018/4/24 下午4:40
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSaleTypeItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.orderAfterSaleTypeMoney.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_navy))
            binding.orderAfterSaleTypeMoney.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
            binding.orderAfterSaleTypeGoods.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_navy))
            binding.orderAfterSaleTypeGoods.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
            if (type == 1){
                binding.orderAfterSaleTypeMoney.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_pay_red))
                binding.orderAfterSaleTypeMoney.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            }else{
                binding.orderAfterSaleTypeGoods.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_pay_red))
                binding.orderAfterSaleTypeGoods.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            }
            binding.orderAfterSaleTypeMoney.setOnClickListener {
                agreement.serviceSelect(1)
            }
            binding.orderAfterSaleTypeGoods.setOnClickListener {
                agreement.serviceSelect(0)
            }
        }
    }
}