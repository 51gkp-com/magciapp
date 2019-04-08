package com.enation.javashop.android.component.promotion.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.PromotionGroupbuyItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/5/22 下午2:11
 * @From   com.enation.javashop.android.component.promotion.adapter
 * @Note   团购商品适配器
 */
class PromotionGroupBuyGoodsAdapter(val data :ArrayList<GoodsItemViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<PromotionGroupbuyItemBinding>,GoodsItemViewModel>() {

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:21
     * @Note   地址获取
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:23
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:24
     * @Note   创建VM
=     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<PromotionGroupbuyItemBinding> {

        return BaseRecyclerViewHolder.build(parent, R.layout.promotion_groupbuy_item)

    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:24
     * @Note   item个数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:25
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.size)
    }

    /**
     * @author LDD
     * @From   PromotionGroupBuyGoodsAdapter
     * @Date   2018/5/22 下午2:26
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<PromotionGroupbuyItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
            binding.promotionGroupbuyItemSpv.setTotalAndCurrentCount(getItem(position).countNum,getItem(position).buyNum)
        }
    }
}