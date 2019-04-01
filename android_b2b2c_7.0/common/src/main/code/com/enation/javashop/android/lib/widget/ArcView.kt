package com.enation.javashop.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.Image
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.enation.javashop.android.lib.R




/**
 * @author LDD
 * @Date   2018/2/26 上午10:30
 * @From   com.enation.javashop.android.lib.widget
 * @Note   弧形View
 */
class ArcView : ImageView {

    /**
     * @Name  arcHeight
     * @Type  Int
     * @Note  弧形高度
     */
    private var arcHeight = 0

    /**
     * @Name  arcShape
     * @Type  ArcShape
     * @Note  内部还是外部
     */
    private var arcShape = ArcShape.outSide

    /**
     * @Name  arcPosition
     * @Type  ArcPosition
     * @Note  弧形方向
     */
    private var arcPosition = ArcPosition.bottom

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/27 上午9:10
     * @Note   一参构造调用二餐
     */
    constructor(context: Context) : this(context,null)

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/27 上午9:11
     * @Note   二参调用三参
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/27 上午9:11
     * @Note   实际调用的构造方法
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        arcHeight = typeArray.getDimensionPixelSize(R.styleable.ArcView_arc_height,10)
        typeArray.recycle()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/27 上午9:11
     * @Note   绘制弧形
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (arcHeight > 0) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = Color.WHITE
            var saveCount = 0
            saveCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
            }else{
                canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            }
            super.onDraw(canvas)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
                canvas.drawPath(createClipPath(arcPosition,arcShape), paint)
            }else{
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
                val path = Path()
                path.addRect(0F, 0F, width.toFloat(),height.toFloat(), Path.Direction.CW)
                path.op(createClipPath(arcPosition,arcShape), Path.Op.DIFFERENCE)
                canvas.drawPath(createClipPath(arcPosition,arcShape), paint)
            }
            canvas.restoreToCount(saveCount)
            paint.xfermode = null
        } else {
            super.onDraw(canvas)
        }
    }

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/27 上午9:17
     * @Note   绘制弧形
     * @param  arcHeight 弧形高度
     * @param  arcPosition 弧形方向
     * @param  arcShape 内部外部
     */
    fun drawArc(arcHeight : Int , arcShape: ArcShape , arcPosition: ArcPosition){
        this.arcHeight = arcHeight
        this.arcPosition = arcPosition
        this.arcShape = arcShape
        invalidate()
    }

    /**
     * @author LDD
     * @From   ArcView
     * @Date   2018/2/26 下午4:01
     * @Note   构建弧形Path
     * @param  arcPosition 方向
     * @param  arcShape 内部还是外部
     */
    private fun createClipPath(arcPosition:ArcPosition , arcShape: ArcShape): Path {
        val path = Path()

        when (arcPosition) {
            ArcPosition.bottom -> {
                if (arcShape === ArcShape.inSide) {
                    path.moveTo(0f, 0f)
                    path.lineTo(0f, height.toFloat())
                    path.quadTo((width / 2).toFloat(), (height - 2 * arcHeight).toFloat(), width.toFloat(), height.toFloat())
                    path.lineTo(width.toFloat(), 0f)
                    path.close()
                } else {
                    path.moveTo(0f, 0f)
                    path.lineTo(0f, (height - arcHeight).toFloat())
                    path.quadTo((width / 2).toFloat(), (height + arcHeight).toFloat(), width.toFloat(), (height - arcHeight).toFloat())
                    path.lineTo(width.toFloat(), 0f)
                    path.close()
                }
            }
            ArcPosition.top -> if (arcShape === ArcShape.inSide) {
                path.moveTo(0f, height.toFloat())
                path.lineTo(0f, 0f)
                path.quadTo((width / 2).toFloat(), (2 * arcHeight).toFloat(), width.toFloat(), 0F)
                path.lineTo(width.toFloat(), height.toFloat())
                path.close()
            } else {
                path.moveTo(0f, arcHeight.toFloat())
                path.quadTo((width / 2).toFloat(), (-arcHeight).toFloat(), width.toFloat(), arcHeight.toFloat())
                path.lineTo(width.toFloat(), height.toFloat())
                path.lineTo(0F, height.toFloat())
                path.close()
            }
            ArcPosition.left -> if (arcShape === ArcShape.inSide) {
                path.moveTo(width.toFloat(), 0f)
                path.lineTo(0f, 0f)
                path.quadTo((arcHeight * 2).toFloat(), (height / 2).toFloat(), 0F, height.toFloat())
                path.lineTo(width.toFloat(), height.toFloat())
                path.close()
            } else {
                path.moveTo(width.toFloat(), 0f)
                path.lineTo(arcHeight.toFloat(), 0f)
                path.quadTo((-arcHeight).toFloat(), (height / 2).toFloat(), arcHeight.toFloat(), height.toFloat())
                path.lineTo(width.toFloat(), height.toFloat())
                path.close()
            }
            ArcPosition.right -> if (arcShape === ArcShape.inSide) {
                path.moveTo(0f, 0f)
                path.lineTo(width.toFloat(), 0f)
                path.quadTo((width - arcHeight * 2).toFloat(), (height / 2).toFloat(), width.toFloat(), height.toFloat())
                path.lineTo(0f, height.toFloat())
                path.close()
            } else {
                path.moveTo(0f, 0f)
                path.lineTo((width - arcHeight).toFloat(), 0f)
                path.quadTo((width + arcHeight).toFloat(), (height / 2).toFloat(), (width - arcHeight).toFloat(), height.toFloat())
                path.lineTo(0f, height.toFloat())
                path.close()
            }
        }

        return path
    }

    /**
     * @author LDD
     * @Date   2018/2/27 上午9:12
     * @From   ArcView
     * @Note   方向选择枚举
     */
    enum class ArcPosition {
        top,
        bottom,
        left,
        right
    }

    /**
     * @author LDD
     * @Date   2018/2/27 上午9:13
     * @From   ArcView
     * @Note   外部选择枚举
     */
    enum class ArcShape {
        inSide, outSide
    }
}