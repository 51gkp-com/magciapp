package com.enation.javashop.android.middleware.bind

import android.app.Activity
import android.databinding.BindingAdapter
import android.graphics.Color
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.R
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool
import com.m7.imkfsdk.KfStartHelper
import com.moor.imkf.model.entity.CardInfo


object DataBindingHelper {

    @BindingAdapter(value = "bind:grade_point", requireAll = true)
    @JvmStatic
    fun setReply(view: TextView, point: Int) {
        view.text = "等级积分：$point"
    }

    @BindingAdapter(value = "bind:cosnum_point", requireAll = true)
    @JvmStatic
    fun cosnumPoint(view: TextView, point: Int) {
        view.text = "消费积分：$point"
    }

    @BindingAdapter(value = "bind:status_bar", requireAll = true)
    @JvmStatic
    fun statusBar(view : View , type :Int){
        view.reLayout<ViewGroup.LayoutParams> {
            params ->
            params.height = AppTool.SystemUI.getStatusBarHeight()
        }
    }

    @BindingAdapter(value = "bind:smart_padding", requireAll = true)
    @JvmStatic
    fun smartPadding(view : View , type :Int){
        view.reLayout<ViewGroup.LayoutParams> {
            params ->
            val MIN_API = 17
            if (Build.VERSION.SDK_INT >= MIN_API) {
                val statusBarHeight = AppTool.SystemUI.getStatusBarHeight()
                val lp = view.layoutParams
                if (lp != null && lp.height > 0) {
                    lp.height += statusBarHeight//增高
                }
                view.setPadding(view.paddingLeft, view.paddingTop + statusBarHeight,
                        view.paddingRight, view.paddingBottom)
            }
        }
    }

    @BindingAdapter(value = "bind:auto_topbar_height", requireAll = true)
    @JvmStatic
    fun topbarAutoHeight(view : View , type :Boolean){
        view.reLayout<ViewGroup.LayoutParams> {
            params ->
            params.height = (AppTool.SystemUI.getStatusBarHeight() + ScreenTool.getScreenWidth(view.context) * 0.135).toInt()
        }
    }

    @BindingAdapter(value = "bind:auto_topbar_content_height", requireAll = true)
    @JvmStatic
    fun topbarContentAutoHeight(view : View , type :Boolean){
        view.reLayout<ViewGroup.LayoutParams> {
            params ->
            params.height = (ScreenTool.getScreenWidth(view.context) * 0.135).toInt()
        }
    }

