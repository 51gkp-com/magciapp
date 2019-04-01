package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsInfoAddressItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/8 下午1:08
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品详情地址适配器
 */
class GoodsInfoWeigetAdapter(var weight :Double) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoAddressItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:18
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:19
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:24
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoAddressItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_address_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:25
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:28
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0,0,0, ScreenTool.dip2px(BaseApplication.appContext,10f))
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoAddressAdapter
     * @Date   2018/4/8 下午1:29
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoAddressItemBinding>?, position: Int) {
        holder?.bind { binding ->
            binding.goodsInfoWeightContentTv.text = String.format("%.2fg",weight)
        }
    }
}