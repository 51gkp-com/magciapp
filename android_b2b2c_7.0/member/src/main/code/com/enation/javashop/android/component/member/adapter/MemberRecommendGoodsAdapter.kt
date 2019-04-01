package com.enation.javashop.android.component.member.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.bumptech.glide.Glide
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberRecommendGoodsItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.RecommendGoodsViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @author LDD
 * @Date   2018/3/15 下午4:13
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   推荐商品适配器
 */
class MemberRecommendGoodsAdapter(var goodsList: ArrayList<RecommendGoodsViewModel> = ArrayList()): BaseDelegateAdapter<BaseRecyclerViewHolder<MemberRecommendGoodsItemLayBinding>, RecommendGoodsViewModel>() {

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:14
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return goodsList
    }

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:14
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:15
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberRecommendGoodsItemLayBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.member_recommend_goods_item_lay,parent,false))
    }

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:15
     * @Note   获取Item数量
     */
    override fun getItemCount(): Int {
        return goodsList.count()
    }

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:15
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return GridLayoutHelper(2).then {
            self ->
            self.setAutoExpand(false)
            self.hGap = AppTool.SystemUI.dpToPx(5f)
            self.vGap = AppTool.SystemUI.dpToPx(5f)
            self.setMargin(AppTool.SystemUI.dpToPx(5f),AppTool.SystemUI.dpToPx(5f),AppTool.SystemUI.dpToPx(5f),0)
        }
    }

    /**
     * @author LDD
     * @From   MemberRecommendGoodsAdapter
     * @Date   2018/3/15 下午4:16
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberRecommendGoodsItemLayBinding>?, position: Int) {
        holder?.bind {
            self ->
            self.root.setBackgroundResource(R.drawable.javashop_corners_common)
            self.data = getItem(position)
            Glide.with(self.root.context)
                    .load(getItem(position).image)
                    .thumbnail(0.1f)
                    .bitmapTransform(CropSquareTransformation(self.root.context), RoundedCornersTransformation(self.root.context,3.dpToPx(),0,RoundedCornersTransformation.CornerType.TOP))
                    .into(self.memberRecommendGoodsIv)
        }
    }
}