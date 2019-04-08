package com.enation.javashop.android.component.order.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.AftersaleListItemLayBinding
import com.enation.javashop.android.component.order.databinding.OrderCreateGoodsItemLayBinding
import com.enation.javashop.android.component.order.databinding.OrderListItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.AutoSizeTextView
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool
import com.umeng.commonsdk.statistics.internal.UMImprintChangeCallback

/**
 * @author LDD
 * @Date   2018/4/17 上午11:02
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单Item适配器
 */
class OrderCreateGoodsItemAdapter(var data :ArrayList<CartGoodsItemViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateGoodsItemLayBinding>,CartGoodsItemViewModel>() {

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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateGoodsItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_goods_item_lay)
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
            self.setMargin(0,  0,0, 0)
        }
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:07
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateGoodsItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.promotion.removeAllViews()
            val item = data[position]
            loadImage(item.imageUrl,binding.image)
            binding.name.text = item.name
            binding.num.text = "x${item.currentNum}"
            binding.price.text = SpannableStringBuilder(String.format("￥%.2f",item.price)).then { self ->
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 9 * 0.5 * 0.6).toInt()) , 0 , 1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 9 * 0.5 * 0.6).toInt()) ,String.format("￥%.2f",item.price).length-3  , String.format("￥%.2f",item.price).length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                self.setSpan(AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) / 9 * 0.5 ).toInt()) , 1 , String.format("￥%.2f",item.price).length-3 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            item.promotionList?.forEach {
                if (it.isCheck){
                    when(it.type){
                        "MINUS" -> {
                            buildItem(binding.promotion,"立减")
                        }
                        "GROUPBUY" -> {
                            buildItem(binding.promotion,"团购")
                        }
                        "EXCHANGE" -> {
                            buildItem(binding.promotion,"积分商城")
                        }
                        "HALF_PRICE" -> {
                            buildItem(binding.promotion,"第二件半价")
                        }
                        "SECKILL" -> {
                            buildItem(binding.promotion,"秒杀")
                        }
                    }
                }
            }
            if (item.promotionList != null && item.promotionList!!.isNotEmpty()){
                binding.promotion.visable()
            }else{
                binding.promotion.gone()
            }
        }
    }

    private fun buildItem(parentView :ViewGroup , single : String){
        var tv = TextView(parentView.context)
        tv.gravity = Gravity.CENTER
        tv.setPadding(5.dpToPx(),0,5.dpToPx(),0)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,ScreenTool.getScreenWidth(parentView.context)/30)
        tv.setBackgroundResource(R.drawable.javashop_red_radis_bg)
        tv.setTextColor(Color.WHITE)
        tv.text = single
        parentView.addView(tv)
        tv.reLayout<LinearLayout.LayoutParams> {
                it.setMargins(0,0,5.dpToPx(),0)
                it.width = LinearLayout.LayoutParams.WRAP_CONTENT
                it.height = LinearLayout.LayoutParams.MATCH_PARENT
        }

    }
}