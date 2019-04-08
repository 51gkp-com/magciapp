package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterBrandItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.middleware.model.GoodsFilterValue

/**
 * @author LDD
 * @Date   2018/3/23 上午10:42
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   品牌Item适配器
 */
class GoodsSearchFilterBrandAdapter(val activity: FragmentActivity ,var data :ArrayList<GoodsFilterValue>) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsSearchFilterBrandItemBinding>,GoodsFilterValue>() {


    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:42
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:42
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:42
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsSearchFilterBrandItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_search_filter_brand_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:43
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:43
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.size)
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterBrandAdapter
     * @Date   2018/3/23 上午10:43
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsSearchFilterBrandItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            var item = getItem(position)
            if (position == 0 || (item.letter != null && item.letter != getItem(position-1).letter)){
                binding.goodsSearchFilterBrandWorldLay.visibility = View.VISIBLE
                binding.goodsSearchFilterBrandWorldTv.text = item.letter
            }else{
                binding.goodsSearchFilterBrandWorldLay.visibility = View.GONE
            }
            binding.data = getItem(position)
            binding.root.setOnClickListener {
                data.forEach {
                    if (it == item){
                        it.selectedObservable.set(true)
                    }else{
                        it.selectedObservable.set(false)
                    }
                }
            }
        }
    }
}