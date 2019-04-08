package com.enation.javashop.android.component.home.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.databinding.CategoryParentItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/1/30 下午2:50
 * @From   com.enation.javashop.android.component.home.adapter
 * @Note   二级分类适配器
 */
class ParentCategoryAdapter(val name : String) : BaseDelegateAdapter<BaseRecyclerViewHolder<CategoryParentItemBinding>,String>() {


    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/2/8 下午3:27
     * @Note   数据员提供者
     * @return 数据源
     */
    override fun dataProvider(): Any {
        return name
    }

    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/2/8 下午3:28
     * @Note   响应事件拦截方法
     * @param  position 坐标
     * @return 是否相应
     */
    override fun itemFilter(position: Int): Boolean {
        return false
    }

    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/1/30 下午2:52
     * @Note   获取LayoutHelper
     * @return LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/1/30 下午2:52
     * @Note   ViewHolder创建
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<CategoryParentItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context),R.layout.category_parent_item,parent,false))
    }

    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/1/30 下午2:53
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CategoryParentItemBinding>?, position: Int) {
        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.dip2px(holder?.itemView?.context,35f)))
        holder?.bind {
                binding ->
                binding.name = name
            }
    }

    /**
     * @author LDD
     * @From   ParentCategoryAdapter
     * @Date   2018/1/30 下午2:53
     * @Note   获取item总数
     * @return item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

}