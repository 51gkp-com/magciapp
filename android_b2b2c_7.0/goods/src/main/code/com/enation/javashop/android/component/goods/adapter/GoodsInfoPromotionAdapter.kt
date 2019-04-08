package com.enation.javashop.android.component.goods.adapter

import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsInfoPromotionItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.TimeHandle
import com.enation.javashop.android.lib.utils.bindingParams
import com.enation.javashop.android.middleware.model.PromotionDetailViewModel

/**
 * @author LDD
 * @Date   2018/5/31 下午2:26
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品详情页面适配器
 */
class GoodsInfoPromotionAdapter (val data :PromotionDetailViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoPromotionItemBinding>,PromotionDetailViewModel>() {

    /**
     * @Name  count
     * @Type  Int
     * @Note  item总数
     */
    val count by lazy {
        var count = 0
        if (data.halfPrice != null){
            count += 1
            halfPrice = true
        }
        if (data.minusPromotion != null){
            count += 1
            minusPromotion = true
        }
        if (data.secKill != null){
            if (data.secKill!!.disStartTime > 0){
                count += 1
                secKill = true
            }
        }
        if (data.FullDiscountGroup != null){
            if (data.FullDiscountGroup?.isPonit!!){
                count += 1
                isPonit = true
            }
            if (data.FullDiscountGroup?.isBonus!! && data.FullDiscountGroup?.bonusValue!!.price > 0){
                count += 1
                isBonus = true
            }
            if (data.FullDiscountGroup?.isDiscount!!){
                count += 1
                isDiscount = true
            }
            if (data.FullDiscountGroup?.isFreeShip!!){
                count += 1
                isFreeShip = true
            }
            if (data.FullDiscountGroup?.isGift!!){
                count += 1
                isGift = true
            }
            if (data.FullDiscountGroup?.isMinus!!){
                count += 1
                isMinus = true
            }
        }
        return@lazy count
    }

    /**
     * @Name  exchange
     * @Type  Boolean
     * @Note  积分商城标记
     */
    private var exchange : Boolean = false

    /**
     * @Name  groupBuy
     * @Type  Boolean
     * @Note  团购标记
     */
    private var groupBuy : Boolean = false

    /**
     * @Name  halfPrice
     * @Type  Boolean
     * @Note  第二件半价标记
     */
    private var halfPrice : Boolean = false

    /**
     * @Name  minusPromotion
     * @Type  Boolean
     * @Note  单品立减标记
     */
    private var minusPromotion : Boolean = false

    /**
     * @Name  secKill
     * @Type  Boolean
     * @Note  秒杀标记
     */
    private var secKill : Boolean = false

    /**
     * @Name  isPonit
     * @Type  Boolean
     * @Note  积分标记
     */
    private var isPonit : Boolean = false

    /**
     * @Name  isBonus
     * @Type  Boolean
     * @Note  优惠券标记
     */
    private var isBonus : Boolean = false

    /**
     * @Name  isDiscount
     * @Type  Boolean
     * @Note  满折标记
     */
    private var isDiscount : Boolean = false

    /**
     * @Name  isFreeShip
     * @Type  Boolean
     * @Note  免邮标记
     */
    private var isFreeShip : Boolean = false

    /**
     * @Name  isGift
     * @Type  Boolean
     * @Note  赠品标记
     */
    private var isGift : Boolean = false

    /**
     * @Name  isMinus
     * @Type  Boolean
     * @Note  满减标记
     */
    private var isMinus : Boolean = false


    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:27
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:28
     * @Note   点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:29
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoPromotionItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.goods_info_promotion_item)
    }

    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:31
     * @Note   获取itemCount
     */
    override fun getItemCount(): Int {
        return count
    }

    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:31
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,count)
    }

    /**
     * @author LDD
     * @From   GoodsInfoPromotionAdapter
     * @Date   2018/5/31 下午2:33
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoPromotionItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (data.halfPrice != null && halfPrice){
                binding.goodsinfoPromotionItemContent.text = "该商品第二件半价"
                binding.goodsinfoPromotionItemTitle.text = "第二件半价"
                halfPrice = false
                return@bind
            }
            if (data.minusPromotion != null && minusPromotion){
                binding.goodsinfoPromotionItemContent.text = String.format("立减%.2f元",data.minusPromotion!!.minusPrice)
                binding.goodsinfoPromotionItemTitle.text = "单品立减"
                minusPromotion = false
                return@bind
            }
            if (data.secKill != null && secKill){
                if ((data.secKill!!.disStartTime > 0)){
                    binding.goodsinfoPromotionItemContent.text = String.format("%d天%d小时%d分钟后开始", TimeHandle.getDay(data.secKill!!.disStartTime.toInt()),
                            TimeHandle.getHour(data.secKill!!.disStartTime.toInt()),
                            TimeHandle.getSecond(data.secKill!!.disStartTime.toInt()))
                    binding.goodsinfoPromotionItemTitle.text = "限时抢购"
                    secKill = false
                    return@bind
                }
            }
            if (data.FullDiscountGroup != null){
                if (data.FullDiscountGroup?.isPonit!! && isPonit){
                    binding.goodsinfoPromotionItemContent.text = String.format("赠送%d积分",data.FullDiscountGroup!!.pointValue)
                    binding.goodsinfoPromotionItemTitle.text = "积分"
                    isPonit = false
                    return@bind
                }
                if (isBonus && data.FullDiscountGroup?.isBonus!! && data.FullDiscountGroup!!.bonusValue!!.price > 0){
                    binding.goodsinfoPromotionItemContent.text = String.format("赠送优惠券【满%.2f减%.2f】",
                            data.FullDiscountGroup!!.bonusValue!!.basePrice,
                            data.FullDiscountGroup!!.bonusValue!!.price)
                    binding.goodsinfoPromotionItemTitle.text = "赠券"
                    isBonus = false
                    return@bind
                }
                if (data.FullDiscountGroup?.isDiscount!! && isDiscount){
                    binding.goodsinfoPromotionItemContent.text = String.format("满%.2f元打%.2f折",
                            data.FullDiscountGroup!!.basePrice,
                            data.FullDiscountGroup!!.discount)
                    binding.goodsinfoPromotionItemTitle.text = "满折"
                    isDiscount = false
                    return@bind
                }
                if (data.FullDiscountGroup?.isFreeShip!! && isFreeShip){
                    binding.goodsinfoPromotionItemContent.text = "免快递费"
                    binding.goodsinfoPromotionItemTitle.text = "免邮"
                    isFreeShip = false
                    return@bind
                }
                if (data.FullDiscountGroup?.isGift!! && isGift){
                    binding.goodsinfoPromotionItemContent.text = String.format("赠送【%s ￥%.2f】",
                            data.FullDiscountGroup!!.giftValue!!.name,
                            data.FullDiscountGroup!!.giftValue!!.price)
                    binding.goodsinfoPromotionItemTitle.text = "赠品"
                    isGift = false
                    return@bind
                }
                if (data.FullDiscountGroup?.isMinus!! && isMinus){
                    binding.goodsinfoPromotionItemContent.text = String.format("满%.2f元减%.2f元",
                            data.FullDiscountGroup!!.basePrice,
                            data.FullDiscountGroup!!.minusPrice)
                    binding.goodsinfoPromotionItemTitle.text = "满减"
                    isMinus = false
                    return@bind
                }
            }
        }
    }

}