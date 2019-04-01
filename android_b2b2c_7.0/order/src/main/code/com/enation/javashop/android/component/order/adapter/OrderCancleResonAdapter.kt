package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCancleItemBinding
import com.enation.javashop.android.component.order.vo.OrderCancleResonViewModel
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单取消原因Item适配器
 */
class OrderCancleResonAdapter(val resons :ArrayList<OrderCancleResonViewModel> ,val selectCall :(Int) ->Unit) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCancleItemBinding>,OrderCancleResonViewModel>() {

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:41
     * @Note   数据适配器
     */
    override fun dataProvider(): Any {
        return resons
    }

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:41
     * @Note   item过滤
     * @param  position  item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:42
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCancleItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_cancle_item)
    }

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:42
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {

        return resons.size

    }

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:42
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(10.dpToPx(),resons.size).then {
            self ->
            self.setMargin(10.dpToPx(),15.dpToPx(),10.dpToPx(),30.dpToPx())
        }
    }

    /**
     * @author LDD
     * @From   OrderCancleResonAdapter
     * @Date   2018/4/24 下午4:42
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCancleItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
            binding.root.setOnClickListener {
                if (binding.data.selectObservable.get()){
                    selectCall.invoke(0)
                }else{
                    selectCall.invoke(1)
                }
                binding.data.selectObservable.set(!binding.data.selectObservable.get())
            }
        }
    }
}