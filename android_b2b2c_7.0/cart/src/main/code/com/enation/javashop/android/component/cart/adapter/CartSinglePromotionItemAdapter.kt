package com.enation.javashop.android.component.cart.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.databinding.CartShopItemBinding
import com.enation.javashop.android.component.cart.databinding.CartSinglePromotionLayBinding
import com.enation.javashop.android.component.cart.util.BaseCartItemAdapter
import com.enation.javashop.android.component.cart.util.CartActionAgreement
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.middleware.model.CartShopItemViewModel
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.SingglePromotionViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/2/24 下午4:26
 * @From   com.enation.javashop.android.component.cart.adapter
 * @Note   购物车店铺Item适配器
 */
class CartSinglePromotionItemAdapter(val data :List<SingglePromotionViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<CartSinglePromotionLayBinding>, SingglePromotionViewModel>() {

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:26
     * @Note   数据提供者
     * @return 数据
     */
    override fun dataProvider(): Any {
        return data
    }

    override fun getItemViewType(position: Int): Int {
        return 10
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   常见viewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<CartSinglePromotionLayBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.cart_single_promotion_lay,parent,false))
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   获取数据源大小
     */
    override fun getItemCount(): Int {
        return data.count()
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   创建布局辅助器
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.count())
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:28
     * @Note   绑定操作
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CartSinglePromotionLayBinding>, position: Int) {
        holder.bind {
            binding ->
            var item = getItem(position)
            binding.tag.text = if (item.promotionId == 0){"其他操作"}else{"促销"}
            binding.content.text = item.title
        }
    }
}