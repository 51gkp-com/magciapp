package com.enation.javashop.android.component.goods.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterFragParentItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.OnClickListenerAntiViolence
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/3/21 下午2:50
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品筛选父视图适配器
 */
class GoodsSearchFilterParentAdapter(val activity: Activity, val data :GoodsFilterViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsSearchFilterFragParentItemBinding>, GoodsFilterViewModel>() {

    /**
     * @Name  openCallback
     * @Type  block
     * @Note  打开子视图回调
     */
    private var openCallback :(()->Unit)? = null

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:50
     * @Note   设置回到
     * @param  call 回调代码块
     */
    fun setCallBack( call :()->Unit){
        openCallback = call
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:51
     * @Note   提供ViewType
     */
    override fun getItemViewType(position: Int): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:51
     * @Note   提供数据
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:51
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:51
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsSearchFilterFragParentItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_search_filter_frag_parent_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:52
     * @Note   提供Item数量
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:52
     * @Note   提供LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            val interval = ScreenTool.getScreenWidth(activity) * 0.85 /20
            self.setMargin(interval.toInt(),interval.toInt(), interval.toInt(),interval.toInt())
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterParentAdapter
     * @Date   2018/3/21 下午2:52
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsSearchFilterFragParentItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = this.data
            binding.root.setOnClickListener(OnClickListenerAntiViolence({
                data.openFlag.set(!data.openFlag.get())
                openCallback?.invoke()
            }))
        }
    }
}