    @BindingAdapter(value = "bind:comment_reply", requireAll = true)
    @JvmStatic
    fun gradePoint(view: TextView, reply: String?) {
        if (reply == null || reply.isEmpty()) {
            view.visibility = View.GONE
            return
        }
        view.visibility = View.VISIBLE
        view.text = SpannableStringBuilder("客服回复：$reply").then { self ->
            self.setSpan(ForegroundColorSpan(view.context.getColorCompatible(R.color.javashop_color_price_red)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }



    @BindingAdapter(value = "bind:to_shop", requireAll = true)
    @JvmStatic
    fun toShop(view: View, shopId: Int) {
        view.setOnClickListener(OnClickListenerAntiViolence(event = {
            Do.prepare().doOnBack { call ->
                JRouter.prepare().create("/shop/detail").withInt("shopId",shopId).withTransition(R.anim.push_left_in, R.anim.push_left_out).seek(view.context)
                call.invoke()
            }.execute()
        }))
    }

    @BindingAdapter(value = "bind:to_goods", requireAll = true)
    @JvmStatic
    fun toGoods(view: View, goodsId: Int) {
        view.setOnClickListener(OnClickListenerAntiViolence(event = {
            Do.prepare().doOnBack { call ->
                JRouter.prepare().create("/goods/detail").withInt("goodsId",goodsId).withTransition(R.anim.push_left_in, R.anim.push_left_out).seek(view.context)
                call.invoke()
            }.execute()
        }))
    }

    @BindingAdapter(value = "bind:to_inquiry_price", requireAll = true)
    @JvmStatic
    fun toInquiryPrice(view: View, goods: GoodsItemViewModel) {
        view.setOnClickListener(OnClickListenerAntiViolence(event = {
            Do.prepare().doOnBack { call ->
                if(view.context is Activity){
                    gotoInquiryPrice((view.context as Activity), goods.goodsImage, goods.goodsName, "",  goods.goodsId)
                }
                call.invoke()
            }.execute()
        }))
    }


    @BindingAdapter(value = "bind:action", requireAll = true)
    @JvmStatic
    fun back(view: View, action: Int) {
        when (action) {
            DataBindingAction.BACK -> {
                if (view.context is BaseToolActivity) {
                    view.setOnClickListener(OnClickListenerAntiViolence(event = {
                        (view.context as BaseToolActivity).onBackPressed()
                    }))
                }
            }
        }
    }

    @BindingAdapter(value = "bind:width_percent", requireAll = true)
    @JvmStatic
    fun width(view: View, percent: Double) {
        view.reLayout<ViewGroup.LayoutParams> { params ->
            params.width = (ScreenTool.getScreenWidth(view.context) * 0.01 * percent).toInt()
        }
    }

    @BindingAdapter(value = "bind:height_percent", requireAll = true)
    @JvmStatic
    fun height(view: View, percent: Double) {
        view.reLayout<ViewGroup.LayoutParams> { params ->
            params.height = (ScreenTool.getScreenHeight(view.context) * 0.01 * percent).toInt()
        }
    }

    @BindingAdapter(value = "bind:width_percent_height_ratio", requireAll = true)
    @JvmStatic
    fun widthWithHeight(view: View, attr: ViewAttrModel) {
        view.reLayout<ViewGroup.LayoutParams> { params ->
            params.width = (ScreenTool.getScreenWidth(view.context) * 0.01 * attr.percent).toInt()
            params.height = (params.width * 0.01 * attr.prop).toInt()
        }
    }

    @BindingAdapter(value = "bind:height_with_width_percent", requireAll = true)
    @JvmStatic
    fun widthWithHeight(view: View, percent: Int) {
        view.reLayout<ViewGroup.LayoutParams> { params ->
            params.height = (ScreenTool.getScreenWidth(view.context) * 0.01 * percent).toInt()
        }
    }

    @BindingAdapter(value = "bind:height_percent_width_ratio", requireAll = true)
    @JvmStatic
    fun heightWithWidth(view: View, attr: ViewAttrModel) {
        view.reLayout<ViewGroup.LayoutParams> { params ->
            params.height = (ScreenTool.getScreenHeight(view.context) * 0.01 * attr.percent).toInt()
            params.width = (params.height * 0.01 * attr.prop).toInt()
        }
    }

    @BindingAdapter(value = "bind:price_with_shop_home", requireAll = true)
    @JvmStatic
    fun setPriceWithShopName(view: TextView, data: GoodsItemViewModel) {
        val textHeight = ScreenTool.getScreenWidth(BaseApplication.appContext) / 2 * 1.25 / 13
        view.text = SpannableStringBuilder("￥${data.goodsPrice}起 ￥${data.orginPrice}起").then { self ->
            self.setSpan(AbsoluteSizeSpan((textHeight * 0.5).toInt()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan((textHeight).toInt()), 1, "￥${data.goodsPrice}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan((textHeight * 0.5).toInt()), "￥${data.goodsPrice}".length, "￥${data.goodsPrice}".length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan((textHeight * 0.5).toInt()), "￥${data.goodsPrice}起".length + 1, "￥${data.goodsPrice}起 ￥${data.orginPrice}起".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(ForegroundColorSpan(Color.parseColor("#888888")), "￥${data.goodsPrice}起".length + 1, "￥${data.goodsPrice}起 ￥${data.orginPrice}起".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(StrikethroughSpan(), "￥${data.goodsPrice}起".length + 1, "￥${data.goodsPrice}起 ￥${data.orginPrice}起".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(ForegroundColorSpan(Color.parseColor(data.priceColor)), 0, "￥${data.goodsPrice}起".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    @BindingAdapter(value = "bind:to_order", requireAll = true)
    @JvmStatic
    fun toOrder(view: View, state: Int) {
        view.setOnClickListener(OnClickListenerAntiViolence(event = {
            toOrderImpl(view,state)
        }))
    }


    @BindingAdapter(value = "bind:text_num_count_percent",requireAll = true)
    @JvmStatic
    fun textNumCountPercent(view :TextView ,text :ObserableString){
        view.text = "${text.get().length}/500"
    }

    /**
     * @author LDD
     * @From   DataBindingHelper
     * @Date   2018/4/16 下午5:05
     * @Note   跳转订单实现方法
     * @param  state 列表
     * @param  view  视图
     */
    fun toOrderImpl(view: View, state: Int) {
        Do.prepare().doOnBack { call ->
            if (MemberState.manager.getLoginState()){
                JRouter.prepare().create("/order/list").withInt("state",state).withTransition(R.anim.push_left_in, R.anim.push_left_out).seek(view.context)
            }else{
                JRouter.prepare().create("/member/login/main").withTransition(R.anim.push_left_in, R.anim.push_left_out).seek(view.context)
            }
            call.invoke()
        }.execute()
    }


    @JvmStatic
    fun createOrderGoodsNumText(goodsNum :Int):String{
        return "共${goodsNum}件商品 实付款："
    }

    @JvmStatic
    fun priceToText(price :Double):String{
        return "￥$price"
    }

    @JvmStatic
    fun couponDiscount(price :Double):String{
        return "满${price}元可用"
    }

    @JvmStatic
    fun couponDes(shop :String):String{
        return "仅可购买${shop}店铺商品"
    }

    @JvmStatic
    fun userNameFix(shop :String?):String{
        if (shop == null) {
            return ""
        }
        return "用户名：${shop}"
    }

    @JvmStatic
    fun collectText(collectNum :Int):String{
        return "$collectNum 人关注"
    }

    @JvmStatic
    fun gotoInquiryPrice(activity: Activity, icon: String, name: String, des: String , goodsId: Int){
        val helper = KfStartHelper(activity)
        val ci = CardInfo(icon, name, des, "￥询价", "http://www.51gkp.com/goods/$goodsId")
        helper.setCard(ci)
        val accessId = "ef326d00-80eb-11e9-a433-57669e6b65a0"
        val userName = "玛吉克商城客服"
        val userId = "001"
        helper.initSdkChat(accessId, userName, userId)
    }


}