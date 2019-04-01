package com.enation.javashop.android.component.member.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberPointItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.middleware.model.PointViewModel

/**
 * @author LDD
 * @Date   2018/5/4 下午5:12
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   积分适配器
 */
class PointAdapter(val data :ArrayList<PointViewModel>) :BaseDelegateAdapter<BaseRecyclerViewHolder<MemberPointItemBinding>,PointViewModel>() {

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:13
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:13
     * @Note   item点击过滤操作
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:14
     * @Note   创建VH
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberPointItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.member_point_item)
    }

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:14
     * @Note   item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:15
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(1.dpToPx(),data.size)
    }

    /**
     * @author LDD
     * @From   PointAdapter
     * @Date   2018/5/4 下午5:15
     * @Note   绑定数据
=     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberPointItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
        }
    }
}