package com.enation.javashop.android.component.member.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.activity.PostCommentActivity
import com.enation.javashop.android.component.member.databinding.MemberPostCommentGoodsItemBinding
import com.enation.javashop.android.component.member.utils.CommentImageHelper
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.android.middleware.model.PostCommentGoodsViewModel
import com.enation.javashop.android.middleware.model.PostCommentViewModel
import com.enation.javashop.photoutils.uitl.RxGetPhotoUtils
import com.enation.javashop.utils.base.tool.CommonTool

/**
 * @author LDD
 * @Date   2018/4/26 下午3:18
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   发表评论页面商品Item适配器
 */
class PostCommentGoodsAdapter(var data : PostCommentViewModel) :BaseDelegateAdapter<BaseRecyclerViewHolder<MemberPostCommentGoodsItemBinding> ,PostCommentGoodsViewModel>() {

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:20
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data.goodsList
    }

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:20
     * @Note   item点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:20
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberPostCommentGoodsItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.member_post_comment_goods_item)
    }

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:21
     * @Note   item总户数
     */
    override fun getItemCount(): Int {
        return data.goodsList.count()
    }

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:22
     * @Note   构建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(10.dpToPx(),data.goodsList.count())
    }

    /**
     * @author LDD
     * @From   PostCommentGoodsAdapter
     * @Date   2018/4/26 下午3:22
     * @Note   绑定ViewHolder数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberPostCommentGoodsItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            /**获取页面弱引用避免内存泄漏*/
            val activity = (binding.root.context as PostCommentActivity).weak()
            binding.data = getItem(position)
            binding.postGoodsCommentItemImageLay.removeAllViews()
            binding.postGoodsCommentItemStar.setStar(getItem(position).goodsStar)
            binding.postGoodsCommentItemStar.setStarEvent {
                star ->
                getItem(position).goodsStar = star
            }
            /**设置评论图片*/
            CommentImageHelper.build(binding.postGoodsCommentItemImageLay,5,getItem(position).commentImages,object :CommentImageHelper.ImageLayoutIInter{
                override fun add(layUtils: CommentImageHelper) {
                    /**添加图片方式*/
                    CommonTool.createVerifyDialog("请选择添加图片方式","相册","拍照",activity.get(),object :CommonTool.DialogInterface{
                        override fun yes() {
                            RxGetPhotoUtils.init(activity.get()).configCompress(true,true,false,102400,800,800).getPhotoForCarema(false)
                        }

                        override fun no() {
                            RxGetPhotoUtils.init(activity.get()).configCompress(true,true,true,102400,800,800).getPhotoFromGallery(false)
                        }
                    }).then {
                        self ->
                        self.setCancelable(true)
                        self.setCanceledOnTouchOutside(true)
                    }.show()
                    /**设置图片获取监听*/
                    activity.get()?.photoListener = {
                        url ->
                        getItem(position).commentImages.add(url)
                        layUtils.addImage(url)
                    }
                }

                override fun remove(url: String) {
                    getItem(position).commentImages.remove(url)
                }
            })
        }
    }
}