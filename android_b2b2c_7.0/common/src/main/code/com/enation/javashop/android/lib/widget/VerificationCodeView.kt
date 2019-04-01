package com.enation.javashop.android.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.enation.javashop.android.lib.R
import com.enation.javashop.utils.base.tool.ScreenTool


/**
 * Created by LDD on 2018/5/9.
 */
class VerificationCodeView :RelativeLayout {

    private lateinit var containerEt: LinearLayout

    private lateinit var et: EditText
    // 输入框数量
    private var mEtNumber: Int = 0
    // 输入框的宽度
    private var mEtWidth: Int = 0
    //输入框分割线
    private var mEtDividerDrawable: Drawable? = null
    //输入框文字颜色
    private var mEtTextColor: Int = 0
    //输入框文字大小
    private var mEtTextSize: Float = 0.toFloat()
    //输入框获取焦点时背景
    private var mEtBackgroundDrawableFocus: Drawable? = null
    //输入框没有焦点时背景
    private var mEtBackgroundDrawableNormal: Drawable? = null
    //是否是密码模式
    private var mEtPwd: Boolean = false
    //密码模式时圆的半径
    private var mEtPwdRadius: Float = 0.toFloat()

    private var heightPercent :Float = -1f

    //存储TextView的数据 数量由自定义控件的属性传入
    private lateinit var mPwdTextViews: Array<PwdTextView>

