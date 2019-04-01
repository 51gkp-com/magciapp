package com.enation.javashop.android.component.goods.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.databinding.GoodsCommentItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.lib.base.BaseControl
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.AutoSizeTextView
import com.enation.javashop.android.middleware.model.GoodsCommentViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.ScreenTool
import com.umeng.socialize.media.Base
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/4/2 下午2:37
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品评论页面适配器
 */
class GoodsCommentItemAdapter(control: WeakReference<BaseControl>, val data :ArrayList<GoodsCommentViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsCommentItemLayBinding>,GoodsCommentViewModel>() {

    /**
     * @Name  imageLayWidth
     * @Type  float
     * @Note  图片父容器宽度
     */
    private val imageLayWidth by lazy { ScreenTool.getScreenWidth(BaseApplication.appContext)*0.9-ScreenTool.dip2px(BaseApplication.appContext,30f) }

    /**
     * @Name  TenDp
     * @Type  Float
     * @Note  10Dp 对应的px
     */
    private val FiveDp by lazy { ScreenTool.dip2px(BaseApplication.appContext,5f) }
    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/2 下午2:38
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/2 下午2:39
     * @Note   点击事件过滤器
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/2 下午2:39
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsCommentItemLayBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_comment_item_lay,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/2 下午2:40
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/2 下午2:40
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.size)
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/8 下午1:05
     * @Note   绑定参数
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsCommentItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.goodsCommentItemImageLay.removeAllViews()
            binding.data = getItem(position)
            if (getItem(position).grade == 3){
                binding.goodsCommentItemGrade.text = "好评"
            }else if (getItem(position).grade == 2){
                binding.goodsCommentItemGrade.text = "中评"
            }else if (getItem(position).grade == 1){
                binding.goodsCommentItemGrade.text = "差评"
            }
            if (getItem(position).images.size >0){
                imageSet(binding.goodsCommentItemImageLay,getItem(position).images)
                binding.goodsCommentItemImageLay.setOnClickListener {
                    binding.root.context.to<GoodsActivity>().showGallay(getItem(position).images.map { return@map Uri.parse(it) },0)
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/8 下午1:06
     * @Note   设置图片布局
     * @param  lay 图片父布局
     * @param  images 图片URL
     */
    private fun imageSet(lay :LinearLayout,images:ArrayList<String>){
        if (images.size>1){
            if (images.size == 4){
                val linearA = LinearLayout(lay.context).then {
                    self ->
                    self.orientation = LinearLayout.HORIZONTAL
                }
                val linearB = LinearLayout(lay.context).then {
                    self ->
                    self.orientation = LinearLayout.HORIZONTAL
                }
                lay.addView(linearA)
                lay.addView(linearB)
                linearA.reLayout<LinearLayout.LayoutParams> {
                    params ->
                    params.setMargins(0,0,0,FiveDp*2)
                    params.width = imageLayWidth.toInt()
                    params.height = ((imageLayWidth-FiveDp*4)/3).toInt()
                }
                linearB.reLayout<LinearLayout.LayoutParams> {
                    params ->
                    params.setMargins(0,0,0,FiveDp*2)
                    params.width = imageLayWidth.toInt()
                    params.height = ((imageLayWidth-FiveDp*4)/3).toInt()
                }
                createImageView(linearA,images[0],false,0)
                createImageView(linearA,images[1],true,1)
                createImageView(linearB,images[2],false,2)
                createImageView(linearB,images[3],true,3)
            }else{
                val count = (images.size>9).judge(9,images.size)
                val lastNum = count%3
                var hNum = (count - lastNum)/3
                if (lastNum > 0){
                    hNum +=1
                }
                var index = -1
                for (i in 0..(hNum-1)){
                    val linear = LinearLayout(lay.context).then {
                        self ->
                        self.orientation = LinearLayout.HORIZONTAL
                    }
                    lay.addView(linear)
                    linear.reLayout<LinearLayout.LayoutParams> {
                        params ->
                        params.setMargins(0,0,0,FiveDp*2)
                        params.width = imageLayWidth.toInt()
                        params.height = ((imageLayWidth-FiveDp*4)/3).toInt()
                    }
                    index+=1
                    images.getOrNull(index)?.more {
                        url ->
                        createImageView(linear,url,false,index,images.size)
                    }
                    index+=1
                    images.getOrNull(index)?.more {
                        url ->
                        createImageView(linear,url,true,index,images.size)
                    }
                    index+=1
                    images.getOrNull(index)?.more {
                        url ->
                        createImageView(linear,url,true,index,images.size)
                    }
                }
            }
        }else{
            var iv = ImageView(lay.context)
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            lay.addView(iv)
            iv.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.width = imageLayWidth.toInt()
                params.height = imageLayWidth.toInt()
            }
            GlideUtils.setImage(lay.context,images[0],iv)
        }
    }

    /**
     * @author LDD
     * @From   GoodsCommentItemAdapter
     * @Date   2018/4/8 下午1:06
     * @Note   添加ImageView
     * @param  lay 父布局
     * @param  url 图片url
     * @param  haveMargin 是否设置外间距
     * @param  index 当前index
     * @param  count 图片总数
     */
    @SuppressLint("SetTextI18n")
    private fun createImageView(lay:LinearLayout, url :String, haveMargin : Boolean, index : Int, count :Int = 0){
        if (index == 8 && count > 9){
            val parent = RelativeLayout(lay.context)
            lay.addView(parent)
            parent.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.width = ((imageLayWidth - FiveDp*4)/3).toInt()
                params.height = ((imageLayWidth - FiveDp*4)/3).toInt()
                params.setMargins(FiveDp*2,0,0,0)
            }
            val iv = ImageView(parent.context)
            parent.addView(iv)
            iv.reLayout<RelativeLayout.LayoutParams> {
                params ->
                params.width = ((imageLayWidth - FiveDp*4)/3).toInt()
                params.height = ((imageLayWidth - FiveDp*4)/3).toInt()
            }
            GlideUtils.setImage(lay.context,url,iv)
            val textView = AutoSizeTextView(parent.context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (iv.layoutParams.width/6).toFloat())
            textView.setBackgroundResource(R.drawable.javashop_rounder_corner_gray)
            textView.setTextColor(Color.WHITE)
            textView.setLines(1)
            textView.gravity = Gravity.CENTER
            parent.addView(textView)
            textView.reLayout<RelativeLayout.LayoutParams> {
                params ->
                params.width =  RelativeLayout.LayoutParams.WRAP_CONTENT
                params.height = iv.layoutParams.width/5
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                params.setMargins(0,0,params.height/3,params.height/3)
            }
            textView.text = " 共 $count 张 "
        }else{
            val iv = ImageView(lay.context)
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            lay.addView(iv)
            iv.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.width = ((imageLayWidth - FiveDp*4)/3).toInt()
                params.height = ((imageLayWidth - FiveDp*4)/3).toInt()
                if (haveMargin){
                    params.setMargins(FiveDp*2,0,0,0)
                }
            }
            GlideUtils.setImage(lay.context,url,iv)
        }
    }
}