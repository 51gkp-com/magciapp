package com.enation.javashop.android.component.member.adapter

import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberPostCommentShopItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.PostCommentViewModel

/**
 * @author LDD
 * @Date   2018/4/26 下午3:07
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   评论编写页面
 */
class PostCommentShopAdapter(var data :PostCommentViewModel) : BaseDelegateAdapter<BaseRecyclerViewHolder<MemberPostCommentShopItemBinding>,PostCommentViewModel>() {

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:10
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:10
     * @Note   item过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:11
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberPostCommentShopItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.member_post_comment_shop_item)
    }

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:12
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:13
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0,10.dpToPx(),0,0)
        }
    }

    /**
     * @author LDD
     * @From   PostCommentShopAdapter
     * @Date   2018/4/26 下午3:13
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberPostCommentShopItemBinding>?, position: Int) {
            holder?.bind { 
                binding ->
                binding.postCommentShopDesStar.setStar(data.desStar)
                binding.postCommentShopLogisticsStar.setStar(data.logisticsStar)
                binding.postCommentShopServiceStar.setStar(data.serviceStar)

                binding.postCommentShopDesStar.setStarEvent {
                    data.desStar = it
                }
                binding.postCommentShopLogisticsStar.setStarEvent {
                    data.logisticsStar = it
                }
                binding.postCommentShopServiceStar.setStarEvent {
                    data.serviceStar = it
                }
            }   
    }
}