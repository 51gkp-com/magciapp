package com.enation.javashop.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.annotation.IntRange
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.enation.javashop.android.lib.R
import android.support.constraint.ConstraintSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.utils.base.tool.ScreenTool
import com.enation.javashop.utils.base.tool.SystemTool


/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.widget
 * @Date   2018/3/7 上午10:14
 * @Note   通用ActionBar
 */
class CommonActionBar : ConstraintLayout {

    /**
     * @Name  holderColor
     * @Type  Int
     * @Note  顶部Holder的颜色
     */
    private var holderColor: Int = -1

    /**
     * @Name  backgroundViewColor
     * @Type  Int
     * @Note  背景颜色
     */
    private var backgroundViewColor: Int = -1

    /**
     * @Name  titleText
     * @Type  String
     * @Note  标题
     */
    private lateinit var titleText: String

    /**
     * @Name  leftText
     * @Type  String
     * @Note  左侧文字
     */
    private lateinit var leftText: String

    /**
     * @Name  leftImageDrawable
     * @Type  Drawable
     * @Note  左侧图片
     */
    private var leftImageDrawable: Drawable? = null

    /**
     * @Name  rightText
     * @Type  String
     * @Note  右侧文字
     */
    private lateinit var rightText: String

    /**
     * @Name  rightImageDrawable
     * @Type  Drawable
     * @Note  右侧ImageView
     */
    private var rightImageDrawable: Drawable? = null

    /**
     * @Name  holderVisibility
     * @Type  Boolean
     * @Note  holder是否显示
     */
    private var holderVisibility: Boolean = true

    /**
     * @Name  titleImageDrawable
     * @Type  Drawable
     * @Note  标题图片
     */
    private var titleImageDrawable: Drawable? = null

    /**
     * @Name  backgroundImageDrawable
     * @Type  Drawable
     * @Note  北京图片
     */
    private var backgroundImageDrawable: Drawable? = null

    /**
     * @Name  leftTextColor
     * @Type  Int
     * @Note  左侧文字颜色
     */
    private var leftTextColor :Int = -1

    /**
     * @Name  rightTextColor
     * @Type  Int
     * @Note  右侧文字颜色
     */
    private var rightTextColor :Int = -1

    /**
     * @Name  titleTextColor
     * @Type  Int
     * @Note  标题文字颜色
     */
    private var titleTextColor:Int = -1

    /**View ID 常量 =========================*/
    private val TopHolderId = 222
    private val TitleTextId = 333
    private val TitleImageId = 444
    private val LeftTextId = 555
    private val LeftImageId = 666
    private val LeftTouchId = 777
    private val RightTextId = 888
    private val RightImageId = 999
    private val RightTouchViewId = 1111
    private val BackgroundImageViewId = 2222
    private val BottomLineId =3333
    /**=====================================*/

    /**
     * @Name  applyConstraintSet
     * @Type  ConstraintSet
     * @Note  constraint布局辅助类
     */
    private val applyConstraintSet = ConstraintSet()

    /**
     * @Name  holderView
     * @Type  View
     * @Note  holder占位View
     */
    private val holderView by lazy {
        return@lazy View(context).then { self ->
            self.id = TopHolderId
            self.setBackgroundColor(holderColor)
            self.visibility = holderVisibility.judge(View.VISIBLE,View.INVISIBLE)
        }
    }

    /**
     * @Name  backgroundImageView
     * @Type  ImageView
     * @Note  背景ImageView
     */
    private val backgroundImageView by lazy {
        return@lazy ImageView(context).then { self ->
            self.id = BackgroundImageViewId
            self.setBackgroundColor(Color.TRANSPARENT)
            self.scaleType = ImageView.ScaleType.CENTER_CROP
            backgroundImageDrawable.haveDo {
                self.setImageDrawable(backgroundImageDrawable)
            }
            setBackgroundColor((backgroundViewColor == -1).judge(Color.WHITE,backgroundViewColor))
        }
    }

