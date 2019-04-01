package com.enation.javashop.android.component.order.adapter

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateAddressItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.utils.base.tool.BaseToolActivity
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单创建地址模块
 */
class OrderCreateAddressAdapter(var address:MemberAddressViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateAddressItemBinding>,MemberAddressViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:24
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return address
    }

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:24
     * @Note   点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:25
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateAddressItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_address_item)
    }

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:30
     * @Note   获取Item个数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:30
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx())
    }

    /**
     * @author LDD
     * @From   OrderCreateAddressAdapter
     * @Date   2018/5/23 上午10:34
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateAddressItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = address
            binding.root.setOnClickListener {
                (binding.root.context as AppCompatActivity).push("/member/address/list",{
                    it.withBoolean("order",true)
                },requstCode = GlobalState.ORDER_CREATE_ADDRESS)
            }
        }
    }
}