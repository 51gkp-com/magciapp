package com.enation.javashop.android.lib.adapter

import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.alibaba.android.vlayout.DelegateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.more
import com.enation.javashop.imagepluin.R
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @author LDD
 * @Date   2018/1/30 上午11:46
 * @From   com.enation.javashop.android.lib.utils
 * @Note   DelegateAdapter基类 添加了点击长按事件
 */
abstract class BaseDelegateAdapter<VH : RecyclerView.ViewHolder, out DataType> : DelegateAdapter.Adapter<VH>() {

    /**
     * @Name  tag
     * @Type  Any
     * @Note  标识Adapter的标签 同一个列表内 不可使用重复Tag 否则会造成查找失败
     */
    var tag : Any? = null

    /**
     * @Name  itemClickListener
     * @Type  ((data:DataType, position:Int)->(Unit))?
     * @Note  点击事件
     */
    private var itemClickListener :((data:DataType, position:Int)->(Unit))? = null

    /**
     * @Name  itemClickListener
     * @Type  ((data:DataType, position:Int)->(Unit))?
     * @Note  长按事件
     */
    private var itemLongClickListener :((data:DataType, position:Int)->(Unit))? = null

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/2/6 上午10:40
     * @Note   数据提供者
     * @return 数据源
     */
    abstract fun dataProvider():Any

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/2/6 上午10:40
     * @Note   数据源处理器 判断是单个数据 还是数据集合
     * @return 全部转换为数据集合
     */
    private fun dataProcessor():List<DataType>{
        return if (dataProvider() is List<*> || dataProvider() is ArrayList<*>){
            dataProvider() as List<DataType>
        }else{
            listOf(dataProvider() as DataType)
        }
    }

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/1/30 上午11:54
     * @Note   设置item点击事件
     * @param  itemClickListener 点击回调
     */
    fun setOnItemClickListener(itemClickListener:(data:DataType,position:Int)->(Unit)){
        this.itemClickListener = itemClickListener
    }

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/1/30 上午11:54
     * @Note   设置item点击事件
     * @param  itemLongClickListener 长按回调
     */
    fun setOnItemLongClickListener(itemLongClickListener: (data:DataType,position:Int)->(Unit)){
        this.itemLongClickListener = itemLongClickListener
    }

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/1/30 上午11:55
     * @Note   重写该方法 对item进行事件配置
     */
    override fun onBindViewHolderWithOffset(holder: VH, position: Int, offsetTotal: Int) {
        super.onBindViewHolderWithOffset(holder, position, offsetTotal)
        if (itemFilter(position)){
            itemClickListener?.more {
                self ->
                holder.itemView.setOnClickListener {
                    self.invoke(dataProcessor()[position],position)
                }
            }
            itemLongClickListener?.more {
                self ->
                holder.itemView.setOnLongClickListener {
                    self.invoke(dataProcessor()[position],position)
                    return@setOnLongClickListener true
                }
            }
        }else{
            holder.itemView.setOnLongClickListener{true}
            holder.itemView.setOnClickListener{}
        }
    }

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/2/9 下午2:18
     * @Note   获取item对应数据
     * @param  item坐标
     */
    open fun getItem(position: Int):DataType{
        return dataProcessor()[position]
    }

    /**
     * @author LDD
     * @From   BaseDelegateAdapter
     * @Date   2018/1/30 上午11:56
     * @Note   用来拦截是否为该item配置响应事件
     * @param  position item坐标
     */
    abstract fun itemFilter(position: Int):Boolean


    fun loadImage(url :String,imageView :ImageView){
        Glide.with(imageView.context)
                .load(url)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(imageView)
    }

    fun loadImageRound(url :String,imageView: ImageView,cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL){
        Glide.with(imageView.context)
                .load(url)
                .thumbnail(0.1f)
                .bitmapTransform(RoundedCornersTransformation(imageView.context,3.dpToPx(),0,cornerType))
                .into(imageView)
    }

}