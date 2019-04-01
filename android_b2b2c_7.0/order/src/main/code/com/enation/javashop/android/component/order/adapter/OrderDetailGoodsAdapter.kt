package com.enation.javashop.android.component.order.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.OrderDetailAgreement
import com.enation.javashop.android.component.order.databinding.OrderDetailGoodsItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.OrderDetailGoodsViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单详细商品Item适配器
 */
class OrderDetailGoodsAdapter(val agreement: OrderDetailAgreement,val data :ArrayList<OrderDetailGoodsViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderDetailGoodsItemBinding>,OrderDetailGoodsViewModel>() {

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:47
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:48
     * @Note   item过滤器
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:49
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderDetailGoodsItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_detail_goods_item)
    }

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:49
     * @Note   item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:50
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(1,data.size).then {
            self ->
            self.setMargin(0,1,0,0)
        }
    }

    /**
     * @author LDD
     * @From   OrderDetailGoodsAdapter
     * @Date   2018/4/24 下午4:50
     * @Note   创建ViewHoler
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderDetailGoodsItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            loadImage(getItem(position).goodsImg,binding.goodsDetailGoodsIv)
            binding.orderDetailAddCart.setOnClickListener {
                agreement.addCart(getItem(position).skuId)
            }
            binding.orderDetailAfter.setOnClickListener {
                agreement.toAfterSale(getItem(position).skuId)
            }
            binding.orderDetailGoodsNameTv.text = getItem(position).goodsName
            binding.orderDetailGoodsSpecTv.text = getItem(position).sepc
            binding.orderDetailGoodsPriceTv.text = String.format("￥%.2f",getItem(position).goodsPrice)
            binding.orderDetailAfter.visibility = getItem(position).goodsActionModel.allowApplyService.judge(View.VISIBLE,View.GONE)
            binding.promotionTagParent.removeAllViews()
            for (promotionTag in getItem(position).promotionTags) {
                buildItem(binding.promotionTagParent,promotionTag)
            }
        }
    }

    private fun buildItem(parentView :ViewGroup , single : String){
        var tv = TextView(parentView.context)
        tv.gravity = Gravity.CENTER
        tv.setPadding(5.dpToPx(),0,5.dpToPx(),0)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTool.getScreenWidth(parentView.context)/40)
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