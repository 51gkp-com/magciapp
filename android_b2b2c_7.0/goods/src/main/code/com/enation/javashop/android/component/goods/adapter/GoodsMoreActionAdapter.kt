package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchMoreActionItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsFilterValue
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/3/15 下午4:03
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品筛选View 列表适配器
 */
class GoodsMoreActionAdapter(var data:GoodsFilterViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsSearchMoreActionItemBinding>, GoodsFilterValue>() {

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:03
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data.valueList
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:03
     * @Note   item点击过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:04
     * @Note   构建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsSearchMoreActionItemBinding> {
        return BaseRecyclerViewHolder<GoodsSearchMoreActionItemBinding>(DataBindingUtil.inflate<GoodsSearchMoreActionItemBinding>(LayoutInflater.from(parent?.context), R.layout.goods_search_more_action_item,parent,false).then {
            bind ->
            var layoutParams = bind.root.layoutParams
            layoutParams.width = (ScreenTool.getScreenWidth(bind.root.context)/2).toInt()
            layoutParams.height = (ScreenTool.getScreenWidth(bind.root.context)/8).toInt()
        })
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:04
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.valueList.size
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:04
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return GridLayoutHelper(2).then {
            self ->
            /**取消自动填充*/
            self.setAutoExpand(false)

            /**设置上下间距*/
            self.hGap = 20

            /**设置Margin*/
            self.setMargin(20,0,20,0)
        }
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/15 下午4:06
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsSearchMoreActionItemBinding>?, position: Int) {

        holder?.bind {
            binding ->
            binding.data = getItem(position)
            binding.root.setOnClickListener {
                data.valueList.forEach {
                    item ->
                    if (data.valueList.indexOf(item) != position){
                        item.selectedObservable.set(false)
                    }else{
                        getItem(position).selectedObservable.set(true)
                    }
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/21 下午2:36
     * @Note   回归初始值
     */
    fun resetData(){
        data.valueList.forEach {
            item ->
            item.selectedObservable.set(item.selected.then {
                if (it){
                    data.selectedFilterValue = item
                    data.selectedValueObser.set(item)
                }
            })
        }
    }

    /**
     * @author LDD
     * @From   GoodsMoreActionAdapter
     * @Date   2018/3/21 下午2:36
     * @Note   注入选中数据
     */
    fun injectData(){
        data.valueList.forEach {
            item ->
            item.selected = item.selectedObservable.get().then {
                if (it){
                    data.selectedFilterValue = item
                    data.selectedValueObser.set(item)
                }
            }
        }
    }
}