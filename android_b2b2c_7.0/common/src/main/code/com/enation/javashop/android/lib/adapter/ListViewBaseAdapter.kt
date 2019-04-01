package com.enation.javashop.android.lib.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.databinding.DataBindingUtil
import android.view.LayoutInflater

/**
 * @author LDD
 * @Date   2018/1/30 上午11:33
 * @From   com.enation.javashop.android.lib.utils
 * @Note   listView单一种类Item 快速适配器
 */
abstract class ListViewBaseAdapter<DataType, in BindingType:ViewDataBinding>constructor(private val context: Context, private val layout:Int, var datas : List<DataType>) : BaseAdapter() {

    /**
     * @Name  binding
     * @Type  BindingType
     * @Note  databinding工具类
     */
    private lateinit var binding : BindingType

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:35
     * @Note   获取View
     * @param  position 坐标
     * @param  convertView item
     * @param  parent  父容器
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var currentView = convertView
        if (currentView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), layout, parent, false)
            currentView = binding.root
            reSize(currentView)
            currentView.tag = binding
        } else {
            binding = currentView.tag as BindingType
        }
        fillItem(binding,datas.get(position),position)
        binding.root.setOnClickListener {
            itemClick(datas.get(position),position)
        }
        return currentView!!
    }

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:36
     * @Note   获取item数据
     * @param  position 坐标
     */
    override fun getItem(position: Int): DataType {
        return datas.get(position)
    }

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:36
     * @Note   获取ItemID
     * @param  position 坐标
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:37
     * @Note   获取item总数
     * @return item总数
     */
    override fun getCount(): Int {
        return datas.count()
    }

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:37
     * @Note   抽象方法 填充数据
     * @param  binding databinding
     * @param  data 数据
     * @param  position 坐标
     */
    abstract fun fillItem(binding: BindingType , data:DataType ,position: Int)

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:39
     * @Note   重新设置item大小
     * @param  convertView item
     */
    open fun reSize(convertView: View){}

    /**
     * @author LDD
     * @From   ListViewBaseAdapter
     * @Date   2018/1/30 上午11:39
     * @Note   item点击事件
     * @param  data itemData
     * @param  position 坐标
     */
    open fun itemClick(data:DataType ,position: Int){}
}