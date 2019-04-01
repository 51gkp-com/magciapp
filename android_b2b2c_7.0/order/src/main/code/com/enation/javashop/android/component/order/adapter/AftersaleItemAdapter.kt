package com.enation.javashop.android.component.order.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.AftersaleListItemLayBinding
import com.enation.javashop.android.component.order.databinding.OrderListItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.AutoSizeTextView
import com.enation.javashop.android.middleware.model.AftersaleListModel
import com.enation.javashop.android.middleware.model.OrderItemGoodsViewModel
import com.enation.javashop.android.middleware.model.OrderItemViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/17 上午11:02
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单Item适配器
 */
class AftersaleItemAdapter(var data :ArrayList<AftersaleListModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<AftersaleListItemLayBinding>,AftersaleListModel>() {

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:05
     * @Note   item事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<AftersaleListItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.aftersale_list_item_lay)
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   提供Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(10.dpToPx(),data.count()).then {
            self ->
            self.setMargin(0,  10.dpToPx(),0, 10.dpToPx())
        }
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:07
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<AftersaleListItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            val item = getItem(position)
            binding.afterSaleItemPriceTv.text = String.format("￥%.2f+%d积分",item.money,(item.point == -1).judge(0,item.point))
            binding.afterSaleItemSnTv.text = "售后编号：${item.sn}"
            binding.afterSaleItemStateTv.text = "售后进度：${item.type}"
            binding.afterSaleItemTypeTv.text = item.state
        }
    }
}