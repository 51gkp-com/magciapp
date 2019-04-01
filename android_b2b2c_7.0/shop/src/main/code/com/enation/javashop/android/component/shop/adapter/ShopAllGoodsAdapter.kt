package com.enation.javashop.android.component.shop.adapter

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopAllGridItemLayBinding
import com.enation.javashop.android.component.shop.databinding.ShopAllListItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/4/10 下午3:16
 * @From   com.enation.javashop.android.component.shop.adapter
 * @Note   店铺全部商品页面
 */
class ShopAllGoodsAdapter(val data :ArrayList<GoodsItemViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<ViewDataBinding>,GoodsItemViewModel>() {

    /**
     * @Name  isList
     * @Type  Boolean
     * @Note  标识列表状态是list还是grid
     */
    private var isList  = true

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:55
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:55
     * @Note   Item事件过滤
     * @param  position  item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/3/21 下午2:54
     * @Note   设置ViewType
     */
    override fun getItemViewType(position: Int): Int {
        return if (isList) 0 else 1
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:56
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ViewDataBinding> {
        if (isList){
            return BaseRecyclerViewHolder.build(parent, R.layout.shop_all_list_item_lay)
        }else{
            return BaseRecyclerViewHolder.build(parent, R.layout.shop_all_grid_item_lay)
        }
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:56
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:57
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return if (isList){
            LinearLayoutHelper(0,1)
        }else{
            GridLayoutHelper(2).then {
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
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/4/24 下午2:58
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ViewDataBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (binding is ShopAllListItemLayBinding){
                binding.data = getItem(position)
            }else if(binding is ShopAllGridItemLayBinding){
                binding.data = getItem(position)
            }
        }
    }

    /**
     * @author LDD
     * @From   ShopAllGoodsAdapter
     * @Date   2018/3/21 下午2:55
     * @Note   item样式转换
     */
    fun itemTransform(){
        isList = !isList
    }
}