    /**
     * @Name  titleTextView
     * @Type  TextView
     * @Note  左侧TextView
     */
    private val titleTextView by lazy {
        return@lazy  (LayoutInflater.from(context).inflate(R.layout.auto_size_tv,null) as TextView ).then { self ->
            self.id = TitleTextId
            self.gravity = Gravity.CENTER
            self.visibility = View.INVISIBLE
            self.text = titleText
            self.setTextColor(titleTextColor)
        }
    }

    /**
     * @Name  titleImageView
     * @Type  ImageView
     * @Note  标题ImageView
     */
    private val titleImageView by lazy {
        return@lazy ImageView(context).then { self ->
            self.id = TitleImageId
            self.setBackgroundColor(Color.parseColor("#00000000"))
            self.scaleType = ImageView.ScaleType.FIT_CENTER
            self.visibility = View.INVISIBLE
            if (titleImageDrawable != null){
                self.visibility = View.VISIBLE
                self.setImageDrawable(titleImageDrawable)
            }
        }
    }

    /**
     * @Name  leftTextView
     * @Type  TextView
     * @Note  左侧TextView
     */
    private val leftTextView by lazy {
        return@lazy (LayoutInflater.from(context).inflate(R.layout.auto_size_tv,null) as TextView ).then { self ->
            self.id = LeftTextId
            self.gravity = Gravity.CENTER
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(self, 10, 100, 2, TypedValue.COMPLEX_UNIT_SP)
            TextViewCompat.setAutoSizeTextTypeWithDefaults(self, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            self.visibility = View.INVISIBLE
            self.text = leftText
            self.setTextColor(leftTextColor)
        }
    }
    /**
     * @Name  leftImageView
     * @Type  ImageView
     * @Note  左侧ImageView
     */
    private val leftImageView by lazy {
        return@lazy ImageView(context).then { self ->
            self.id = LeftImageId
            self.visibility = View.INVISIBLE
            leftImageDrawable.haveDo {
                self.setImageDrawable(leftImageDrawable)
            }
        }
    }
    /**
     * @Name  leftTouchView
     * @Type  View
     * @Note  左侧点击视图
     */
    private val leftTouchView by lazy {
        return@lazy View(context).then { self ->
            self.id = LeftTouchId
        }
    }
    /**
     * @Name  rightTextView
     * @Type  TextView
     * @Note  右侧TextView
     */
    private val rightTextView by lazy {
        return@lazy (LayoutInflater.from(context).inflate(R.layout.auto_size_tv,null) as TextView ).then { self ->
            self.id = RightTextId
            self.gravity = Gravity.CENTER
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(self, 10, 100, 2, TypedValue.COMPLEX_UNIT_SP)
            TextViewCompat.setAutoSizeTextTypeWithDefaults(self, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            self.visibility = View.INVISIBLE
            self.text = rightText
            self.setTextColor(rightTextColor)
        }
    }
    /**
     * @Name  rightImageView
     * @Type  ImageView
     * @Note  右侧ImageView
     */
    private val rightImageView by lazy {
        return@lazy ImageView(context).then { self ->
            self.id = RightImageId
            self.visibility = View.INVISIBLE
            rightImageDrawable.haveDo {
                self.setImageDrawable(rightImageDrawable)
            }
        }
    }

    /**
     * @Name  rightTouchView
     * @Type  View
     * @Note  右侧点击视图
     */
    private val rightTouchView by lazy {
        return@lazy View(context).then { self ->
            self.id = RightTouchViewId
        }
    }

    /**
     * @Name  bottomLine
     * @Type  View
     * @Note  底部Line
     */
    private val bottomLine by lazy {
        return@lazy View(context).then { self ->
            self.id = BottomLineId
            self.setBackgroundColor(Color.parseColor("#e4e6e5"))
        }
    }

    /**
     * @Name  imageLoader
     * @Type  (url: String, imageView: ImageView) -> Unit
     * @Note  图片加载器
     */
    private var imageLoader:(url: String, imageView: ImageView) -> Unit = { url : String, iv : ImageView ->
        Glide.with(context)
                .load(url)
                .thumbnail(0.1f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv)
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setBackgroundColor(Color.TRANSPARENT)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonActionBar)
        parameterInit(typedArray)
        layoutInit()
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/3/7 上午10:14
     * @Note   参数初始化
     */
    private fun parameterInit(typedArray: TypedArray) {
        holderColor = typedArray.getColor(R.styleable.CommonActionBar_holder_color, Color.parseColor("#ff484848"))
        leftTextColor = typedArray.getColor(R.styleable.CommonActionBar_left_text_color, Color.parseColor("#6e6c6f"))
        rightTextColor = typedArray.getColor(R.styleable.CommonActionBar_right_text_color, Color.parseColor("#6e6c6f"))
        titleTextColor = typedArray.getColor(R.styleable.CommonActionBar_title_text_color, Color.parseColor("#000000"))
        titleText = typedArray.getString(R.styleable.CommonActionBar_title_text) ?: ""
        leftText = typedArray.getString(R.styleable.CommonActionBar_left_text) ?: ""
        leftImageDrawable = typedArray.getDrawable(R.styleable.CommonActionBar_left_image)
        rightText = typedArray.getString(R.styleable.CommonActionBar_right_text) ?: ""
        rightImageDrawable = typedArray.getDrawable(R.styleable.CommonActionBar_right_image)
        holderVisibility = typedArray.getBoolean(R.styleable.CommonActionBar_holder_visibility, true)
        titleImageDrawable = typedArray.getDrawable(R.styleable.CommonActionBar_title_image)
        backgroundImageDrawable = typedArray.getDrawable(R.styleable.CommonActionBar_background_image)
        backgroundViewColor = typedArray.getColor(R.styleable.CommonActionBar_background_color,-1)
        typedArray.recycle()
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/3/7 下午1:25
     * @Note   布局
     */
    @SuppressLint("ResourceType")
    private fun layoutInit() {
        applyConstraintSet.clone(this)

        addView(backgroundImageView).then {
            applyConstraintSet.connect(BackgroundImageViewId, ConstraintSet.LEFT, id, ConstraintSet.LEFT, 0)
            applyConstraintSet.connect(BackgroundImageViewId, ConstraintSet.RIGHT, id, ConstraintSet.RIGHT, 0)
            applyConstraintSet.connect(BackgroundImageViewId, ConstraintSet.TOP, id, ConstraintSet.TOP, 0 )
            applyConstraintSet.connect(BackgroundImageViewId, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 0)
            applyConstraintSet.applyTo(this)
        }
        addView(holderView).then {
            applyConstraintSet.connect(TopHolderId, ConstraintSet.LEFT, id, ConstraintSet.LEFT, 0)
            applyConstraintSet.connect(TopHolderId, ConstraintSet.RIGHT, id, ConstraintSet.RIGHT, 0)
            applyConstraintSet.connect(TopHolderId, ConstraintSet.TOP, id, ConstraintSet.TOP, 0)
            applyConstraintSet.constrainWidth(TopHolderId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(TopHolderId, AppTool.SystemUI.getStatusBarHeight())
            applyConstraintSet.applyTo(this)
        }
        addView(leftTextView).then {
            applyConstraintSet.connect(LeftTextId, ConstraintSet.LEFT, id, ConstraintSet.LEFT, ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(LeftTextId, ConstraintSet.TOP, holderView.id, ConstraintSet.BOTTOM, 0)
            applyConstraintSet.connect(LeftTextId, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 0)
            applyConstraintSet.constrainWidth(LeftTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(LeftTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(LeftTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(LeftTextId,0.4F)
            applyConstraintSet.setDimensionRatio(LeftTextId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }
        addView(leftImageView).then {
            applyConstraintSet.connect(LeftImageId,ConstraintSet.LEFT,id,ConstraintSet.LEFT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(LeftImageId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(LeftImageId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(LeftImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(LeftImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(LeftImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(LeftImageId,0.33f)
            applyConstraintSet.setDimensionRatio(LeftImageId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }

        addView(leftTouchView).then {
            applyConstraintSet.connect(LeftTouchId,ConstraintSet.LEFT,id,ConstraintSet.LEFT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(LeftTouchId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(LeftTouchId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(LeftTouchId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(LeftTouchId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(LeftTouchId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(LeftTouchId,0.65f)
            applyConstraintSet.setDimensionRatio(LeftTouchId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }

        addView(rightTextView).then {
            applyConstraintSet.connect(RightTextId, ConstraintSet.RIGHT, id, ConstraintSet.RIGHT, ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(RightTextId, ConstraintSet.TOP, holderView.id, ConstraintSet.BOTTOM, 0)
            applyConstraintSet.connect(RightTextId, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 0)
            applyConstraintSet.constrainWidth(RightTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(RightTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(RightTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(RightTextId,0.4F)
            applyConstraintSet.setDimensionRatio(RightTextId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }

        addView(rightImageView).then {
            applyConstraintSet.connect(RightImageId,ConstraintSet.RIGHT,id,ConstraintSet.RIGHT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(RightImageId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(RightImageId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(RightImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(RightImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(RightImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(RightImageId,0.33f)
            applyConstraintSet.setDimensionRatio(RightImageId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }

        addView(rightTouchView).then {
            applyConstraintSet.connect(RightTouchViewId,ConstraintSet.RIGHT,id,ConstraintSet.RIGHT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(RightTouchViewId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(RightTouchViewId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(RightTouchViewId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(RightTouchViewId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(RightTouchViewId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(RightTouchViewId,0.65f)
            applyConstraintSet.setDimensionRatio(RightTouchViewId,"w,1:1")
            applyConstraintSet.applyTo(this)
        }

        addView(titleTextView).then {
            applyConstraintSet.connect(TitleTextId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(TitleTextId,ConstraintSet.LEFT,LeftTouchId,ConstraintSet.RIGHT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(TitleTextId,ConstraintSet.RIGHT,RightTouchViewId,ConstraintSet.LEFT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(TitleTextId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(TitleTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(TitleTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(TitleTextId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(TitleTextId,0.33F)
            applyConstraintSet.applyTo(this)
        }

        addView(titleImageView).then{
            applyConstraintSet.connect(TitleImageId,ConstraintSet.TOP,TopHolderId,ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(TitleImageId,ConstraintSet.LEFT,LeftTouchId,ConstraintSet.RIGHT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(TitleImageId,ConstraintSet.RIGHT,RightTouchViewId,ConstraintSet.LEFT,ScreenTool.dip2px(context,10f))
            applyConstraintSet.connect(TitleImageId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(TitleImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(TitleImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainDefaultHeight(TitleImageId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainPercentHeight(TitleImageId,0.33F)
            applyConstraintSet.applyTo(this)
        }

        addView(bottomLine).then {
            applyConstraintSet.connect(BottomLineId,ConstraintSet.BOTTOM,id,ConstraintSet.BOTTOM,0)
            applyConstraintSet.constrainWidth(BottomLineId, ConstraintSet.MATCH_CONSTRAINT)
            applyConstraintSet.constrainHeight(BottomLineId, ScreenTool.dip2px(context,1f))
            applyConstraintSet.applyTo(this)
        }
    }

    /**
     * @author LDD
     * @Date   2018/1/31 下午4:56
     * @From   CommonActionBar
     * @Note   设置左侧资源图片
     * @param  imageRes 图片资源
     */
    fun setLeftImage(@DrawableRes imageRes:Int):CommonActionBar{
        leftImageView.setImageResource(imageRes)
        leftImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午4:59
     * @Note   设置左侧网络图片
     * @param  imageUrl 网络图片URL
     */
    fun setLeftImage(imageUrl:String):CommonActionBar{
        imageLoader(imageUrl,leftImageView)
        leftImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:00
     * @Note   设置右侧图片资源
     * @param  imageRes 图片资源
     */
    fun setRightImage(@DrawableRes imageRes:Int):CommonActionBar{
        rightImageView.setImageResource(imageRes)
        rightImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:00
     * @Note   设置右侧网络图片
     * @param  imageUrl 网络图片URL
     */
    fun setRightImage(imageUrl:String):CommonActionBar{
        imageLoader(imageUrl,rightImageView)
        rightImageView.visibility = View.VISIBLE
        return  this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:00
     * @Note   设置左侧文字
     * @param  text 文字
     */
    fun setLeftText(text:String):CommonActionBar{
        leftTextView.text = text
        leftTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:01
     * @Note   设置左侧文字颜色
     * @param  color 文字颜色
     */
    fun setLeftTextColor(@ColorRes color :Int):CommonActionBar{
        leftTextView.setTextColor(context.getColorCompatible(color))
        leftTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:02
     * @Note   设置右侧文字
     * @param  text 文字
     */
    fun setRightText(text:String):CommonActionBar{
        rightTextView.text = text
        rightTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:03
     * @Note   设置右侧文字颜色
     * @param  color 文字颜色
     */
    fun setRightTextColor(@ColorRes color :Int):CommonActionBar{
        rightTextView.setTextColor(context.getColorCompatible(color))
        rightTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:03
     * @Note   设置标题
     * @param  title 标题
     */
    fun setTitleText(title:String):CommonActionBar{
        titleText = title
        titleTextView.text = titleText
        titleTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置标题颜色
     * @param  color 标题颜色
     */
    fun setTitleColor(@ColorRes color :Int):CommonActionBar{
        titleTextView.setTextColor(context.getColorCompatible(color))
        titleTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置左侧文字大小
     * @param  sizeLevel 设置文字大小 0.0到10.0之间随意调
     */
    fun setLeftTextSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = leftTextView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        leftTextView.layoutParams = layoutParams
        leftTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置右侧文字大小
     * @param  sizeLevel 设置文字大小 0.0到10.0之间随意调
     */
    fun setRightTextSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = rightTextView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        rightTextView.layoutParams = layoutParams
        rightTextView.visibility = View.VISIBLE
        return this
    }
    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:06
     * @Note   设置背景图
     * @param  res 本地资源
     */
    fun setBackground(@DrawableRes res :Int):CommonActionBar{
        backgroundImageView.setImageResource(res)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:07
     * @Note   设置背景图 网络资源
     * @param  imageUrl 网络图片URL
     */
    fun setBackground(imageUrl :String):CommonActionBar{
        imageLoader(imageUrl,backgroundImageView)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:09
     * @Note   设置背景透明度  0-255区间  当背景为image时 无效
     * @param  aplha 透明度
     */
    fun setBackgroundAlpha(@IntRange(from = 0, to = 255) aplha:Int):CommonActionBar{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            backgroundImageView.imageAlpha = aplha
        }
        backgroundImageView.background?.alpha = aplha
        backgroundImageView.drawable?.alpha =aplha
        bottomLine.background.alpha = aplha
        background?.alpha = aplha
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/5/21 下午6:10
     * @Note   设置底线颜色
     * @param  color 颜色
     */
    fun setLineBgColor(color: Int):CommonActionBar{
        bottomLine.setBackgroundColor(color)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:10
     * @Note   设置topbar占位视图背景颜色
     * @param  res 颜色资源
     */
    fun setTopHolderColor(@DrawableRes res :Int):CommonActionBar{
        holderView.setBackgroundColor(res)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:12
     * @Note   设置占位View是否显示
     * @param  isVisible 是否显示
     */
    fun setTopHolderVisibility(isVisible: Boolean): CommonActionBar{
        holderView.visibility = isVisible.judge(View.VISIBLE,View.INVISIBLE)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置左侧图片大小
     * @param  sizeLevel 设置大小 0.0到10.0之间随意调
     */
    fun setLeftImageSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = leftImageView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        leftImageView.layoutParams = layoutParams
        leftImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置右侧图片大小
     * @param  sizeLevel 设置大小 0.0到10.0之间随意调
     */
    fun setRightImageSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = rightImageView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        rightImageView.layoutParams = layoutParams
        rightImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置标题文字大小
     * @param  sizeLevel 设置文字大小 0.0到10.0之间随意调
     */
    fun setTitleSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = titleTextView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        titleTextView.layoutParams = layoutParams
        titleTextView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:04
     * @Note   设置标题文字大小
     * @param  sizeLevel 设置文字大小 0.0到10.0之间随意调
     */
    fun setTitleImageSize(@FloatRange(from = 0.0,to = 10.0)sizeLevel:Float):CommonActionBar{
        var layoutParams = titleImageView.layoutParams.to<ConstraintLayout.LayoutParams>()
        layoutParams.matchConstraintPercentHeight = sizeLevel*0.1f*0.65f
        titleImageView.layoutParams = layoutParams
        titleImageView.visibility = View.VISIBLE
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/3/8 下午1:40
     * @Note   标题图片
     */
    fun setTitleImageRes(@DrawableRes res: Int):CommonActionBar{
        titleImageView.setImageResource(res)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/3/8 下午1:42
     * @Note   标题图片
     * @param  imageUrl 图片URL
     */
    fun setTitleNetImage(imageUrl : String):CommonActionBar{
        imageLoader(imageUrl,titleImageView)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:13
     * @Note   设置左侧点击事件
     * @param  listener 点击事件
     */
    fun setLeftClickListener(listener: (View) -> (Unit)):CommonActionBar{
        leftTouchView.setOnClickListener(listener)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:14
     * @Note   设置右侧点击事件
     * @param  listener 点击事件
     */
    fun setRightClickListener(listener: (View) -> (Unit)):CommonActionBar{
        rightTouchView.setOnClickListener(listener)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/1/31 下午5:14
     * @Note   设置网络图片加载器
     * @param  imageLoader  图片加载器  默认内置图片加载器
     */
    fun setImageLoader(imageLoader :(url : String , imageView :ImageView) -> (Unit)):CommonActionBar{
        this.imageLoader = imageLoader
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/2/27 下午3:10
     * @Note   设置标题显示与否
     * @param  visibility 显示
     */
    fun setTitleVisibility(visibility : Boolean):CommonActionBar{
        titleTextView.visibility = visibility.judge(View.VISIBLE,View.INVISIBLE)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/2/27 下午3:10
     * @Note   设置右侧显示与否
     * @param  visibility 显示
     */
    fun setRightVisibility(visibility : Boolean):CommonActionBar{
        rightTextView.visibility = visibility.judge(View.VISIBLE,View.INVISIBLE)
        rightImageView.visibility = visibility.judge(View.VISIBLE,View.INVISIBLE)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/2/28 上午9:17
     * @Note   设置背景颜色
     * @param  color ColorInt
     */
    fun setBgColor(@ColorInt color:Int):CommonActionBar{
        setBackgroundColor(color)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/2/28 上午9:17
     * @Note   设置背景颜色
     * @param  color ColorRes
     */
    fun setBgResColor(@ColorRes color: Int):CommonActionBar{
        setBackgroundColor(context.getColorCompatible(color))
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/2/28 上午10:31
     * @Note   设置显示隐藏
     * @param  visibility 是否显示
     */
    fun setVisibility(visibility: Boolean):CommonActionBar{
        this.visibility = visibility.judge(View.VISIBLE, View.INVISIBLE)
        return this
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/3/14 下午2:20
     * @Note   重置设置topbar高度
     * @param  height 重新设置的高度
     */
    fun reTobBarHeight(height : Int){

        this.reLayout<ConstraintLayout.LayoutParams> {
            params ->
            if (height == -1){
                params.height = (ScreenTool.getScreenHeight(context)*0.13).toInt()
            }else{
                params.height = height
            }
        }
    }

    /**
     * @author LDD
     * @From   CommonActionBar
     * @Date   2018/4/19 下午12:22
     * @Note   获占位View
     */
    fun holderViewProvider():View{
        return holderView
    }
}


