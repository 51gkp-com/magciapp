package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsInfoShopItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.ShopViewModel

/**
 * @author LDD
 * @Date   2018/4/8 下午1:44
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品所属店铺信息
 */
class GoodsInfoShopAdapter(var shop :ShopViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoShopItemBinding>,Int>() {
    
    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   Item过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoShopItemBinding> {

        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_shop_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoShopItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.shop = shop
        }
    }

}