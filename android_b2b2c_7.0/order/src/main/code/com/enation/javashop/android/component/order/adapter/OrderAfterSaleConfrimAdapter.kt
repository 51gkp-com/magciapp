package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleConfrimItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.VlayoutHelper
import com.enation.javashop.android.middleware.model.AfterSaleViewModel
import com.enation.javashop.android.middleware.model.ReturnData

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后确定Item适配器
 */
class OrderAfterSaleConfrimAdapter(val agreement: AfterSaleAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSaleConfrimItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:14
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:14
     * @Note   item过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:15
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSaleConfrimItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_confrim_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:15
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:15
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleConfrimAdapter
     * @Date   2018/4/24 下午4:15
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSaleConfrimItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.orderAfterSaleConfrimBtn.setOnClickListener {
                agreement.comfrim()
            }
        }
    }
}