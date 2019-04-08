package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreatePriceItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.OrderCreatePriceViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单创建价格Item
 */
class OrderCreatePriceAdapter(var data :OrderCreatePriceViewModel ) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreatePriceItemBinding>,OrderCreatePriceViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:25
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:26
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:26
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreatePriceItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_price_item)
    }

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:27
     * @Note   item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:28
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx(),bottom = ScreenTool.getScreenWidth(BaseApplication.appContext).toInt()/8)
    }

    /**
     * @author LDD
     * @From   OrderCreatePriceAdapter
     * @Date   2018/5/23 上午11:28
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreatePriceItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = data
            if (data.pointPrice.get() == "+0积分"){
                binding.orderCreatePricePointHeader.gone()
            }else{
                binding.orderCreatePricePointHeader.visable()
            }
            if (data.discountPrice.get() == "-￥0.00"){
                binding.orderCreatePriceDiscountHeader.gone()
            }else{
                binding.orderCreatePriceDiscountHeader.visable()
            }
            if (data.cashPrice.get() == "-￥0.00"){
                binding.orderCreatePriceCashHeader.gone()
            }else{
                binding.orderCreatePriceCashHeader.visable()
            }
        }
    }

}