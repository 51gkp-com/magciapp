package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderDetailAddressItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.OrderDetailViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详情地址Item适配器
 */
class OrderDetailAddressAdapter(val data :OrderDetailViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailAddressItemBinding>,MemberAddressViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:43
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:43
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:43
     * @Note   创建ViewHolder
     * @param  ...
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailAddressItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_address_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:43
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:44
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
     * @From   OrderDetailAddressAdapter
     * @Date   2018/4/24 下午4:44
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailAddressItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.orderDetailNameTv.text = "${data.consigneeName}  ${data.consigneeMobile}"
            binding.orderDetailAddressTv.text = data.consigneeAddress
        }
    }
}