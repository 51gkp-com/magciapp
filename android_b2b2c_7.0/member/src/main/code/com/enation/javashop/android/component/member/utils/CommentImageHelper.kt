package com.enation.javashop.android.component.member.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/5/3 下午1:45
 * @From   com.enation.javashop.android.component.member.utils
 * @Note   评论图片构建辅助类
 */
class CommentImageHelper private constructor(private val imageLayout: LinearLayout, private val imageMax: Int, private val images: ArrayList<String>, private val iInter: ImageLayoutIInter) {


    companion object {

        /**
         * @author LDD
         * @From   CommentImageHelper
         * @Date   2018/5/3 下午1:46
         * @Note   静态构建
         * @param  imageLayout 加载图片的父容器
         * @param  imageMax    图片上限
         * @param  images      图片集合
         * @param  iInter      回调
         */
        fun build(imageLayout: LinearLayout, imageMax: Int, images: ArrayList<String>, iInter: ImageLayoutIInter): CommentImageHelper {
            return CommentImageHelper(imageLayout, imageMax, images, iInter)
        }
    }

    /**
     * @Name  ADD_IMAGE_LAYOUT_TAG
     * @Type  String
     * @Note  添加图片Tag
     */
    private val ADD_IMAGE_LAYOUT_TAG = "ADD_IMAGE_LAYOUT_TAG"

    /**
     * @Name  context
     * @Type  Context
     * @Note  上下文
     */
    private val context: Context = imageLayout.context

    /**
     * @Name  screenWidth
     * @Type  float
     * @Note  屏幕宽度
     */
    private val screenWidth = ScreenTool.getScreenWidth(imageLayout.context)

    /**
     * @Name  addImageLay
     * @Type  LinearLayout
     * @Note  添加图片视图
     */
    private lateinit var addImageLay: LinearLayout

    /**
     * @Name  hintTV
     * @Type  TextView
     * @Note  添加文字视图
     */
    private lateinit var hintTV: TextView

    /**
     * @Name  hintTV
     * @Type  GradientDrawable
     * @Note  添加视图背景
     */
    private val addImageBg by lazy {
        GradientDrawable().then { self ->
            self.cornerRadius = 2.dpToPx().toFloat()
            self.setColor(Color.TRANSPARENT)
            self.setStroke(1.dpToPx(), Color.parseColor("#d8d7dc"), 5.dpToPx().toFloat(), 1.dpToPx().toFloat())
        }
    }

    /**
     * @Name  imageWidth
     * @Type  Int
     * @Note  image宽度
     */
    private val imageWidth by lazy {
        (screenWidth / (imageMax + 1)).toInt()
    }

    /**
     * @Name  marginLeft
     * @Type  Int
     * @Note  左侧间距
     */
    private val marginLeft by lazy {
        val marginCount = screenWidth / (imageMax + 1)
        return@lazy (marginCount / (imageMax + 1)).toInt()
    }

    /**
     * @author LDD
     * @From   CommentImageHelper
     * @Date   2018/5/3 下午1:52
     * @Note   初始化
     */
    init {
        images.forEach { url ->
            if (imageLayout.childCount < 5) {
                imageLayout.addView(getImageItem(url))
            }
        }
        if (imageLayout.childCount < 5) {
            this.imageLayout.addView(getAddImageLay())
            if (imageLayout.childCount > 1) {
                hintTV.text = String.format("%d / %d", imageLayout.childCount - 1, imageMax)
            }
        }
    }

    /**
     * @author LDD
     * @From   CommentImageHelper
     * @Date   2018/5/3 下午1:52
     * @Note   获取图片Item
     * @param  data url/bitmap
     */
    private fun getImageItem(data: Any): RelativeLayout {
        val imageLay = RelativeLayout(context)
        imageLay.tag = data
        val layoutParams = LinearLayout.LayoutParams(imageWidth, imageWidth)
        layoutParams.setMargins(marginLeft, 0, 0, 0)
        imageLay.layoutParams = layoutParams
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageLay.addView(imageView)
        val imageParams = imageView.layoutParams
        imageParams.width = imageWidth
        imageParams.height = imageWidth
        imageView.layoutParams = imageParams
        if (data is String) {
            GlideUtils.setImageForErrorDefult(context, imageView, data, 1)
        } else {
            imageView.setImageBitmap(data as Bitmap)
        }
        val closeImage = ImageView(context)
        closeImage.scaleType = ImageView.ScaleType.FIT_CENTER
        closeImage.setImageResource(R.drawable.javashop_icon_cancel_gray)
        imageLay.addView(closeImage)
        val imageParms = RelativeLayout.LayoutParams(imageWidth / 3, imageWidth / 3)
        imageParms.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        imageParms.setMargins(imageWidth / 3 * 2, 0, 0, 0)
        closeImage.layoutParams = imageParms
        closeImage.setOnClickListener { removeImage(imageLay) }
        return imageLay
    }

