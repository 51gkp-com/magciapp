package com.enation.javashop.android.component.shop.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopHomeItemHeaderLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then

/**
 * @author LDD
 * @Date   2018/4/10 上午10:44
 * @From   com.enation.javashop.android.component.shop.adapter
 * @Note   店铺首页item标题适配器
 */
class ShopHomeHeaderAdapter(val message :String) :BaseDelegateAdapter<BaseRecyclerViewHolder<ShopHomeItemHeaderLayBinding>,String>() {

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:01
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return message
    }

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:01
     * @Note   item事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:02
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ShopHomeItemHeaderLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.shop_home_item_header_lay)
    }

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:02
     * @Note   Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:02
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0,10,0,0)
        }
    }

    /**
     * @author LDD
     * @From   ShopHomeHeaderAdapter
     * @Date   2018/4/24 下午3:02
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ShopHomeItemHeaderLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.shopHomeItemHeaderTv.text = message
            when (message){
                "新品上架" ->{
                    binding.shopHomeItemHeaderIv.setImageResource(R.drawable.javashop_icon_new_yellow)
                }
                "店家推荐" ->{
                    binding.shopHomeItemHeaderIv.setImageResource(R.drawable.javashop_icon_recommend_yellow)
                }
                "店铺热卖" ->{
                    binding.shopHomeItemHeaderIv.setImageResource(R.drawable.javashop_icon_hot_yellow)
                }
            }

        }
    }
}