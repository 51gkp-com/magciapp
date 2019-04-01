package com.enation.javashop.android.component.order.adapter

import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.activity.OrderAfterSaleActivity
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleResonItemBinding
import com.enation.javashop.android.component.order.vo.OrderCancleResonViewModel
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.TextViewDelegateAdapter
import com.enation.javashop.android.lib.adapter.TextViewListDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopCommonView
import kotlinx.android.synthetic.main.order_after_sale_lay.*

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后订单原因
 */
class OrderAfterSaleResonAdapter(var reson :String,val agreement: AfterSaleAgreement) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSaleResonItemBinding>,String>() {

    private val resons = arrayListOf("缺货少件",
            "不想要了",
            "商品降价",
            "发错货",
            "质量问题哦",
            "商品与页面描述不符",
            "商品损坏",
            "商品质量问题",
            "物流问题",
            "商品严重损坏",
            "协商退货")

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:33
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return reson
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:34
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:35
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSaleResonItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_reson_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:35
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:35
     * @Note   创建ViewHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleResonAdapter
     * @Date   2018/4/24 下午4:35
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSaleResonItemBinding>?, position: Int) { 
        holder?.bind { 
            binding ->
            binding.orderAfterSaleResonContent.text = reson
            binding.root.setOnClickListener {
                PopCommonView.build(binding.root.context as OrderAfterSaleActivity)
                        .setTitle("请选择退款原因")
                        .confirmButtonVisable(false)
                        .setAdapters(TextViewListDelegateAdapter(resons,22,Color.BLACK,10.dpToPx(),0,10.dpToPx(),10.dpToPx()).then {
                            it.setOnItemClickListener { data, position ->
                                binding.orderAfterSaleResonContent.text = data
                                agreement.reson(data)
                            }
                        }).show((binding.root.context as OrderAfterSaleActivity).order_after_sale_topbar.holderViewProvider())
            }
        }
    }
}