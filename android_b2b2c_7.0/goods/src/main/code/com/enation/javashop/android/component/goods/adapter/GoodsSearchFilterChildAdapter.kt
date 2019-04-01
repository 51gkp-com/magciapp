package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterFragChildItemBinding
import com.enation.javashop.android.component.goods.fragment.GoodsSearchFilterValueFragment
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsFilterValue
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/3/21 下午2:37
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品列表筛选列表子视图
 */
class GoodsSearchFilterChildAdapter(val activity: FragmentActivity , var datas :GoodsFilterViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsSearchFilterFragChildItemBinding>, GoodsFilterValue>() {

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:40
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return datas.valueList
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:40
     * @Note   点击事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:41
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsSearchFilterFragChildItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_search_filter_frag_child_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:41
     * @Note   提供Item总数
     */
    override fun getItemCount(): Int {
        return if (datas.valueList.size <= 3){
            datas.valueList.size
        }else if(datas.openFlag.get()){
            if(datas.valueList.size > 9 && datas.filterName.contains("品牌")){
                9
            }else{
                datas.valueList.size
            }
        }else{
            3
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:41
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return GridLayoutHelper(3).then {
            self ->

            /**获取间隔  应为 屏幕总宽度的85%的20分之一*/
            val interval = ScreenTool.getScreenWidth(activity) * 0.85 /20

            /**取消自动填充*/
            self.setAutoExpand(false)

            /**设置左右间距*/
            self.hGap = interval.toInt()

            /**设置上下间距*/
            self.vGap = interval.toInt()

            /**设置Margin 当tag==-1时 代表该adapter为最后一个 当adapter为最后一个时 设置底部间距*/
            self.setMargin(interval.toInt(),0, interval.toInt(),(tag == -1).judge(interval.toInt(),0))
        }
    }

    fun notifyChild(){
        if (itemCount > 3){
            notifyItemRangeRemoved(3,itemCount-3)
        }else{
            notifyItemRangeInserted(3,itemCount-3)
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:48
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsSearchFilterFragChildItemBinding>?, position: Int) {
        holder?.bind {
            binding ->

            if (datas.valueList.size > 9 && position == 8 && datas.filterName.contains("品牌")){
                binding.data = null
                binding.root.setOnClickListener {
                    val fragment = GoodsSearchFilterValueFragment()
                    fragment.setData(datas)
                    activity.supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                            .add(R.id.right_layout, fragment,"value")
                            .addToBackStack(null).commit()
                }
            }else{
                binding.data = datas.valueList[position]
                binding.root.setOnClickListener {
                    datas.valueList.forEach {
                        item ->
                        if (datas.valueList.indexOf(item) != position){
                            item.selectedObservable.set(false)
                        }else{
                            getItem(position).selectedObservable.set(true)
                            datas.selectedValueObser.set(getItem(position))
                        }
                    }
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:48
     * @Note   设置ViewType
     */
    override fun getItemViewType(position: Int): Int {
        return 2
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:49
     * @Note   回归原始数据
     */
    fun resetData(){
        var selectFlag = false
        datas.valueList.forEach {
            item ->
            item.selectedObservable.set(item.selected.then {
                if (it){
                    selectFlag = true
                    datas.selectedFilterValue = item
                    datas.selectedValueObser.set(item)
                }
            })
        }
        if (!selectFlag && datas.selectedValueObser.get()!=null){
            datas.selectedFilterValue = null
            datas.selectedValueObser.set(null)
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterChildAdapter
     * @Date   2018/3/21 下午2:49
     * @Note   注入操作后的数据
     */
    fun injectData(){
        var selectFlag = false
        datas.valueList.forEach {
            item ->
            item.selected = item.selectedObservable.get().then {
                if (it){
                    selectFlag = true
                    datas.selectedFilterValue = item
                    datas.selectedValueObser.set(item)
                }
            }
        }
        if (!selectFlag && datas.selectedValueObser.get()!=null){
            datas.selectedFilterValue = null
            datas.selectedValueObser.set(null)
        }
    }

}