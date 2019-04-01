package com.enation.javashop.android.component.cart.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.databinding.CartShopItemBinding
import com.enation.javashop.android.component.cart.util.BaseCartItemAdapter
import com.enation.javashop.android.component.cart.util.CartActionAgreement
import com.enation.javashop.android.middleware.model.CartShopItemViewModel
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder

/**
 * @author LDD
 * @Date   2018/2/24 下午4:26
 * @From   com.enation.javashop.android.component.cart.adapter
 * @Note   购物车店铺Item适配器
 */
class CartShopItemAdapter(agreement: CartActionAgreement,private val shopItemViewModel: CartShopItemViewModel) : BaseCartItemAdapter<BaseRecyclerViewHolder<CartShopItemBinding>, CartShopItemViewModel>(agreement) {

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:26
     * @Note   数据提供者
     * @return 数据
     */
    override fun dataProvider(): Any {
        return shopItemViewModel
    }

    override fun getItemViewType(position: Int): Int {
        return 3
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<CartShopItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.cart_shop_item,parent,false))
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   获取数据源大小
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:27
     * @Note   创建布局辅助器
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   CartShopItemAdapter
     * @Date   2018/2/24 下午4:28
     * @Note   绑定操作
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CartShopItemBinding>, position: Int) {
        holder.bind {
            binding ->
            binding.shopItemData = shopItemViewModel
            shopCheck(binding.cartShopItemCheck , shopItemViewModel.shopId , !shopItemViewModel.isCheck)
            toShop(binding.root,shopItemViewModel.shopId)
            showShopBonus(binding.cartShopItemRightTv,shopItemViewModel.shopId)
        }
    }

    /**
     * @Name  shopCheck
     * @Type  Block
     * @Note  店铺选中
     */
    private val shopCheck = { view : CheckBox, shopId :Int, checked :Boolean ->
        view.setOnClickListener({
            actionProvider().shopCheck(shopId,checked)
        })
    }

    /**
     * @Name  toShop
     * @Type  Block
     * @Note  去店铺
     */
    private val toShop = {view :View ,shopId :Int ->
        view.setOnClickListener {
            actionProvider().toShop(shopId)
        }
    }

    /**
     * @Name  showShopBonus
     * @Type  Block
     * @Note  显示店铺可领取的优惠券
     */
    private val showShopBonus = { view : View , shopId : Int ->
        view.setOnClickListener {
            actionProvider().showShopBonus(shopId)
        }
    }
}