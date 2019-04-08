package com.enation.javashop.android.component.order.adapter

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateCouponItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.model.CouponViewModel

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单创建优惠券模块
 */
class OrderCreateCouponAdapter(val shopId:ArrayList<Int>,var data :ArrayList<CouponViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateCouponItemBinding>,CouponViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:36
     * @Note   提供数据
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:37
     * @Note   Item过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:37
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateCouponItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_coupon_item)
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:37
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:38
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx())
    }

    /**
     * @author LDD
     * @From   OrderCreateCouponAdapter
     * @Date   2018/5/23 上午10:38
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateCouponItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.orderCreateCouponHint.text = ""

            var amount = 0.00

            for (coupon in data) {
                if (coupon.isSelect.get() == 1){
                    amount += coupon.price
                }
            }

            if (amount == 0.00){
                binding.orderCreateCouponTv.text =  ""
            }else{
                binding.orderCreateCouponTv.text =  String.format("已减%.2f元",amount)
            }

            if (data.count() > 0){
                binding.orderCreateCouponHint.visable()
                binding.orderCreateCouponHint.text = "${data.count()}张可用优惠券"
            }else{
                binding.orderCreateCouponHint.invisable()
            }

            binding.root.setOnClickListener {
                (binding.root.context as AppCompatActivity).push("/order/create/coupon",{
                    var ids = ""
                    shopId.forEach { item ->
                        ids+="$item,"
                    }
                    it.withString("ids",ids.dropLast(1))
                },requstCode = GlobalState.ORDER_CREATE_COUPON)
            }
        }
    }
}