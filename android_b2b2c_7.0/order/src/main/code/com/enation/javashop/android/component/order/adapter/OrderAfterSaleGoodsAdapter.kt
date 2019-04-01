package com.enation.javashop.android.component.order.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSaleGoodsItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.VlayoutHelper
import com.enation.javashop.android.lib.utils.gone
import com.enation.javashop.android.lib.utils.visable
import com.enation.javashop.android.middleware.model.AfterSaleViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/4/23 上午11:38
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后商品适配器
 */
class OrderAfterSaleGoodsAdapter (val isOrder :Boolean,var data :AfterSaleViewModel,val agreement: AfterSaleAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSaleGoodsItemBinding>,GoodsItemViewModel>() {

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:23
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data.returnGoodsList
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:23
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:24
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSaleGoodsItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_goods_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:24
     * @Note   创建ItemCount
     */
    override fun getItemCount(): Int {
        return data.returnGoodsList.count()
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:25
     * @Note   创建Layout
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(size = data.returnGoodsList.size)
    }

    /**
     * @author LDD
     * @From   OrderAfterSaleGoodsAdapter
     * @Date   2018/4/24 下午4:26
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSaleGoodsItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            val itemData = getItem(position)
            loadImage(itemData.goodsImage,binding.orderAfterSaleGoodsItemIv)
            binding.orderAfterSaleGoodsItemNameTv.text = itemData.goodsName
            binding.orderAfterSaleGoodsItemInfoTv.text = String.format("价格:￥%.2f 数量x%d",itemData.goodsPrice,itemData.buyNum)
            if (data.returnGoodsList.count() == 1 && !isOrder){
                binding.addBtn.visable()
                binding.numTv.text= "${data.returnGoodsList[0].buyNum}"
                binding.addBtn.setOnClickListener {
                    if ((data.returnGoodsList[0].buyNum + 1) <= data.returnGoodsList[0].countNum){
                        data.returnGoodsList[0].buyNum += 1
                        binding.numTv.text= "${data.returnGoodsList[0].buyNum}"
                    }
                }
                binding.reduceBtn.setOnClickListener {
                    if (data.returnGoodsList[0].buyNum > 1){
                        data.returnGoodsList[0].buyNum -= 1
                        binding.numTv.text= "${data.returnGoodsList[0].buyNum}"
                    }
                }
            }else{
                binding.addBtn.gone()
            }
        }
    }
}