package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsPromotionDetailItemLayBinding
import com.enation.javashop.android.component.goods.fragment.GoodsInfoFragment
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.GoodsViewModel
import com.enation.javashop.android.middleware.model.PromotionDetailViewModel
import com.enation.javashop.net.engine.utils.ThreadUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/4/8 下午1:44
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品所属店铺信息
 */
class GoodsPromotionDetailAdapter(val weakControl : WeakReference<GoodsInfoFragment>, var promotion : PromotionDetailViewModel, goods :GoodsViewModel, complete :()->Unit) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsPromotionDetailItemLayBinding>, Int>() {


    /**
     * @Name  timer
     * @Type  TimeEngine
     * @Note  倒计时引擎
     */
    private val timer by lazy {
        if (promotion.secKill != null){
          return@lazy TimeEngine.build(promotion.secKill!!.disEndTime)
        }else if (promotion.groupBuy != null){
            return@lazy TimeEngine.build(promotion.groupBuy!!.endTime,current = true)
        }else {
            return@lazy TimeEngine.build(100000)
        }
    }

    init {
            if(promotion.secKill !=null || promotion.groupBuy != null) {
                timer.setComplete(complete)
            }
            weakControl.get()?.addLifeCycleListener { state ->
            if (state == LIFE_CYCLE_DESTORY){
                if(promotion.secKill !=null || promotion.groupBuy != null){
                    timer.destory()
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   Item过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:45
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsPromotionDetailItemLayBinding> {

        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_promotion_detail_item_lay,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            it.setMargin(0,0,0,10.dpToPx())
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoShopAdapter
     * @Date   2018/4/8 下午1:46
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsPromotionDetailItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (promotion.exchange != null){
                binding.timeLay.gone()
                binding.buyNumTv.visable()
                binding.logoIv.setImageResource(R.drawable.javashop_icon_point)
                binding.logoTv.text = "积分商城"
                val priceStr = String.format("￥%.2f",promotion.exchange!!.exchangeMoney)
                val pointStr = String.format("+%.0f积分",promotion.exchange!!.exchangePoint)
                var contentText = priceStr
                if (promotion.exchange!!.exchangePoint > 0){
                    contentText += pointStr
                }
                val spanStr = SpannableStringBuilder(contentText)
                val bigTextSize = AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) * 0.8 / 5 - 25.dpToPx()).toInt())
                spanStr.setSpan(bigTextSize,1,priceStr.length-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.priceTv.text = spanStr

                val orgPriceStar = SpannableStringBuilder(String.format("￥%.2f",promotion.exchange!!.orgPrice))
                orgPriceStar.setSpan(StrikethroughSpan(),0,orgPriceStar.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.orgPriceTv.text = orgPriceStar
                binding.promotionTitle.text = "商品疯狂售卖中"
                binding.buyNumTv.text = "已售出${promotion.exchange!!.buyNum}件"
            }else if(promotion.secKill != null){
                binding.timeLay.visable()
                binding.buyNumTv.gone()
                binding.logoIv.setImageResource(R.drawable.javashop_icon_seckill)
                binding.logoTv.text = "限时抢购"
                var priceStr = String.format("￥%.2f",promotion.secKill!!.seckillPrice)
                val spanStr = SpannableStringBuilder(priceStr)
                val bigTextSize = AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) * 0.8 / 5 - 25.dpToPx()).toInt())
                spanStr.setSpan(bigTextSize,1,priceStr.length-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.priceTv.text = spanStr
                val orgPriceStar = SpannableStringBuilder(String.format("￥%.2f",promotion.secKill!!.orgPrice))
                orgPriceStar.setSpan(StrikethroughSpan(),0,orgPriceStar.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.orgPriceTv.text = orgPriceStar
                binding.promotionTitle.text = "距离活动结束还有"
                timer.execute({day, hour, min, sec ->
                    binding.root.context.to<BaseToolActivity>().runOnUiThread {
                        binding.dayTv.text = "${day}天"
                        binding.hourTv.text = if (hour < 10){ "0$hour"  } else { "$hour" }
                        binding.minTv.text = if (min < 10){ "0$min"  } else { "$min" }
                        binding.secTv.text = if (sec < 10){ "0$sec"  } else { "$sec" }
                    }
                })
            }else if(promotion.groupBuy != null){
                binding.timeLay.visable()
                binding.buyNumTv.gone()
                binding.logoIv.setImageResource(R.drawable.javashop_icon_groupbuy)
                binding.logoTv.text = "团购"
                var priceStr = String.format("￥%.2f",promotion.groupBuy!!.price)
                val spanStr = SpannableStringBuilder(priceStr)
                val bigTextSize = AbsoluteSizeSpan((ScreenTool.getScreenWidth(binding.root.context) * 0.8 / 5 - 25.dpToPx()).toInt())
                spanStr.setSpan(bigTextSize,1,priceStr.length-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.priceTv.text = spanStr
                val orgPriceStar = SpannableStringBuilder(String.format("￥%.2f",promotion.groupBuy!!.orgPrice))
                orgPriceStar.setSpan(StrikethroughSpan(),0,orgPriceStar.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.orgPriceTv.text = orgPriceStar
                binding.promotionTitle.text = "距离活动结束还有"
                timer.execute({day, hour, min, sec ->
                    weakControl.get()?.activity?.runOnUiThread {
                        binding.dayTv.text = "${day}天"
                        binding.hourTv.text = if (hour < 10){ "0$hour"  } else { "$hour" }
                        binding.minTv.text = if (min < 10){ "0$min"  } else { "$min" }
                        binding.secTv.text = if (sec < 10){ "0$sec"  } else { "$sec" }
                    }
                })
            }
        }
    }

}