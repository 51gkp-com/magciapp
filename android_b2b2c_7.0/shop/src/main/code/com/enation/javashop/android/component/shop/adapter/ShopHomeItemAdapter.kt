package com.enation.javashop.android.component.shop.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopHomeItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/4/10 上午11:59
 * @From   com.enation.javashop.android.component.shop.adapter
 * @Note   商品主页Item适配器
 */
class ShopHomeItemAdapter(val data :ArrayList<GoodsItemViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<ShopHomeItemLayBinding> ,GoodsItemViewModel>() {

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:03
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:03
     * @Note   item事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ShopHomeItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.shop_home_item_lay)
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return  GridLayoutHelper(2).then {
            self ->
            /**取消自动填充*/
            self.setAutoExpand(false)

            /**设置左右间距*/
            self.hGap = 10

            /**设置上下间距*/
            self.vGap = 10

            /**设置Margin*/
            self.setMargin(0,10,0,0)
        }
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ShopHomeItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
        }
    }
}