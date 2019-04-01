package com.enation.javashop.android.lib.utils

import android.content.Context
import android.media.Image
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.imagepluin.R
import com.enation.javashop.imagepluin.utils.GlideUtils
import java.lang.ref.WeakReference


/**
 * @author LDD
 * @Date   2018/3/29 下午2:09
 * @From   com.enation.javashop.android.lib.utils
 * @Note   轮播辅助类
 */
class GalleryHelper<DataType> {

    /**
     * @Name  galleryList
     * @Type  DataType  相册数据泛型
     * @Note  相册数据
     */
    private lateinit var galleryList :ArrayList<DataType>

    /**
     * @Name  viewPager
     * @Type  ViewPager弱引用
     * @Note  ViewPager
     */
    private lateinit var viewPager :WeakReference<ViewPager>

    /**
     * @Name  autoScroll
     * @Type  Boolean
     * @Note  是否开启轮播
     */
    private var autoScroll = true

    /**
     * @Name  scrooIndex
     * @Type  Int
     * @Note  当前position
     */
    private var scrooIndex = 0

    /**
     * @Name  itemCallback
     * @Type  block
     * @Note  点击回调
     */
    private var itemCallback :((position :Int , imageView :ImageView) ->Unit)? = null

    /**
     * @Name  itemCallback
     * @Type  block
     * @Note  点击回调
     */
    private var itemCallback2 :((position :Int , imageView :ImageView , imageViews : SparseArray<ImageView>) ->Unit)? = null

    /**
     * @Name  dataTransform
     * @Type  block
     * @Note  数据转换回调
     */
    private lateinit var dataTransform :((data :DataType) ->String)

    /**
     * @Name  handler
     * @Type  Hander
     * @Note  处理器
     */
    private val handler by lazy { Handler(Handler.Callback { msg ->
        if (!autoScroll){
            return@Callback false
        }
        if (scrooIndex >= galleryList.size){
            scrooIndex = 0
            viewPager.get()?.setCurrentItem(scrooIndex,false)
        }else{
            viewPager.get()?.currentItem = scrooIndex
        }
        if (autoScroll){
            pageScroll()
        }
        return@Callback false
    }) }

    /**
     * @Name  galleryAdapter
     * @Type  PagerAdapter
     * @Note  相册适配器
     */
    private val galleryAdapter by lazy {
        object : PagerAdapter() {

            private var images = SparseArray<ImageView>()

            override fun isViewFromObject(view: View?, data: Any?): Boolean {
                return view === data as View
            }

            override fun getCount(): Int {
                return galleryList.size
            }

            override fun destroyItem(container: ViewGroup?, position: Int, view: Any?) {
                images.remove(position)
                container?.removeView(view as View)
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val imageView = SquareImageView(viewPager.get()?.context!!)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(viewPager.get()?.context)
                        .load(dataTransform.invoke(galleryList[position]))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.image_loading)
                        .error(R.drawable.image_error)
                        .into(imageView)
                itemCallback?.more { call -> imageView.setOnClickListener(OnClickListenerAntiViolence({
                    call.invoke(position,imageView)
                }))}
                itemCallback2?.more { call -> imageView.setOnClickListener(OnClickListenerAntiViolence({
                    call.invoke(position,imageView,images)
                }))}
                container?.addView(imageView, 0)
                images.append(position,imageView)
                return imageView
            }
        }
    }

    companion object {

        /**
         * @author LDD
         * @From   GalleryHelper
         * @Date   2018/3/29 下午2:39
         * @Note   构建入口
         * @param  viewPager ViewPager弱引用
         */
        fun <DataType>build(viewPager :WeakReference<ViewPager>):GalleryHelper<DataType>{
            return GalleryHelper(viewPager)
        }
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:40
     * @Note   私有构造方法 防止外部直接实例化
     * @param  viewPager ViewPager弱引用
     */
    private constructor(viewPager :WeakReference<ViewPager>){
        this.viewPager = viewPager
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:40
     * @Note   设置相册数据
     * @param  galleryList 相册数据
     */
    fun setGallery(galleryList :ArrayList<DataType>):GalleryHelper<DataType>{
        this.galleryList = galleryList
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:40
     * @Note   设置item点击回调
     * @param  itemCallback 点击回调
     */
    fun setItemCallBack(itemCallback :((position :Int, imageView :ImageView) ->Unit)):GalleryHelper<DataType>{
        this.itemCallback = itemCallback
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:40
     * @Note   设置item点击回调
     * @param  itemCallback 点击回调
     */
    fun setItemCallBack(itemCallback :((position :Int, imageView :ImageView,imageViews : SparseArray<ImageView>) ->Unit)):GalleryHelper<DataType>{
        this.itemCallback2 = itemCallback
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:41
     * @Note   设置滑动回调
     * @param  scrollCallBack 滑动回调
     */
    fun setScrollCallBack(scrollCallBack :((position :Int) ->Unit)):GalleryHelper<DataType>{
        scrollCallBack.invoke(0)
        viewPager.get()?.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                initPageScroll()
                scrollCallBack.invoke(position)
            }
        })
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:41
     * @Note   设置数据转换器
     * @param  dataTransform 数据转换器
     */
    fun setDataTransform(dataTransform :((data :DataType) ->String)):GalleryHelper<DataType>{
        this.dataTransform = dataTransform
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:43
     * @Note   开启自动轮播
     * @param  context
     */
    fun autoScroll():GalleryHelper<DataType>{
        autoScroll = true
        return this
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:47
     * @Note   执行
     */
    fun execute(){
        this.viewPager.get()?.adapter = galleryAdapter
        if (autoScroll){
            pageScroll()
        }
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:51
     * @Note   关闭滑动
     */
    fun offAutoScroll(){
        autoScroll = false
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:52
     * @Note   自动滑动
     */
    private fun pageScroll(){
        initPageScroll()
        handler.postDelayed({
            handler.sendEmptyMessage(scrooIndex)
        },3000)
    }

    /**
     * @author LDD
     * @From   GalleryHelper
     * @Date   2018/3/29 下午2:52
     * @Note   初始化滑动坐标
     */
    private fun initPageScroll(){
        scrooIndex = viewPager.get()?.currentItem!! + 1
    }

}

class SquareImageView: ImageView {

    constructor(context: Context) : this(context,null)

    constructor(context :Context,attrs : AttributeSet?) : super(context,attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSize, widthSize)
    }
}