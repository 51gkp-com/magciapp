package com.enation.javashop.android.lib.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper

const val  VlayoutItemType   = 7321

class VlayoutHolderAdapter(val callBack : HolderCallBack) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    /**
     * @author LDD
     * @From   CategoryPlaceHolderAdapter
     * @Date   2018/1/30 下午2:42
     * @Note   ViewHolder创建
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return callBack.viewHolderProvider(parent)
    }

    /**
     * @author LDD
     * @From   CategoryPlaceHolderAdapter
     * @Date   2018/1/30 下午2:42
     * @Note   Item总数
     */
    override fun getItemCount(): Int {
        return onCreateLayoutHelper().itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return VlayoutItemType
    }

    /**
     * @author LDD
     * @From   CategoryPlaceHolderAdapter
     * @Date   2018/1/30 下午2:42
     * @Note   提供LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return callBack.layoutHelperProvider()
    }

    /**
     * @author LDD
     * @From   CategoryPlaceHolderAdapter
     * @Date   2018/1/30 下午2:42
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        callBack.bindView(holder)
    }

    /**
     * @author LDD
     * @Date   2018/1/30 下午2:44
     * @From   CategoryPlaceHolderAdapter
     * @Note   viewHolder
     */
    class PlaceHolder constructor(view : View) : RecyclerView.ViewHolder(view)


    interface HolderCallBack{

        fun bindView(holder: RecyclerView.ViewHolder)

        fun layoutHelperProvider():LayoutHelper

        fun viewHolderProvider(parent: ViewGroup):RecyclerView.ViewHolder
    }
}