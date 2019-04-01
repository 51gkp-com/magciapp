package com.enation.javashop.android.component.shop.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopCategoryItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.ShopCategoryViewModel

/**
 * @author LDD
 * @Date   2018/4/12 上午10:37
 * @From   com.enation.javashop.android.component.shop.adapter
 * @Note   店铺分类适配器
 */
class ShopCategoryAdapter(val list: ArrayList<ShopCategoryViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<ShopCategoryItemLayBinding>,ShopCategoryViewModel>() {

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:58
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return list
    }

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:58
     * @Note   Item过滤
     * @param  position Item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:59
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ShopCategoryItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.shop_category_item_lay)
    }

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:59
     * @Note   Item总数
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:59
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,list.size)
    }

    /**
     * @author LDD
     * @From   ShopCategoryAdapter
     * @Date   2018/4/24 下午2:59
     * @Note   绑定ViewHolder
     * @param  holder 绑定ViewHolder数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ShopCategoryItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->  
            binding.data = getItem(position)
        }
    }
}