package com.enation.javashop.android.component.goods.adapter

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.databinding.GoodsInfoCommentItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.to
import com.enation.javashop.android.middleware.model.CommentNumViewModel
import com.enation.javashop.android.middleware.model.GoodsCommentViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.imagepluin.utils.ImageUtils
import com.enation.javashop.net.engine.core.BaseDownLoadManager
import com.enation.javashop.utils.base.tool.CommonTool
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/4/8 下午1:35
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品详情推荐评论
 */
class GoodsInfoCommentAdapter(var activity :WeakReference<GoodsActivity>,var data :HashMap<String,Any>) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoCommentItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:36
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:37
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:37
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoCommentItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_comment_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:37
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:37
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   GoodsInfoCommentAdapter
     * @Date   2018/4/8 下午1:38
     * @Note   绑定ViewHolder
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoCommentItemBinding>?, position: Int) {

        holder?.bind { binding ->

            binding.goodsInfoCommentMoreBtn.setOnClickListener {
                activity.get()?.toComment()
            }
            val comment = data["data"]!!.to<ArrayList<GoodsCommentViewModel>>()[0]
            binding.goodsInfoHotCommentTitle.text = "评论(${(data["num"]!! as String)})"
            binding.goodsInfoCommentPercentTv.text = data["rate"]!!.to<String>()
            GlideUtils.setImage(binding.root.context,comment.userFace,binding.goodsInfoCommentItemFaceA)
            binding.goodsInfoCommentItemNameA.text = comment.userName
            binding.goodsInfoCommentItemStarA.setStar(comment.getStarViewNum())
            if (comment.grade == 3){
                binding.goodsInfoCommentItemGrade.text = "好评"
            }else if (comment.grade == 2){
                binding.goodsInfoCommentItemGrade.text = "中评"
            }else if (comment.grade == 1){
                binding.goodsInfoCommentItemGrade.text = "差评"
            }
            binding.goodsInfoHotCommentAContentTv.text = comment.content
            if (comment.images.count() > 0){
                binding.goodsInfoHotCommentAImageLay.visibility = View.VISIBLE
                binding.goodsInfoHotCommentAImageLay.setOnClickListener {
                    binding.root.context.to<GoodsActivity>().showGallay(comment.images.map { return@map Uri.parse(it) },0)
                }
                if (comment.images.getOrNull(0) != null){
                    binding.goodsInfoHotCommentAImageA.visibility = View.VISIBLE
                    GlideUtils.setImage(binding.root.context,comment.images[0],binding.goodsInfoHotCommentAImageA)
                }else{
                    binding.goodsInfoHotCommentAImageA.visibility = View.GONE
                }
                if (comment.images.getOrNull(1) != null){
                    binding.goodsInfoHotCommentAImageB.visibility = View.VISIBLE
                    GlideUtils.setImage(binding.root.context,comment.images[1],binding.goodsInfoHotCommentAImageB)
                }else{
                    binding.goodsInfoHotCommentAImageB.visibility = View.GONE
                }
                if (comment.images.getOrNull(2) != null){
                    binding.goodsInfoHotCommentAImageC.visibility = View.VISIBLE
                    GlideUtils.setImage(binding.root.context,comment.images[2],binding.goodsInfoHotCommentAImageC)
                }else{
                    binding.goodsInfoHotCommentAImageC.visibility = View.GONE
                }
                if (comment.images.getOrNull(3) != null){
                    binding.goodsInfoHotCommentAImageD.visibility = View.VISIBLE
                    GlideUtils.setImage(binding.root.context,comment.images[3],binding.goodsInfoHotCommentAImageD)
                }else{
                    binding.goodsInfoHotCommentAImageD.visibility = View.GONE
                }
            }else{
                binding.goodsInfoHotCommentAImageLay.visibility = View.GONE
            }
        }

    }
}