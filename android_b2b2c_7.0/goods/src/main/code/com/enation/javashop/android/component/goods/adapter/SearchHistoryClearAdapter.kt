package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.agreement.HistoryAgreement
import com.enation.javashop.android.component.goods.databinding.GoodsInfoAdviceItemBinding
import com.enation.javashop.android.component.goods.databinding.SearchClearLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/8 下午1:31
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品咨询
 */
class SearchHistoryClearAdapter(val agreement: HistoryAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<SearchClearLayBinding>,Int>() {

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:32
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:33
     * @Note   点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:33
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<SearchClearLayBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.search_clear_lay,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:33
     * @Note   item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:34
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   GoodsInfoAdviceAdapter
     * @Date   2018/4/8 下午1:35
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<SearchClearLayBinding>?, position: Int) {
        holder?.bind {binding ->
            binding.clearLay.setOnClickListener {
                CommonTool.createVerifyDialog("确认清空历史搜索么？","取消","确认",binding.root.context,object : CommonTool.DialogInterface{
                    override fun yes() {
                        agreement.clear()
                    }

                    override fun no() {

                    }
                }).show()
            }
        }
    }
}