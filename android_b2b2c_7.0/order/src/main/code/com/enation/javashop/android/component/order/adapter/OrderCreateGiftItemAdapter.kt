package com.enation.javashop.android.component.order.adapter

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.AftersaleListItemLayBinding
import com.enation.javashop.android.component.order.databinding.OrderCreateGiftItemLayBinding
import com.enation.javashop.android.component.order.databinding.OrderListItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.AutoSizeTextView
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/17 上午11:02
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单Item适配器
 */
class OrderCreateGiftItemAdapter(var data : Gift) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateGiftItemLayBinding>,Gift>() {

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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateGiftItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_gift_item_lay)
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   提供Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(10.dpToPx(),1).then {
            self ->
            self.setMargin(0,  0,0, 0)
        }
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:07
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateGiftItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            loadImage(data.image,binding.image)
            binding.name.text = SpannableStringBuilder("【赠品】"+data.name).then {
                it.setSpan(ForegroundColorSpan(Color.parseColor("#e83437")),0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            binding.pirce.text = SpannableStringBuilder(String.format("￥%.2f",data.price)).then { self ->
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 10 * 0.6 * 0.6).toInt()) , 0 , 1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 10 * 0.6 * 0.6).toInt()) ,String.format("￥%.2f",data.price).length-3  , String.format("￥%.2f",data.price).length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 10 * 0.6 ).toInt()) , 1 , String.format("￥%.2f",data.price).length-3 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}