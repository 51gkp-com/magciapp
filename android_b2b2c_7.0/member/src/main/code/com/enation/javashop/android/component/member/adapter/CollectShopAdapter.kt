package com.enation.javashop.android.component.member.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberCollectShopItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.middleware.model.ShopViewModel

/**
 * @author LDD
 * @Date   2018/5/4 下午4:23
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   关注店铺适配器
 */
class CollectShopAdapter(val data :ArrayList<ShopViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<MemberCollectShopItemBinding>,ShopViewModel>() {

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:24
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:24
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }


    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午5:40
     * @Note   设置ViewType
     * @param  position item坐标
     */
    override fun getItemViewType(position: Int): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:25
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberCollectShopItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.member_collect_shop_item)
    }

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:25
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:25
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(1.dpToPx(),data.size)
    }

    /**
     * @author LDD
     * @From   CollectShopAdapter
     * @Date   2018/5/4 下午4:26
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberCollectShopItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
        }
    }
}