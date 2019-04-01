package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.utils.then

/**
 * @author LDD
 * @Date   2018/3/5 下午12:47
 * @From   com.enation.javashop.android.lib.widget
 * @Note   绘制背景线条的View
 */
class LineView : View{

    /**
     * @Name  hornHieght
     * @Type  Float
     * @Note  角高度
     */
    private var hornHieght = 0.1f

    /**
     * @Name  linePosition
     * @Type  Int
     * @Note  Line坐标
     */
    private var linePosition =3

    /**
     * @Name  linePadding
     * @Type  lineHeight
     * @Note  线绘制高度
     */
    var lineHeight = 0.8f

    /**
     * @Name  PositionTop
     * @Type  Int
     * @Note  顶部对齐
     */
    private val PositionTop = 1

    /**
     * @Name  PositionBottom
     * @Type  Int
     * @Note  底部对齐
     */
    private val PositionBottom = 2

    /**
     * @Name  PositionCenter
     * @Type  Int
     * @Note  中心对戏
     */
    private val PositionCenter = 3

    /**
     * @Name  lineColor
     * @Type  Int
     * @Note  线条颜色
     */
    private var lineColor :Int  =Color.BLACK

    /**
     * @Name  topPosition
     * @Type  Float
     * @Note  顶部position
     */
    private val topPosition :Float by lazy {
        var y = 0f
        when (linePosition) {
            PositionBottom -> y = height - height*lineHeight
            PositionCenter -> y = (height-height*lineHeight)/2
            PositionTop -> y = 0f
        }
        return@lazy y
    }

    /**
     * @Name  bottomPosition
     * @Type  Float
     * @Note  底部Position
     */
    private val bottomPosition:Float by lazy {
        var y = height.toFloat()
        when (linePosition) {
            PositionCenter -> y = height*lineHeight+(height-height*lineHeight)/2
            PositionTop -> y = (height-height*lineHeight)/2
        }
        return@lazy y
    }

    /**
     * @Name  hornCenterPostion
     * @Type  Float
     * @Note  中心店
     */
    private val hornCenterPostion:Float by lazy {
        var y = 0f
        when (linePosition) {
            PositionCenter -> y = (height/2).toFloat()
            PositionTop -> y = height*lineHeight/2
            PositionBottom -> y = (height/2)+(height-height*lineHeight)/2
        }
            return@lazy y
    }


    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) :this(context,attrs,0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LineView)
        lineHeight = typeArray.getFloat(R.styleable.LineView_line_height,0.8f)
        hornHieght = typeArray.getFloat(R.styleable.LineView_horn_height,0.1f)
        linePosition = typeArray.getInt(R.styleable.LineView_line_position,3)
        lineColor = typeArray.getColor(R.styleable.LineView_line_color,Color.BLACK)
        typeArray.recycle()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    /**
     * @author LDD
     * @From   LineView
     * @Date   2018/3/5 下午4:52
     * @Note   绘制UI
     */
    override fun onDraw(canvas: Canvas) {
        var paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (width/40).toFloat()
        paint.isAntiAlias = true
        paint.color = lineColor
        paint.strokeJoin = Paint.Join.ROUND
        canvas.drawPath(createPath(), paint)
    }


    /**
     * @author LDD
     * @From   LineView
     * @Date   2018/3/5 下午4:51
     * @Note   构建Path
     * @return Path
     */
    fun createPath():Path{
        return Path().then {
            path ->
            path.moveTo((width/20).toFloat(),topPosition)
            path.lineTo((width/20).toFloat(),hornCenterPostion-hornHieght*height/2)
            path.moveTo((width/20).toFloat(),hornCenterPostion-hornHieght*height/2)
            path.lineTo(hornHieght*height*0.7f+(width/20f),hornCenterPostion)
            path.moveTo(hornHieght*height*0.7f+(width/20f),hornCenterPostion)
            path.lineTo((width/20).toFloat(),hornCenterPostion+hornHieght*height/2)
            path.moveTo((width/20).toFloat(),hornCenterPostion+hornHieght*height/2)
            path.lineTo((width/20).toFloat(),bottomPosition)
            path.close()
        }
    }

}