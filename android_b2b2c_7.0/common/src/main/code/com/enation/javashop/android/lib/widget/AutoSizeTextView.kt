package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.utils.base.tool.ScreenTool


/**
 * @author LDD
 * @Date   2018/3/19 上午11:44
 * @From   com.enation.javashop.android.lib.widget
 * @Note   自动改变文字大小的TextView
 */
class AutoSizeTextView : TextView {

    private val mTextPaint by lazy {
        TextPaint()
    }
    private var mMaxTextSize: Float = 100.toFloat() // 获取当前所设置文字大小作为最大文字大小
    private var mMinTextSize = 8f    //最小的字体大小


    private var priceType = false

    private var autoSize = false

    private var decimalHandle = 1

    private val AlwaysRetain = 1

    private var deleteLine = false

    private val IfExistRetain = 2

    private val AlwaysRemove = 3

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView)

        priceType = typeArray.getBoolean(R.styleable.AutoSizeTextView_price_type,false)

        decimalHandle = typeArray.getInt(R.styleable.AutoSizeTextView_decimal_handle,AlwaysRetain)

        autoSize = typeArray.getBoolean(R.styleable.AutoSizeTextView_auto_size,false)

        deleteLine = typeArray.getBoolean(R.styleable.AutoSizeTextView_delete_line,false)

        typeArray.recycle()
    }

    init {
        gravity = gravity or Gravity.CENTER_VERTICAL // 默认水平居中
        initialise()
    }

    private fun initialise() {
        mTextPaint.set(this.paint)
        //默认的大小是设置的大小，如果撑不下了 就改变
        mMaxTextSize = this.textSize
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {

        if (priceType) {
            try {
                text.toString().toDouble()
            } catch (e: Exception) {
                refitText(text.toString(), this.height, true)
                return
            }

            when (decimalHandle) {
                AlwaysRetain -> {
                    val price = text.toString().toDouble()
                    var maxSize = refitText(text.toString(), height, false)
                    this.text = SpannableStringBuilder("￥$price").then { self ->
                        if (deleteLine){
                            self.setSpan(StrikethroughSpan(), 0,text.length+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }else{
                            self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            self.setSpan(AbsoluteSizeSpan((maxSize).toInt()), 1, self.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), self.indexOf("."), self.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }
                }
                IfExistRetain -> {
                    val str = "${text.toString().toDouble()}"
                    var decimal = str.substring(text.indexOf("."), text.length)
                    var maxSize = refitText(text.toString(), height, false)
                    if (decimal == ".00" || decimal == ".0"){
                        this.text = SpannableStringBuilder("￥${str.removeRange(str.indexOf("."), str.length)}").then { self ->
                            if (deleteLine){
                                self.setSpan(StrikethroughSpan(), 0,text.length+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }else{
                                self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                self.setSpan(AbsoluteSizeSpan((maxSize).toInt()),1, self.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }
                    }else{
                        this.text = SpannableStringBuilder("￥$str").then { self ->
                            if (deleteLine){
                                self.setSpan(StrikethroughSpan(), 0,text.length+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }else{
                                self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                self.setSpan(AbsoluteSizeSpan((maxSize).toInt()), 1, self.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), self.indexOf("."), self.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }
                    }
                }
                AlwaysRemove -> {
                    val str = "${text.toString().toDouble()}"
                    var maxSize = refitText(text.toString(), height, false)
                    this.text = SpannableStringBuilder("￥${str.removeRange(str.indexOf("."), str.length)}").then { self ->
                        if (deleteLine){
                            self.setSpan(StrikethroughSpan(), 0,text.length+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }else{
                            self.setSpan(AbsoluteSizeSpan((maxSize * 0.6).toInt()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            self.setSpan(AbsoluteSizeSpan((maxSize).toInt()),1, self.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }
                }
            }
        } else {
            refitText(text.toString(), this.height, true)
        }

        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }


    private fun refitText(textString: String, height: Int, isSet: Boolean): Float {
        if (height > 0) {
            val availableHeight = (height - this.paddingTop - this.paddingBottom) / maxLines  //减去边距为字体的实际高度
            var trySize = mMaxTextSize
            mTextPaint.textSize = trySize
            while (mTextPaint.descent() - mTextPaint.ascent() > availableHeight) {   //测量的字体高度过大，不断地缩放
                trySize -= 1f  //字体不断地减小来适应
                if (trySize <= mMinTextSize) {
                    trySize = mMinTextSize  //最小为这个
                    break
                }
                mTextPaint.textSize = trySize
            }
            if (isSet) {
                textSize = ScreenTool.px2sp(context, trySize).toFloat()
            }
            return trySize
        }
        return 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (h != oldh) {
            refitText(this.text.toString(), h, true)
        }
    }
}