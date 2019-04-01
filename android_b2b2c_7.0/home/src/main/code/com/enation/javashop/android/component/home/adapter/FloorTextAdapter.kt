package com.enation.javashop.android.component.home.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.agreement.FloorActionAgreement
import com.enation.javashop.android.component.home.databinding.FloorItemSingleImageLayBinding
import com.enation.javashop.android.component.home.databinding.FloorItemTextLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.FloorItem
import com.enation.javashop.android.middleware.model.FloorViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * 单个图片item
 */
class FloorTextAdapter(val data : FloorViewModel,val agreement: FloorActionAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<FloorItemTextLayBinding>,FloorItem>() {

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:26
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }


    override fun getItemViewType(position: Int): Int {
        return 11
    }


    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   item过滤
     * @param  position Item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<FloorItemTextLayBinding> {
        return BaseRecyclerViewHolder.build(parent,R.layout.floor_item_text_lay)
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:28
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then { self ->
            self.setMargin(10,0,10,0)
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FloorItemTextLayBinding>?, position: Int) {
        holder?.bind { self ->
            self.text.text = data.itemList[0].getImageValue()
            agreement.floorHandle(self.text,data.itemList[0])
        }
    }
}