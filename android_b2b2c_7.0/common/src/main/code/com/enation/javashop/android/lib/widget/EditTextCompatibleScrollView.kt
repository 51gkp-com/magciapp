package com.enation.javashop.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText


/**
 * @author LDD
 * @Date   2018/4/24 下午1:23
 * @From   com.enation.javashop.android.lib.widget
 * @Note   兼容滑动视图嵌套的EditText
 */
class EditTextCompatibleScrollView :EditText {


    /**
     * @Name  mOffsetHeight
     * @Type  Int
     * @Note  滑动距离的最大边界
     */
    private var mOffsetHeight: Int = 0

    /**
     * @Name  mBottomFlag
     * @Type  Boolean
     * @Note  是否到顶或者到底的标志
     */
    private var mBottomFlag = false

    /**构造*/
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * @author LDD
     * @From   EditTextCompatibleScrollView
     * @Date   2018/4/24 下午1:24
     * @Note   测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val mLayout = layout
        val paddingTop: Int = totalPaddingTop
        val paddingBottom: Int = totalPaddingBottom
        val mHeight: Int = height
        val mLayoutHeight: Int = mLayout.height

        //计算滑动距离的边界
        mOffsetHeight = mLayoutHeight + paddingTop + paddingBottom - mHeight
    }


    /**
     * @author LDD
     * @Date   2018/4/24 下午1:25
     * @From   EditTextCompatibleScrollView
     * @Note   消耗触摸事件
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN)
        //如果是新的按下事件，则对mBottomFlag重新初始化
            mBottomFlag = false
        //如果已经不要这次事件，则传出取消的信号，这里的作用不大
        if (mBottomFlag)
            event.action = MotionEvent.ACTION_CANCEL
        return super.dispatchTouchEvent(event)
    }

    /**
     * @author LDD
     * @From    EditTextCompatibleScrollView
     * @Date   2018/4/24 下午1:25
     * @Note   触摸处理
     */
    @SuppressLint("WrongConstant")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        if (!mBottomFlag)
            parent.requestDisallowInterceptTouchEvent(true)
        return result
    }

    /**
     * @author LDD
     * @From   EditTextCompatibleScrollView
     * @Date   2018/4/24 下午1:27
     * @Note   滑动时的处理
     */
    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        if (vert == mOffsetHeight || vert == 0) {
            //这里触发父布局或祖父布局的滑动事件
            parent.requestDisallowInterceptTouchEvent(false)
            mBottomFlag = true
        }
    }



}