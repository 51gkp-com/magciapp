package com.enation.javashop.android.lib.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author LDD
 * @Date   2018/2/2 下午4:02
 * @From   com.enation.javashop.android.lib.utils
 * @Note   RecyclerView ViewHolder 基类
 */
class BaseRecyclerViewHolder<out BindType:ViewDataBinding>(val databinding:BindType) :RecyclerView.ViewHolder(databinding.root) {

    companion object {
        /**
         * @author LDD
         * @From   BaseRecyclerViewHolder
         * @Date   2018/4/10 上午11:18
         * @Note   快速构建
         * @param  parent   父容器
         * @param  layoutId 布局ID
         */
        fun <BindType:ViewDataBinding>build(parent: ViewGroup?,layoutId : Int) : BaseRecyclerViewHolder<BindType>{
            return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(layoutId,parent,false)))
        }
    }

    /**
     * @author LDD
     * @From   BaseRecyclerViewHolder
     * @Date   2018/2/2 下午4:03
     * @Note   获取viewbinding
     * @return ViewDataBinding
     */
    fun getBinding():BindType{
        return databinding
    }

    /**
     * @author LDD
     * @From   BaseRecyclerViewHolder
     * @Date   2018/2/2 下午4:05
     * @Note   绑定数据
     * @param  block 绑定回调
     */
    inline fun bind(block :(binding : BindType)-> Unit){
        block(databinding)
    }

}