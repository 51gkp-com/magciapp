package com.enation.javashop.android.component.member.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberRecommendTitleItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.ReflexHelper


/**
 * @author LDD
 * @Date   2018/3/15 下午4:16
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   推荐商品适配器
 */
class MemberRecommendTitleAdapter(private val offset: Int) : BaseDelegateAdapter<BaseRecyclerViewHolder<MemberRecommendTitleItemLayBinding>, Int>() {

    constructor(offset: ReflexHelper.ReflexFieldShell<Int>) : this(offset.value)

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:17
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return -1
    }

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:17
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:17
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberRecommendTitleItemLayBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.member_recommend_title_item_lay, parent, false))
    }

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:18
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:18
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        var stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(true)
        stickyLayoutHelper.setOffset(offset)
        return stickyLayoutHelper
    }

    /**
     * @author LDD
     * @From   MemberRecommendTitleAdapter
     * @Date   2018/3/15 下午4:19
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberRecommendTitleItemLayBinding>?, position: Int) {

    }
}