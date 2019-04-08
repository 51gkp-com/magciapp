package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchItemGridLayBinding
import com.enation.javashop.android.component.goods.databinding.GoodsSearchItemListLayBinding
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsItemViewModel

/**
 * @author LDD
 * @Date   2018/3/21 下午2:53
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品列表适配器
 */
class SearchGoodsAdapter(var dataList:ArrayList<GoodsItemViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<ViewDataBinding>, GoodsItemViewModel>() {

    /**
     * @Name  isList
     * @Type  Boolean
     * @Note  标识列表状态是list还是grid
     */
    private var isList  = true

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:53
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return dataList
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:54
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:54
     * @Note   设置ViewType
     */
    override fun getItemViewType(position: Int): Int {
        return if (isList) 0 else 1
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:54
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ViewDataBinding> {
        return if (isList){
            BaseRecyclerViewHolder<GoodsSearchItemGridLayBinding>(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_search_item_list_lay,parent,false)))
        }else{
            BaseRecyclerViewHolder<GoodsSearchItemGridLayBinding>(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_search_item_grid_lay,parent,false)))
        }
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:54
     * @Note   设置Item数量
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:55
     * @Note   设置LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return if (isList){
            LinearLayoutHelper(0,1)
        }else{
            GridLayoutHelper(2).then {
                self ->
                /**取消自动填充*/
                self.setAutoExpand(false)

                /**设置左右间距*/
                self.hGap = 10

                /**设置上下间距*/
                self.vGap = 10

                /**设置Margin*/
                self.setMargin(0,10,0,0)
            }
        }
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:55
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ViewDataBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (binding is GoodsSearchItemGridLayBinding){
                binding.data = getItem(position)
            }else if(binding is GoodsSearchItemListLayBinding){
                binding.data = getItem(position)
            }
        }
    }

    /**
     * @author LDD
     * @From   SearchGoodsAdapter
     * @Date   2018/3/21 下午2:55
     * @Note   item样式转换
     */
    fun itemTransform(){
        isList = !isList
    }
}