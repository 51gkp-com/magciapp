package com.enation.javashop.android.component.home.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.agreement.FloorActionAgreement
import com.enation.javashop.android.component.home.databinding.FloorItemFourImageLayBinding
import com.enation.javashop.android.component.home.databinding.FloorItemSingleImageLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.FloorItem
import com.enation.javashop.android.middleware.model.FloorViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.ScreenTool
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * 单个图片item
 */
class FloorFourImageAdapter(val data : FloorViewModel,val agreement: FloorActionAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<FloorItemFourImageLayBinding>,FloorItem>() {

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:26
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<FloorItemFourImageLayBinding> {
        return BaseRecyclerViewHolder.build(parent,R.layout.floor_item_four_image_lay)
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


    override fun getItemViewType(position: Int): Int {
        return 4
    }


    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:28
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then { self ->
            self.setMargin(5.dpToPx(),0,5.dpToPx(),5.dpToPx())
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FloorItemFourImageLayBinding>?, position: Int) {
        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.getScreenWidth(holder?.databinding?.root?.context!!)*0.25).toInt())
        holder?.bind { self ->
            loadImageRound(data.itemList[0].getImageValue(),self.imageA,RoundedCornersTransformation.CornerType.LEFT)
            loadImage(data.itemList[1].getImageValue(),self.imageB)
            loadImage(data.itemList[2].getImageValue(),self.imageC)
            loadImageRound(data.itemList[3].getImageValue(),self.imageD,RoundedCornersTransformation.CornerType.RIGHT)
            agreement.floorHandle(self.imageA,data.itemList[0])
            agreement.floorHandle(self.imageB,data.itemList[1])
            agreement.floorHandle(self.imageC,data.itemList[2])
            agreement.floorHandle(self.imageD,data.itemList[3])
        }
    }
}