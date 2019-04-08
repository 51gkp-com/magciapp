package com.enation.javashop.android.component.promotion.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.PromotionSeckillItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.utils.base.tool.BaseToolActivity

/**
 * @author LDD
 * @Date   2018/5/21 下午3:06
 * @From   com.enation.javashop.android.component.promotion.adapter
 * @Note   秒杀Item适配器
 */
class PromotionSecKillGoodsAdapter(val data :ArrayList<GoodsItemViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<PromotionSeckillItemBinding>,GoodsItemViewModel>() {

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:26
     * @Note   获取数据
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:26
     * @Note   点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:27
     * @Note   创建VM
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<PromotionSeckillItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.promotion_seckill_item)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:27
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:27
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.size)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillGoodsAdapter
     * @Date   2018/5/21 下午3:27
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<PromotionSeckillItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
            binding.promotionSeckillItemBuyTv.isSelected = getItem(position).salesEnable
            binding.promotionSeckillItemBuyTv.text = getItem(position).salesEnable.judge("立即抢购","尚未开售")
            binding.promotionSeckillItemBuyPerTv.text = "已售${AppTool.Calculation.getPercentage(getItem(position).countNum,getItem(position).buyNum)}%"
            binding.promotionSeckillItemSpv.setTotalAndCurrentCount(getItem(position).countNum,getItem(position).buyNum)
            binding.promotionSeckillItemBuyTv.setOnClickListener {
                if (it.isSelected){
                    binding.root.context.to<BaseToolActivity>().push("/goods/detail",{ it ->
                        it.withInt("goodsId",getItem(position).goodsId)
                    })
                }else{
                    showMessage("活动尚未开始")
                }
            }
        }
    }
}