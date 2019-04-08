package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderDetailShopItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.OrderDetailViewModel
import com.enation.javashop.utils.base.tool.ScreenTool
/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详细页面店铺Item适配器
 * */
class OrderDetailShopAdapter(val data :OrderDetailViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailShopItemBinding>,OrderDetailViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:52
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:53
     * @Note   Item过滤点击
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:54
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailShopItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_shop_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:55
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:56
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
     * @From   OrderDetailShopAdapter
     * @Date   2018/4/24 下午4:56
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailShopItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.orderDetailShopNameTv.text = data.sellerName
            binding.orderDetailShopIv.setImageResource((data.sellerId == 1 ).judge(R.drawable.javashop_cart_self_shop_icon,R.drawable.javashop_cart_shop_icon))
        }
    }
}