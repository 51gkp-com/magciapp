package com.enation.javashop.android.lib.bind

import android.databinding.BindingAdapter
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.getColorCompatible
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.imagepluin.R
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/1/19 下午3:20
 * @From   com.enation.javashop.android.lib.bind
 * @Note   databinding辅助类
 */
object BaseBindingHelper {
    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.binding.BindingHelper
     * @Data   2017/12/26 上午9:23
     * @Note   加载图片
     * @param  imageView 需要加载图片的ImageView
     * @param  url       图片Url
     */
    @BindingAdapter(value = "bind:url",requireAll = false)
    @JvmStatic fun loadImage(imageView: ImageView?, url: String?) {
        if(imageView == null || url == null ||url == ""){
            return
        }
        Glide.with(imageView.context).load(url).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.image_loading).error(R.drawable.image_error).into(imageView)
    }

    @BindingAdapter(value = "bind:url_without_holder",requireAll = false)
    @JvmStatic fun loadImageWithoutHoldlder(imageView: ImageView?, url: String?) {
        if(imageView == null || url == null ||url == ""){
            return
        }
        Glide.with(imageView.context).load(url).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.image_error).into(imageView)
    }
    /**
     * @author LDD
     * @From   BaseBindingHelper
     * @Date   2018/2/24 下午2:22
     * @Note   显示价格大小
     * @param  value 价格
     */
    @BindingAdapter(value = "bind:price_text", requireAll = false)
    @JvmStatic
    fun setPriceText(view: TextView, value: Double) {
        view.text = SpannableStringBuilder("￥$value").then { self ->
            self.setSpan(AbsoluteSizeSpan(ScreenTool.dip2px(view.context, 12f)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan(ScreenTool.dip2px(view.context, 15f)), 1, self.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            self.setSpan(AbsoluteSizeSpan(ScreenTool.dip2px(view.context, 12f)), self.indexOf("."), self.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    /**
     * @author LDD
     * @From   HomeFragmentBindHelper
     * @Date   2018/1/30 下午3:05
     * @Note   透明渐变
     */
    @BindingAdapter(value = "bind:alpha",requireAll = false)
    @JvmStatic fun backgroundGradient(view: ConstraintLayout, value:Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var alpha = value
            if (alpha > 255) {
                alpha = 255
                if (view.background.alpha == 255) {
                    return
                }
            }
            view.background.alpha = alpha
        }
    }

    /**
     * @author LDD
     * @From   BaseBindingHelper
     * @Date   2018/2/28 上午9:34
     * @Note   解决ConstraintLayout 无法上色
     */
    @BindingAdapter(value = "bind:backgroundColor",requireAll = false)
    @JvmStatic fun setBackgroundColor(view : View, value: Int){
        view.setBackgroundColor(value)
    }

    @BindingAdapter(value = "bind:text_count_for_line",requireAll = true)
    @JvmStatic fun setTextSize(tv :TextView , textCount :Int){
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,ScreenTool.getScreenWidth(BaseApplication.appContext)/textCount)
    }
}