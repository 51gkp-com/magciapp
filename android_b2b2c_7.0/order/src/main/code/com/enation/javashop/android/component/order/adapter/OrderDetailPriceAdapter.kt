package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderDetailPriceItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.gone
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.visable
import com.enation.javashop.android.middleware.model.OrderDetailViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详细价格Item适配器
 */
class OrderDetailPriceAdapter(val data :OrderDetailViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailPriceItemBinding>,OrderDetailViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:52
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:53
     * @Note   Item过滤点击
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:54
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailPriceItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_price_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:55
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0, (ScreenTool.getScreenWidth(BaseApplication.appContext)/25).toInt(),0,0)
        }
    }

    /**
     * @author LDD
     * @From   OrderDetailPriceAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailPriceItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.orderDetailPriceCountTv.text = String.format("+￥%.2f",data.goodsPrice)
            binding.orderDetailPriceShipTv.text = String.format("+￥%.2f",data.shipPrice)
            binding.orderDetailPricePay.text = String.format("￥%.2f",data.needPayPrice)
            if (data.discountPrice <= 0.0){
                binding.orderDetailPriceDiscountHeader.gone()
            }else{
                binding.orderDetailPriceDiscountHeader.visable()
                binding.orderDetailPriceDiscountTv.text = String.format("-￥%.2f",data.discountPrice)
            }
            if (data.pointPrice <= 0){
                binding.orderDetailPricePointHeader.gone()
            }else{
                binding.orderDetailPricePointHeader.visable()
                binding.orderDetailPricePointTv.text = String.format("+%d积分",data.pointPrice)
            }
            if (data.couponPrice <= 0.0){
                binding.orderDetailPriceCouponHeader.gone()
            }else{
                binding.orderDetailPriceCouponHeader.visable()
                binding.orderDetailPriceCouponTv.text = String.format("-￥%.2f",data.couponPrice)
            }
        }
    }
}