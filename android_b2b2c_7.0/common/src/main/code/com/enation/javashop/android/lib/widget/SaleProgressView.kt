package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.enation.javashop.android.lib.R
import java.text.DecimalFormat
import android.R.attr.bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth





/**
 * @author LDD
 * @Date   2018/5/18 上午10:48
 * @From   com.enation.javashop.android.lib.widget
 * @Note   促销进度视图
 */
class SaleProgressView :View {

    //商品总数
    private var totalCount: Int = 0
    //当前卖出数
    private var currentCount: Int = 0
    //动画需要的
    private var progressCount: Int = 0
    //售出比例
    private var scale: Float = 0.toFloat()
    //边框颜色
    private var sideColor: Int = 0
    //文字颜色
    private var textColor: Int = 0
    //边框粗细
    private var sideWidth: Float = 0.toFloat()
    //边框所在的矩形
    private lateinit var sidePaint: Paint
    //背景矩形
    private var bgRectF: RectF? = null
    private var radius: Float = 0.toFloat()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mPorterDuffXfermode: PorterDuffXfermode? = null
    private lateinit var srcPaint: Paint
    private var fgSrc: Bitmap? = null
    private var bgSrc: Bitmap? = null
    private var textHeader: String? = null
    private var nearOverText: String? = null
    private var overText: String? = null
    private var textSize: Float = 0.toFloat()
    private lateinit var textPaint: Paint
    private var nearOverTextWidth: Float = 0.toFloat()
    private var overTextWidth: Float = 0.toFloat()
    private var baseLineY: Float = 0.toFloat()
    private var bgBitmap: Bitmap? = null
    private var isNeedAnim: Boolean = false
    private var onlyColor: String? = null

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttrs(context,attrs)
        initPaint()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView)
        sideColor = ta.getColor(R.styleable.SaleProgressView_sideColor, -0xc3ce)
        textColor = ta.getColor(R.styleable.SaleProgressView_textColor, -0xc3ce)
        sideWidth = ta.getDimension(R.styleable.SaleProgressView_sideWidth, dp2px(2f))
        overText = ta.getString(R.styleable.SaleProgressView_overText)
        textHeader = ta.getString(R.styleable.SaleProgressView_textHeader)
        nearOverText = ta.getString(R.styleable.SaleProgressView_nearOverText)
        textSize = ta.getDimension(R.styleable.SaleProgressView_textSize, sp2px(10f))
        isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_isNeedAnim, true)
        onlyColor = ta.getString(R.styleable.SaleProgressView_onlyColor)
        ta.recycle()
    }

    private fun initPaint() {
        sidePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sidePaint.style = Paint.Style.STROKE
        sidePaint.strokeWidth = sideWidth
        sidePaint.color = sideColor
        srcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        srcPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = textSize
        mPorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        nearOverTextWidth = textPaint.measureText(nearOverText)
        overTextWidth = textPaint.measureText(overText)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //获取View的宽高
        mWidth = measuredWidth
        mHeight = measuredHeight

        //圆角半径
        radius = mHeight / 2.0f

        //留出一定的间隙，避免边框被切掉一部分
        if (bgRectF == null) {
            bgRectF = RectF(sideWidth, sideWidth, mWidth - sideWidth, mHeight - sideWidth)
        }

        if (baseLineY == 0.0f) {
            val fm = textPaint.fontMetricsInt
            baseLineY = (mHeight / 2 - (fm!!.descent / 2 + fm.ascent / 2)).toFloat()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isNeedAnim) {
            progressCount = currentCount
        }

        scale = if (totalCount == 0) {
            0.0f
        } else {
            DecimalFormat("0.00").format(progressCount.toFloat() / totalCount.toFloat()).toFloat()
        }

        drawSide(canvas)
        drawBg(canvas)
        drawFg(canvas)
        drawText(canvas)

        //这里是为了演示动画方便，实际开发中进度只会增加
        if (progressCount != currentCount) {
            if (progressCount < currentCount) {
                progressCount++
            } else {
                progressCount--
            }
            postInvalidate()
        }

    }

    //绘制背景边框
    private fun drawSide(canvas: Canvas) {
        canvas.drawRoundRect(bgRectF, radius, radius, sidePaint)
    }

    //绘制背景
    private fun drawBg(canvas: Canvas) {
        if (bgBitmap == null) {
            bgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        }
        val bgCanvas = Canvas(bgBitmap)
        if (bgSrc == null) {
            if (onlyColor != null) {
                bgSrc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bgSrc?.eraseColor(Color.WHITE)
            } else {
                bgSrc = BitmapFactory.decodeResource(resources, R.drawable.sale_progress_view_bg)
            }
        }
        bgCanvas.drawRoundRect(bgRectF, radius, radius, srcPaint)

        srcPaint.xfermode = mPorterDuffXfermode

        bgCanvas.drawBitmap(bgSrc, null, bgRectF, srcPaint)

        canvas.drawBitmap(bgBitmap, 0f, 0f, null)

        srcPaint.xfermode = null
    }

    //绘制进度条
    private fun drawFg(canvas: Canvas) {
        if (scale == 0.0f) {
            return
        }
        val fgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val fgCanvas = Canvas(fgBitmap)
        if (fgSrc == null) {
            if (onlyColor != null) {
                fgSrc = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
                val color = Color.parseColor(onlyColor)
                fgSrc?.eraseColor(color)
                fgSrc = toRound(fgSrc!!)
            } else {
                fgSrc = BitmapFactory.decodeResource(resources, R.drawable.sale_progress_view_fg)
            }
        }

        fgCanvas.drawRoundRect(
                RectF(sideWidth, sideWidth, (mWidth - sideWidth) * scale, mHeight - sideWidth),
                radius, radius, srcPaint)

        srcPaint.xfermode = mPorterDuffXfermode
        fgCanvas.drawBitmap(fgSrc, null, bgRectF, srcPaint)

        canvas.drawBitmap(fgBitmap, 0f, 0f, null)
        srcPaint.xfermode = null
    }

    //绘制文字信息
    private fun drawText(canvas: Canvas) {
        val scaleText = DecimalFormat("#%").format(scale)

        val saleText = if (textHeader != null) {
            textHeader + String.format("%s件", progressCount)
        } else {
            String.format("已抢%s件", progressCount)
        }

        val scaleTextWidth = textPaint.measureText(scaleText)

        val textBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val textCanvas = Canvas(textBitmap)
        if (onlyColor != null) {
            textPaint.color = Color.TRANSPARENT
        } else {
            textPaint.color = textColor
        }

        textPaint.textSize = (mHeight*0.6).toFloat()

        if (scale < 0.8f) {
            textCanvas.drawText(saleText, dp2px(10f), baseLineY, textPaint)
            textCanvas.drawText(scaleText, mWidth - scaleTextWidth - dp2px(10f), baseLineY, textPaint)
        } else if (scale < 1.0f) {
            textCanvas.drawText(nearOverText, mWidth / 2 - nearOverTextWidth / 2, baseLineY, textPaint)
            textCanvas.drawText(scaleText, mWidth - scaleTextWidth - dp2px(10f), baseLineY, textPaint)
        } else {
            textCanvas.drawText(overText, mWidth / 2 - overTextWidth / 2, baseLineY, textPaint)
        }

        textPaint.xfermode = mPorterDuffXfermode
        if (onlyColor != null) {
            textPaint.color = Color.TRANSPARENT
        } else {
            textPaint.color = Color.WHITE
        }
        if (onlyColor != null) {
            textCanvas.drawRoundRect(
                    RectF(sideWidth - 1, sideWidth, (mWidth - sideWidth) * scale + 1, mHeight - sideWidth),
                    radius, radius, textPaint)
        } else {
            textCanvas.drawRoundRect(
                    RectF(sideWidth, sideWidth, (mWidth - sideWidth) * scale, mHeight - sideWidth),
                    radius, radius, textPaint)
        }
        canvas.drawBitmap(textBitmap, 0f, 0f, null)
        textPaint.xfermode = null
    }

    private fun dp2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    private fun sp2px(spValue: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return (spValue * scale + 0.5f)
    }

    private fun toRound(bitmap: Bitmap):Bitmap{
        val output = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = bitmap.height/2f - sideWidth

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    fun setTotalAndCurrentCount(totalCount: Int, currentCount: Int) {
        this.totalCount = totalCount

        if (currentCount > totalCount) {
            this.currentCount = totalCount
        }else{
            this.currentCount = currentCount
        }

        postInvalidate()
    }

}