    private val myTextWatcher = MyTextWatcher()


    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context, attrs, defStyleAttr)
    }



    //初始化 布局和属性
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.layout_identifying_code, this)
        containerEt = this.findViewById(R.id.container_et) as LinearLayout
        et = this.findViewById(R.id.et) as EditText

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyleAttr, 0)
        mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeView_vcode_number, 1)
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vcode_width, -1)
        mEtDividerDrawable = typedArray.getDrawable(R.styleable.VerificationCodeView_vcode_divider_drawable)
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vcode_text_size, -1).toFloat()
        mEtTextColor = typedArray.getColor(R.styleable.VerificationCodeView_vcode_text_color, Color.BLACK)
        mEtBackgroundDrawableFocus = typedArray.getDrawable(R.styleable.VerificationCodeView_vcode_bg_focus)
        mEtBackgroundDrawableNormal = typedArray.getDrawable(R.styleable.VerificationCodeView_vcode_bg_normal)
        mEtPwd = typedArray.getBoolean(R.styleable.VerificationCodeView_vcode_pwd, false)
        mEtPwdRadius = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vcode_pwd_radius, 0).toFloat()
        heightPercent = typedArray.getFloat(R.styleable.VerificationCodeView_vcode_height_percent,-1f)
        //释放资源
        typedArray.recycle()


        // 当xml中未配置时 这里进行初始配置默认图片
        if (mEtDividerDrawable == null) {
            mEtDividerDrawable = ContextCompat.getDrawable(context,R.drawable.shape_divider_identifying)
        }

        if (mEtBackgroundDrawableFocus == null) {
            mEtBackgroundDrawableFocus =ContextCompat.getDrawable(context,R.drawable.shape_icv_et_bg_focus)
        }

        if (mEtBackgroundDrawableNormal == null) {
            mEtBackgroundDrawableNormal =ContextCompat.getDrawable(context,R.drawable.shape_icv_et_bg_normal)
        }

        initUI()
    }

    // 初始UI
    private fun initUI() {
        initTextViews(context, mEtNumber, mEtWidth, mEtDividerDrawable, mEtTextSize, mEtTextColor)
        initEtContainer(mPwdTextViews)
        setListener()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置当 高为 warpContent 模式时的默认值 为 50dp
        var mHeightMeasureSpec = heightMeasureSpec

        val heightMode = View.MeasureSpec.getMode(mHeightMeasureSpec)
        if (heightMode == View.MeasureSpec.AT_MOST) {
            mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(dp2px(50f, context).toInt(), View.MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec)
    }


    //初始化TextView
    private fun initTextViews(context: Context, etNumber: Int, etWidth: Int, etDividerDrawable: Drawable?, etTextSize: Float, etTextColor: Int) {
        // 设置 editText 的输入长度
        et.isCursorVisible = false//将光标隐藏
        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(etNumber)) //最大输入长度
        // 设置分割线的宽度
        if (etDividerDrawable != null) {
            etDividerDrawable.setBounds(0, 0, etDividerDrawable.minimumWidth, etDividerDrawable.minimumHeight)
            containerEt.dividerDrawable = etDividerDrawable
        }

        mPwdTextViews = Array(etNumber,{PwdTextView(context)})

        for (i in mPwdTextViews.indices) {
            val textView = PwdTextView(context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, if (etTextSize == -1f){
                ((ScreenTool.getScreenHeight(context)*heightPercent)*0.7).toFloat()
            }else{etTextSize})
            textView.setTextColor(etTextColor)
            textView.width = if (etWidth == -1){
                (ScreenTool.getScreenHeight(context)*heightPercent).toInt()
            }else{
                etWidth
            }
            textView.height =  if (etWidth == -1){
                (ScreenTool.getScreenHeight(context)*heightPercent).toInt()
            }else{
                etWidth
            }
            if (i == 0) {
                textView.background = mEtBackgroundDrawableFocus
            } else {
                textView.background = mEtBackgroundDrawableNormal
            }
            textView.gravity = Gravity.CENTER

            textView.isFocusable = false

            mPwdTextViews[i] = textView
        }
    }

    //初始化存储TextView 的容器
    private fun initEtContainer(mTextViews: Array<PwdTextView>) {
        for (mTextView in mTextViews) {
            containerEt.addView(mTextView)
        }
    }


    private fun setListener() {
        // 监听输入内容
        et.addTextChangedListener(myTextWatcher)

        // 监听删除按键
        et.setOnKeyListener(OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                onKeyDelete()
                return@OnKeyListener true
            }
            false
        })
    }


    // 给TextView 设置文字
    private fun setText(inputContent: String) {

        for (i in mPwdTextViews.indices) {
            val tv = mPwdTextViews[i]
            if (tv.text.toString().trim() == "") {
                if (mEtPwd) {
                    tv.drawPwd(mEtPwdRadius)
                }
                tv.text = inputContent
                // 添加输入完成的监听
                if (inputCompleteListener != null && i == mPwdTextViews.size-1) {
                    inputCompleteListener!!.inputComplete()
                }
                tv.background = mEtBackgroundDrawableNormal
                if (i < mEtNumber - 1) {
                    mPwdTextViews[i + 1].background = mEtBackgroundDrawableFocus
                }
                break
            }
        }
    }

    // 监听删除
    private fun onKeyDelete() {
        for (i in mPwdTextViews.indices.reversed()) {
            val tv = mPwdTextViews[i]
            if (tv.text.toString().trim() != "") {
                if (mEtPwd) {
                    tv.clearPwd()
                }
                tv.text = ""
                // 添加删除完成监听
                if (inputCompleteListener != null) {
                    inputCompleteListener!!.deleteContent()
                }
                tv.background = mEtBackgroundDrawableFocus
                if (i < mEtNumber - 1) {
                    mPwdTextViews[i + 1].background = mEtBackgroundDrawableNormal
                }
                break
            }
        }
    }


    /**
     * 获取输入文本
     *
     * @return string
     */
    fun getInputContent(): String {
        val buffer = StringBuffer()
        for (tv in mPwdTextViews) {
            buffer.append(tv.text.toString().trim())
        }
        return buffer.toString()
    }

    /**
     * 删除输入内容
     */
    fun clearInputContent() {
        for (i in mPwdTextViews.indices) {
            if (i == 0) {
                mPwdTextViews[i].background = mEtBackgroundDrawableFocus
            } else {
                mPwdTextViews[i].background = mEtBackgroundDrawableNormal
            }
            if (mEtPwd) {
                mPwdTextViews[i].clearPwd()
            }
            mPwdTextViews[i].text = ""
        }
    }

    /**
     * 设置输入框个数
     *
     * @param etNumber
     */
    fun setEtNumber(etNumber: Int) {
        this.mEtNumber = etNumber
        et.removeTextChangedListener(myTextWatcher)
        containerEt.removeAllViews()
        initUI()
    }


    /**
     * 获取输入的位数
     *
     * @return int
     */
    fun getEtNumber(): Int {
        return mEtNumber
    }


    /**
     * 设置是否是密码模式 默认不是
     *
     * @param isPwdMode
     */
    fun setPwdMode(isPwdMode: Boolean) {
        this.mEtPwd = isPwdMode
    }


    /**
     * 获取输入的EditText 用于外界设置键盘弹出
     *
     * @return
     */
    fun getEditText(): EditText? {
        return et
    }

    // 输入完成 和 删除成功 的监听
    private var inputCompleteListener: InputCompleteListener? = null

    fun setInputCompleteListener(inputCompleteListener: InputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener
    }


    interface InputCompleteListener {
        fun inputComplete()

        fun deleteContent()
    }


    fun dp2px(dpValue: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.resources.displayMetrics)
    }

    fun sp2px(spValue: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.resources.displayMetrics)
    }


    private inner class MyTextWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            val inputStr = editable.toString()
            if (!TextUtils.isEmpty(inputStr)) {
                setText(inputStr)
                et!!.setText("")
            }
        }
    }

    private inner class PwdTextView : AppCompatTextView {

        private var radius: Float = 0.toFloat()

        private var hasPwd: Boolean = false

        constructor(context: Context) : this(context,null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            if (hasPwd) {
                // 画一个黑色的圆
                val paint = Paint(ANTI_ALIAS_FLAG)
                paint.color = Color.BLACK
                paint.style = Paint.Style.FILL
                canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
            }
        }


        fun clearPwd() {
            this.hasPwd = false
            invalidate()
        }


        fun drawPwd(radius: Float) {
            this.hasPwd = true
            if (radius == 0f) {
                this.radius = (width / 4).toFloat()
            } else {
                this.radius = radius
            }
            invalidate()
        }


    }
}