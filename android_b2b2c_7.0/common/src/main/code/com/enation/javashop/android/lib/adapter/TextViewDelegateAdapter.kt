package com.enation.javashop.android.lib.adapter

import android.graphics.Color
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.databinding.TextAdapterLayBinding
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then

/**
 * @author LDD
 * @Date   2018/4/19 下午2:12
 * @From   com.enation.javashop.android.lib.adapter
 * @Note   文字视图Item适配器
 */
class TextViewDelegateAdapter(val text :String,val textLineCount :Int ,val textColor :Int ,val top : Int = 0 ,val bottom : Int = 0 ,val left : Int = 0 ,val right : Int = 0,val bgColor:Int = Color.WHITE) :BaseDelegateAdapter<BaseRecyclerViewHolder<TextAdapterLayBinding>,Int>(){

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:17
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return -1
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:17
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:18
     * @Note   构建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<TextAdapterLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.text_adapter_lay)
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:19
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:19
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(left,top,right,bottom)
        }
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:20
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TextAdapterLayBinding>?, position: Int) {

        holder?.bind {
            binding ->
            binding.count = textLineCount
            binding.textview.setTextColor(textColor)
            binding.text = text
            binding.root.setBackgroundColor(bgColor)
        }

    }

}




class TextViewListDelegateAdapter(val text :ArrayList<String>,val textLineCount :Int ,val textColor :Int ,val top : Int = 0 ,val bottom : Int = 0 ,val left : Int = 0 ,val right : Int = 0,val bgColor:Int = Color.WHITE) :BaseDelegateAdapter<BaseRecyclerViewHolder<TextAdapterLayBinding>,String>(){

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:17
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return text
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:17
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:18
     * @Note   构建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<TextAdapterLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.text_adapter_lay)
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:19
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return text.count()
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:19
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(left,top,right,bottom)
        }
    }

    /**
     * @author LDD
     * @From   TextViewDelegateAdapter
     * @Date   2018/4/19 下午3:20
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TextAdapterLayBinding>?, position: Int) {

        holder?.bind {
            binding ->
            binding.count = textLineCount
            binding.textview.setTextColor(textColor)
            binding.text = getItem(position)
            binding.root.setBackgroundColor(bgColor)
        }

    }

}