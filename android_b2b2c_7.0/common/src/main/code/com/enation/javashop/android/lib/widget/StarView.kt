package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.base.BaseControl
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.to


/**
 * @author LDD
 * @Date   2018/3/28 下午3:59
 * @From   com.enation.javashop.android.lib.widget
 * @Note   心形等级自定义View
 */
class StarView :View{

    private var star = 0

    private var nomalBitmap :Bitmap

    private var starEvent :((Int) ->Unit)? = null

    private var selectedBitmap :Bitmap

    private val paint by lazy {
        return@lazy Paint().then {
            self ->
            self.isAntiAlias = true
        }
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/3/28 下午4:00
     * @Note   星级自定义View构造方法
     */
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.StarView)
        star = typeArray?.getInt(R.styleable.StarView_star,5) ?: 5

        if (typeArray?.hasValue(R.styleable.StarView_selectImage)!!){
            selectedBitmap = drawableToBitmap(typeArray.getDrawable(R.styleable.StarView_selectImage))
        }else{
            selectedBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.javashop_icon_star_selected)
        }
        if (typeArray.hasValue(R.styleable.StarView_nomalImage)){
            nomalBitmap = drawableToBitmap(typeArray.getDrawable(R.styleable.StarView_nomalImage))
        }else{
            nomalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.javashop_icon_star_nomal)
        }

        if(context is BaseControl){
            context.addLifeCycleListener { 
                state ->
                if (state == LIFE_CYCLE_DESTORY){
                    selectedBitmap.recycle()
                    nomalBitmap.recycle()
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:27
     * @Note   设置星级
     * @param  star 星级
     */
    fun setStar(star :Int){
        this.star = star
        invalidate()
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:27
     * @Note   设置星级变化监听
     * @param  event 回调
     */
    fun setStarEvent(event :(Int) ->Unit){
        starEvent = event
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:28
     * @Note   重写绘制
     * @param  canvas 画板
     */
    override fun onDraw(canvas: Canvas?) {
        var imageWidth = width/7
        var space = imageWidth/2
        if (imageWidth > height){
            imageWidth = height
            space = (width -(height*5))/4
        }
        nomalBitmap = setImgSize(nomalBitmap,imageWidth,imageWidth)
        selectedBitmap = setImgSize(selectedBitmap,imageWidth,imageWidth)

        var left = 0

        for (i in 0..4){
            if (i >= star){
                canvas?.drawBitmap(nomalBitmap, left.toFloat(), 0f, paint)
            }else{
                canvas?.drawBitmap(selectedBitmap, left.toFloat(), 0f, paint)
            }
            left += imageWidth+space
        }

        paint.reset()
    }


    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:28
     * @Note   在此做滑动操作
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (starEvent == null){
            return false
        }

        val x = event.x

        var width = width/7 + width/7/2

        val newStar = if (x < width/3){
            0
        }else{
            (x / width).toInt()+1
        }
        if (star != newStar){
            star = newStar
            invalidate()
            starEvent?.invoke(star)
        }
        return true
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:28
     * @Note   drawable转化Bitmap
     * @param  drawable drawable资源
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * @author LDD
     * @From   StarView
     * @Date   2018/5/18 上午10:29
     * @Note   重新设置Image大小
     * @param  bm  图片
     * @param  newHeight 新的高度
     * @param  newWidth 新的宽度
     */
    fun setImgSize(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高.
        val width = bm.width
        val height = bm.height
        // 计算缩放比例.
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片.
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }
}