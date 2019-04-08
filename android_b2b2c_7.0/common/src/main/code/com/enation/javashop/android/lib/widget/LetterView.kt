package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.enation.javashop.android.lib.utils.errorLog


/**
 * @author LDD
 * @Date   2018/3/23 上午10:54
 * @From   com.enation.javashop.android.lib.widget
 * @Note   首字母快速索引View
 */
class LetterView : View {

    /**
     * @Name  onTouchingLetterChangedListener
     * @Type  Block
     * @Note  触摸到字母时的回调
     */
    private var onTouchingLetterChangedListener: ((letter: String) -> Unit)? = null

    /**
     * @Name  letterList
     * @Type  ArrayList<String>
     * @Note  首字母集合
     */
    private var letterList = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")

    /**
     * @Name  choose
     * @Type  Int
     * @Note  选中字母下标 为-1代表未选中
     */
    private var choose = -1

    /**
     * @Name  paint
     * @Type  Paint
     * @Note  画笔
     */
    private val paint = Paint()

    /**构造事件*/
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    /**======*/

    /**
     * @author LDD
     * @From   LetterView
     * @Date   2018/3/23 上午10:59
     * @Note   设置自定义首字母集合
     * @param  letters 首字母集合
     */
    fun setLetter(letters: ArrayList<String>) {
        letterList = letters
        invalidate()
    }

    /**
     * @author LDD
     * @From   LetterView
     * @Date   2018/3/23 上午11:00
     * @Note   重写Draw方法绘制UI
     * @param  canvas 画布
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**获取单个字母高度 总高度/字母个数*/
        val singleHeight = height / letterList.size

        /**设置粗体*/
        paint.typeface = Typeface.DEFAULT_BOLD
        /**设置抗锯齿*/
        paint.isAntiAlias = true

        /**循环绘制首字母*/
        for (i in letterList.indices) {
            /**设置选中的字母为选中色*/
            if (i == choose) {
                paint.color = Color.parseColor("#f55158")
                paint.isFakeBoldText = true
                /**设置字体大小*/
                paint.textSize = (width * 0.65).toFloat()
            }else{
                /**设置画笔颜色*/
                paint.color = Color.BLACK
                paint.isFakeBoldText = false
                /**设置字体大小*/
                paint.textSize = (width * 0.5).toFloat()
            }
            /**设置文字绘制坐标*/
            val xPos = width / 2 - paint.measureText(letterList[i]) / 2
            val yPos = (singleHeight * i + singleHeight / 2).toFloat()

            /**绘制文字*/
            canvas.drawText(letterList[i], xPos, yPos, paint)

        }

        /**重置画笔*/
        paint.reset()
    }

    /**
     * @author LDD
     * @From   LetterView
     * @Date   2018/3/23 上午11:08
     * @Note   重新监听触摸事件
     * @param  event 事件
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        /**获取触摸Y坐标*/
        val y = event.y
        /**获取上次选中坐标*/
        val oldChoose = choose
        /**点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数*/
        val c = (y / height * letterList.size).toInt()
        /**当action为down时设置设置相应 设置选中下标 并且绘制*/

        if (oldChoose != c) {
            if (c >= 0 && c < letterList.size) {
                onTouchingLetterChangedListener?.invoke(letterList[c])
                choose = c
                invalidate()
            }
        }

        return true
    }

    /**
     * @author LDD
     * @From   LetterView
     * @Date   2018/3/23 上午11:13
     * @Note   设置触摸事件
     */
    fun setOnTouchingLetterChangedListener(onTouchLetterCallBack: ((letter: String) -> Unit)) {
        this.onTouchingLetterChangedListener = onTouchLetterCallBack
    }
}