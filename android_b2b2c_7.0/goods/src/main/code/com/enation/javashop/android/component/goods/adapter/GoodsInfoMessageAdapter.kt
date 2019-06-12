package com.enation.javashop.android.component.goods.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.databinding.GoodsInfoMessageItemBinding
import com.enation.javashop.android.component.goods.fragment.GoodsInfoFragment
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.TextViewDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopCommonView
import com.enation.javashop.android.lib.widget.PopWindowCompatible
import com.enation.javashop.android.middleware.bind.DataBindingHelper
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.GoodsViewModel
import com.enation.javashop.android.middleware.model.PromotionDetailViewModel
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.goods_act_lay.*
import kotlinx.android.synthetic.main.goods_info_message_item.view.*
import kotlinx.android.synthetic.main.goods_search_act_lay.*
import java.util.*

/**
 * @author LDD
 * @Date   2018/4/8 下午1:49
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品数据信息适配器
 */
class GoodsInfoMessageAdapter(val fragment: GoodsInfoFragment,var goods :GoodsViewModel,var coupons :ArrayList<CouponViewModel>,var promotions :PromotionDetailViewModel?) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoMessageItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:50
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:50
     * @Note   item点击过滤
     * @param  position 下标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:50
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoMessageItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_message_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:50
     * @Note   获取item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:51
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0,0,0,ScreenTool.dip2px(BaseApplication.appContext,10f))
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/5/31 上午11:21
     * @Note   构建促销视图
     * @param  binding DataBinding
     * @param  title   标题
     * @param  content 内容
     */
    private fun createPromotionView(binding : GoodsInfoMessageItemBinding,title :String,content :String){
        var height =  (ScreenTool.getScreenWidth(binding.root.context)-10.dpToPx())*0.15/2
        var promotionLay = LinearLayout(binding.root.context)
        promotionLay.orientation = LinearLayout.HORIZONTAL
        promotionLay.gravity = Gravity.CENTER_VERTICAL
        binding.goodsInfoPromotionContent.addView(promotionLay)
        promotionLay.reLayout<LinearLayout.LayoutParams> {
            params ->
            params.height = height.toInt()
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            if (binding.goodsInfoPromotionContent.childCount > 1){
                params.setMargins(0,5.dpToPx(),0,0)
            }
        }
        var titleView = TextView(binding.root.context).then {
            self ->
            self.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_select_color_red))
            self.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            self.setTextSize(TypedValue.COMPLEX_UNIT_PX, (height*0.5).toFloat())
            self.gravity = Gravity.CENTER
            self.setPadding(3.dpToPx(),1.dpToPx(),3.dpToPx(),1.dpToPx())
            self.text = title
        }
        promotionLay.addView(titleView)
        titleView.reLayout<LinearLayout.LayoutParams> {
            params ->
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(0,0,10.dpToPx(),0)
        }

        var contentView = TextView(binding.root.context).then {
            self ->
            self.setTextColor(Color.BLACK)
            self.setTextSize(TypedValue.COMPLEX_UNIT_PX, (height*0.5).toFloat())
            self.gravity = Gravity.LEFT
            self.setLines(1)
            self.ellipsize = TextUtils.TruncateAt.END
            self.setPadding(0,2.dpToPx(),0,2.dpToPx())
            self.text = content
        }
        promotionLay.addView(contentView)
        contentView.reLayout<LinearLayout.LayoutParams> {
            params ->
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT
            params.weight = 1F
        }

    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/5/31 下午3:21
     * @Note   创建优惠券视图
     * @param  binding DataBinding
     * @param  couponViewModel 优惠券数据
     */
    private fun createCouponView (binding: GoodsInfoMessageItemBinding,couponViewModel: CouponViewModel){
        var height =  (ScreenTool.getScreenWidth(binding.root.context)-10.dpToPx())*0.15/2
        val tv = TextView(binding.root.context).then {
            self ->
            self.setTextColor(Color.WHITE)
            self.setBackgroundResource(R.drawable.javashop_corners_red)
            self.setTextSize(TypedValue.COMPLEX_UNIT_PX, (height*0.5).toFloat())
            self.gravity = Gravity.CENTER
            self.setPadding(5.dpToPx(),2.dpToPx(),5.dpToPx(),2.dpToPx())
            self.text = "满${couponViewModel.basePrice}减${couponViewModel.price}"
        }
        binding.goodsInfoCouponContent.addView(tv)
        tv.reLayout<LinearLayout.LayoutParams> {
            params ->
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT
            params.setMargins(0,0,10.dpToPx(),0)
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/5/31 下午3:33
     * @Note   显示popView
     * @param  activity 页面
     * @param  title    标题
     * @param  adapters 适配器
     */
    private fun showPopView(activity : GoodsActivity,title: String ,vararg adapters : DelegateAdapter.Adapter<*>){
        PopCommonView.build(activity)
                .setAdapters(adapters.asList())
                .setTitle(title)
                .setConfirmVisable(false)
                .show(activity.goods_act_topbar_holder)
    }

    /**
     * @author LDD
     * @From   GoodsInfoMessageAdapter
     * @Date   2018/4/8 下午1:51
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoMessageItemBinding>?, position: Int) {
        holder?.bind {
            binding ->

            val activity = (binding.root.context as GoodsActivity)

            binding.goodsInfoCollectGoods.setOnClickListener {
                activity.collect(goods.goodsId,!goods.collect)
            }

            if (coupons.count() > 0){
                binding.goodsInfoCouponContent.removeAllViews()
                coupons.forEach {
                    createCouponView(binding,it)
                }
            }else{
                binding.goodsInfoCouponLay.visibility = View.GONE
            }

            binding.goodsInfoPromotionContent.removeAllViews()

            if((promotions!!.secKill != null && promotions!!.secKill!!.disEndTime > 0) || promotions!!.groupBuy != null || promotions!!.exchange != null){
                binding.goodsInfoPriceTv.gone()
            }else{
                binding.goodsInfoPriceTv.visable()
            }
            binding.goodsInfoPriceTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ScreenTool.getScreenWidth(binding.root.context)*0.08).toFloat())
            if ((promotions!!.secKill != null && (promotions!!.secKill!!.disStartTime > 0)) || promotions!!.FullDiscountGroup != null || promotions!!.minusPromotion != null || promotions!!.halfPrice != null){
                if (promotions?.halfPrice != null){
                    createPromotionView(binding,"第二件半价","该商品第二件半价")
                }
                if (promotions?.minusPromotion != null){
                    createPromotionView(binding,"单品立减",String.format("立减$%.2f元",promotions!!.minusPromotion!!.minusPrice))
                }
                if (promotions?.secKill != null){
                    if ((promotions!!.secKill!!.disStartTime > 0)){
                        createPromotionView(binding,"限时抢购",String.format("%d天%d小时%d分钟后开始",TimeHandle.getDay(promotions!!.secKill!!.disStartTime.toInt()),
                                TimeHandle.getHour(promotions!!.secKill!!.disStartTime.toInt()),
                                TimeHandle.getSecond(promotions!!.secKill!!.disStartTime.toInt())))
                    }
                }
                if (promotions?.FullDiscountGroup != null){
                    if (promotions?.FullDiscountGroup?.isPonit!!){
                        createPromotionView(binding,"积分",String.format("赠送%d积分",promotions!!.FullDiscountGroup!!.pointValue))
                    }
                    if (promotions?.FullDiscountGroup?.isBonus!! && promotions!!.FullDiscountGroup!!.bonusValue!!.price > 0){
                        createPromotionView(binding,"赠券",String.format("赠送优惠券【满%.2f减%.2f】",
                                promotions!!.FullDiscountGroup!!.bonusValue!!.basePrice,
                                promotions!!.FullDiscountGroup!!.bonusValue!!.price))
                    }
                    if (promotions?.FullDiscountGroup?.isDiscount!!){
                        createPromotionView(binding,"满折",String.format("满%.2f元打%.2f折",
                                promotions!!.FullDiscountGroup!!.basePrice,
                                promotions!!.FullDiscountGroup!!.discount))
                    }
                    if (promotions?.FullDiscountGroup?.isFreeShip!!){
                        createPromotionView(binding,"免邮","免快递费")
                    }
                    if (promotions?.FullDiscountGroup?.isGift!!){
                        createPromotionView(binding,"赠品",String.format("赠送【%s ￥%.2f】",
                                promotions!!.FullDiscountGroup!!.giftValue!!.name,
                                promotions!!.FullDiscountGroup!!.giftValue!!.price))
                    }
                    if (promotions?.FullDiscountGroup?.isMinus!!){
                        createPromotionView(binding,"满减",String.format("满%.2f元减%.2f元",
                                promotions!!.FullDiscountGroup!!.basePrice,
                                promotions!!.FullDiscountGroup!!.minusPrice))
                    }
                }
                binding.goodsInfoPromotionIv.setOnClickListener {
                    if (promotions!=null){
                        showPopView(activity,"促销",GoodsInfoPromotionAdapter(promotions!!))
                    }
                }
            }else{
                binding.goodsInfoPromotionContent.visibility = View.GONE
                binding.goodsInfoPromotionIv.visibility = View.GONE
                binding.goodsInfoPromotionTitle.visibility = View.GONE
                binding.goodsInfoPromotionLay.visibility = View.GONE
            }

            if (coupons.count() > 0){
                binding.goodsInfoCouponIv.setOnClickListener {
                    showPopView(activity,"优惠券",TextViewDelegateAdapter("可领取优惠券",23,Color.BLACK,10.dpToPx(),10.dpToPx(),10.dpToPx(),10.dpToPx(),Color.TRANSPARENT),
                            ReflexHelper.build("com.enation.javashop.android.component.member.adapter.CouponAdapter")
                                    .newInstance(coupons).getInstance<BaseDelegateAdapter<*,CouponViewModel>>().then {
                                        it.setOnItemClickListener { data, position ->
                                            fragment.getCoupon(data.id)
                                        }
                                    })
                }
            }

            binding.goodsInfoNameTv.text = goods.name

            if(goods.canInquiry == 1){
                binding.goodsInfoPriceTv.visibility = View.INVISIBLE
                binding.goodsInfoInquiryPriceLay.visibility = View.VISIBLE
                binding.goodsInfoInquiryPriceLay.setOnClickListener {
                    DataBindingHelper.gotoInquiryPrice(activity, goods.goodsImage, goods.name, goods.defaultSpec, goods.goodsId)
                }
            } else {
                binding.goodsInfoPriceTv.text = String.format("￥%.2f",goods.price)
                binding.goodsInfoPriceTv.visibility = View.VISIBLE
                binding.goodsInfoInquiryPriceLay.visibility = View.INVISIBLE
            }

            if (goods.collect) {
                binding.goodsInfoCollectGoodsIv.setImageResource(R.drawable.javashop_icon_heart_selected)
                binding.goodsInfoCollectGoodsTv.text = "已关注"
            }else{
                binding.goodsInfoCollectGoodsIv.setImageResource(R.drawable.javashop_icon_heart_nomal)
                binding.goodsInfoCollectGoodsTv.text = "关注"
            }
        }
    }
}