    /**
     * @author LDD
     * @From   CommentImageHelper
     * @Date   2018/5/3 下午1:52
     * @Note   获取添加图片视图
     */
    private fun getAddImageLay(): LinearLayout {
        addImageLay = LinearLayout(context)
        addImageLay.tag = ADD_IMAGE_LAYOUT_TAG
        val layoutParams = LinearLayout.LayoutParams(imageWidth, imageWidth)
        layoutParams.setMargins(marginLeft, 0, 0, 0)
        addImageLay.layoutParams = layoutParams
        addImageLay.gravity = Gravity.CENTER
        addImageLay.orientation = LinearLayout.VERTICAL
        addImageLay.background = addImageBg
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.javashop_icon_camera)
        addImageLay.addView(imageView)
        val imageParams = imageView.layoutParams
        imageParams.width = imageWidth / 2
        imageParams.height = imageWidth / 2
        imageView.layoutParams = imageParams
        hintTV = TextView(context)
        hintTV.setTextColor(Color.parseColor("#b5b6bc"))
        hintTV.textSize = (ScreenTool.px2dip(context, imageWidth.toFloat()) / 6).toFloat()
        hintTV.text = "添加图片"
        addImageLay.addView(hintTV)
        val textParems = hintTV.layoutParams
        textParems.height = LinearLayout.LayoutParams.WRAP_CONTENT
        textParems.width = LinearLayout.LayoutParams.WRAP_CONTENT
        hintTV.layoutParams = textParems
        addImageLay.setOnClickListener(View.OnClickListener {
            if (imageLayout.childCount == imageMax && imageLayout.findViewWithTag<View>(ADD_IMAGE_LAYOUT_TAG) == null) {
                showMessage(String.format("最多只可添加%d张图片", imageMax))
                return@OnClickListener
            }
            iInter.add(this@CommentImageHelper)
        })
        return addImageLay
    }

    /**
     * @author LDD
     * @From   CommentImageHelper
     * @Date   2018/5/3 下午1:53
     * @Note   移除Item
     * @param  view 移除的视图
     */
    private fun removeImage(view: View) {
        imageLayout.removeView(view)
        iInter.remove(view.tag as String)
        val subViewCount = imageLayout.childCount
        if (imageLayout.findViewWithTag<View>(ADD_IMAGE_LAYOUT_TAG) == null) {
            hintTV.text = String.format("%d / %d", subViewCount, imageMax)
            imageLayout.addView(addImageLay)
        } else if (subViewCount == 1) {
            hintTV.text = "添加图片"
        } else {
            hintTV.text = String.format("%d / %d", subViewCount - 1, imageMax)
        }
    }

    /**
     * @author LDD
     * @From   CommentImageHelper
     * @Date   2018/5/3 下午1:53
     * @Note   添加图片操作
     * @param  url 添加成功的url
     */
    fun addImage(url: String) {
        imageLayout.removeView(addImageLay)
        imageLayout.addView(getImageItem(url))
        val subViewCount = imageLayout.childCount
        if (subViewCount < imageMax) {
            hintTV.text = String.format("%d / %d", subViewCount, imageMax)
            imageLayout.addView(addImageLay)
        }
    }

    /**
     * @author LDD
     * @Date   2018/5/3 下午1:54
     * @From   CommentImageHelper
     * @Note   图片操作回调
     */
    interface ImageLayoutIInter {
        
        /**
         * @author LDD
         * @From   ImageLayoutIInter
         * @Date   2018/5/3 下午1:54
         * @Note   添加
         * @param  layUtils  self
         */
        fun add(layUtils: CommentImageHelper)
        
        /**
         * @author LDD
         * @From   ImageLayoutIInter
         * @Date   2018/5/3 下午1:54
         * @Note   移除
         * @param  url 图片URL
         */
        fun remove(url: String)
    }
}