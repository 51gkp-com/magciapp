package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @author LDD
 * @Date   2018/4/12 下午4:09
 * @From   com.enation.javashop.android.lib.widget
 * @Note   对话框Dilaog背景View
 */
class DialogBackGroundView : View {

    /**
     * @Name  paint
     * @Type  Paint
     * @Note  画笔
     */
    private val paint = Paint()

    /**构造方法*/
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * @author LDD
     * @From   DialogBackGroundView
     * @Date   2018/4/12 下午3:53
     * @Note   类初始化方法
     */
    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**设置画笔颜色*/
        paint.color = Color.parseColor("#bb000000")
        /**设置粗体*/
        paint.typeface = Typeface.DEFAULT_BOLD
        /**设置抗锯齿*/
        paint.isAntiAlias = true
        /**矩形轮廓*/
        val rectrf = RectF(0f, height.toFloat()/20, width.toFloat(), height.toFloat())
        /**画矩形并且设置圆角*/
        canvas.drawRoundRect(rectrf, (width/30).toFloat(), (height/30).toFloat(), paint)
        /**初始化Patn*/
        val path = Path()
        /**移动到适合的点*/
        path.moveTo((width - height.toFloat()*0.1).toFloat(),0f)
        /**画线*/
        path.lineTo((width - height.toFloat()*0.1 + height.toFloat()/20).toFloat(),height.toFloat()/20+0.2f)
        path.lineTo((width - height.toFloat()*0.1 - height.toFloat()/20).toFloat(),height.toFloat()/20+0.2f)
        /**闭合路径*/
        path.close()
        /**画Path*/
        canvas.drawPath(path,paint)
        paint.reset()
    }

}