package com.enation.javashop.android.component.home.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.databinding.CategroyChildItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.getColorCompatible
import com.enation.javashop.android.middleware.model.ChildCategoryViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/1/30 下午2:54
 * @From   com.enation.javashop.android.component.home.adapter
 * @Note   三级分类适配器
 */
class ChildCategoryAdapter constructor(private val mLayoutHelper: LayoutHelper, var data: List<ChildCategoryViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<CategroyChildItemBinding>, ChildCategoryViewModel>() {

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/2/6 上午10:50
     * @Note   数据提供者
     * @return 数据源
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @Name  repairCount
     * @Type  Int
     * @Note  需要填充的数量
     */
    private var repairCount: Int = 0


    init {
        /**填充数量不到3的倍数的数据量*/
        repairCount = 3 - (data.size % 3)
        if (repairCount == 3) repairCount = 0
    }

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/1/30 下午2:56
     * @Note   获取layoutHelper
     * @return layouthelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/1/30 下午2:57
     * @Note   获取ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<CategroyChildItemBinding> {
        val binding = DataBindingUtil.inflate<CategroyChildItemBinding>(LayoutInflater.from(parent.context),R.layout.categroy_child_item,parent,false)
        return BaseRecyclerViewHolder(binding)
    }

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/1/30 下午2:57
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CategroyChildItemBinding>, position: Int) {
        holder.itemView.setBackgroundColor(holder.itemView.context.getColorCompatible(R.color.javashop_color_category_right_child_bg))
        if (position >= data.size){
            holder.getBinding().categoryChildItemTv.visibility = View.GONE
            holder.getBinding().categoryChildItemIv.visibility = View.GONE
        }else{
            holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((ScreenTool.getScreenWidth(holder.getBinding().root.context)*0.7- ScreenTool.dip2px(holder.getBinding().root.context,20f))/3*1.4).toInt())
            holder.getBinding().categoryChildItemTv.visibility = View.VISIBLE
            holder.getBinding().categoryChildItemIv.visibility = View.VISIBLE
            holder.getBinding().data = data[position]
        }
    }

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/1/30 下午2:57
     * @Note   item个数
     */
    override fun getItemCount(): Int {
        return data.size + repairCount
    }

    /**
     * @author LDD
     * @From   ChildCategoryAdapter
     * @Date   2018/1/30 下午2:58
     * @Note   item过滤器 过滤不需要绑定事件的item
     * @param  position item坐标
     * @return 是否需要绑定
     */
    override fun itemFilter(position: Int): Boolean {
        return position <= data.size-1
    }

}