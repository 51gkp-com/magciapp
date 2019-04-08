package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateHintItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.SingleStringViewModel

/**
 * @author LDD
 * @Date   2018/5/23 下午5:31
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   提醒
 */
class OrderCreateHintAdapter(val data :SingleStringViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateHintItemBinding>,SingleStringViewModel>() {

   /**
    * @author LDD
    * @From   OrderCreateHintAdapter
    * @Date   2018/5/23 下午5:31
    * @Note   数据提供
    */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderCreateHintAdapter
     * @Date   2018/5/23 下午5:32
     * @Note   Item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreateHintAdapter
     * @Date   2018/5/23 下午5:32
     * @Note   创建VM
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateHintItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_hint_item)
    }

    /**
     * @author LDD
     * @From   OrderCreateHintAdapter
     * @Date   2018/5/23 下午5:33
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreateHintAdapter
     * @Date   2018/5/23 下午5:35
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return ScrollFixLayoutHelper(ScrollFixLayoutHelper.BOTTOM_LEFT,0,0).then {
            self ->
            self.showType = ScrollFixLayoutHelper.SHOW_ON_LEAVE
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateHintAdapter
     * @Date   2018/5/23 下午5:35
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateHintItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = data
        }
    }
}