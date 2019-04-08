package com.enation.javashop.android.component.cart.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.databinding.CartPromotionItemBinding
import com.enation.javashop.android.component.cart.util.BaseCartItemAdapter
import com.enation.javashop.android.component.cart.util.CartActionAgreement
import com.enation.javashop.android.middleware.model.CartPromotionItemViewModel
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder

/**
 * @author LDD
 * @Date   2018/2/24 下午4:20
 * @From   com.enation.javashop.android.component.cart.adapter
 * @Note   购物车促销Item适配器
 */
class CartPromotionItemAdapter(agreement :CartActionAgreement,private val promotionItemViewModel: String) : BaseCartItemAdapter<BaseRecyclerViewHolder<CartPromotionItemBinding>, CartPromotionItemViewModel>(agreement) {

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:20
     * @Note   数据提供者
     * @return 数据
     */
    override fun dataProvider(): Any {
        return promotionItemViewModel
    }

    override fun getItemViewType(position: Int): Int {
        return 2
    }

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:21
     * @Note   点击时Item的过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:21
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<CartPromotionItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.cart_promotion_item,parent,false))
    }

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:22
     * @Note   获取Item大小
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:22
     * @Note   创布局辅助器
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   CartPromotionItemAdapter
     * @Date   2018/2/24 下午4:23
     * @Note   绑定ViewHolder
     * @param  ...
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CartPromotionItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.promotionItemData = promotionItemViewModel
        }
    }
}