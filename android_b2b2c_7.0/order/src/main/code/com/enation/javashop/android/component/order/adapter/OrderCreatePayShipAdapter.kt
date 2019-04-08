package com.enation.javashop.android.component.order.adapter

import android.databinding.ObservableField
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.activity.OrderCreateActivity
import com.enation.javashop.android.component.order.activity.OrderCreatePayShipActivity
import com.enation.javashop.android.component.order.databinding.OrderCreatePayshipItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.VlayoutHelper
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.model.PayShipTimeViewModel
import com.enation.javashop.android.middleware.model.SingleIntViewModel
import com.enation.javashop.android.middleware.model.SingleStringViewModel

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单创建PayShip
 */
class OrderCreatePayShipAdapter(val data : PayShipTimeViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreatePayshipItemBinding>,PayShipTimeViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:17
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:18
     * @Note   item点击过滤
     * @param  position  坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:19
     * @Note   创建LayoutHelper
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreatePayshipItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_payship_item)
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:20
     * @Note   item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:22
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {

        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx())

    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipAdapter
     * @Date   2018/5/23 上午11:23
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreatePayshipItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = data
            binding.root.setOnClickListener {
                val act = binding.root.context as OrderCreateActivity
                act.push("/order/create/payship",_block = {
                    postcard ->
                    postcard.withObject(OrderCreatePayShipActivity.PARAMS_KEY_PAY_METHOD,SingleIntViewModel(ObservableField(GlobalState.asState(data.payName.get()))))
                    postcard.withObject(OrderCreatePayShipActivity.PARAMS_KEY_SHIP_TIME,SingleIntViewModel(ObservableField(GlobalState.asState(data.payShip.get()))))
                },requstCode = GlobalState.ORDER_CREATE_PAYSHIP)
            }
        }
    }
}