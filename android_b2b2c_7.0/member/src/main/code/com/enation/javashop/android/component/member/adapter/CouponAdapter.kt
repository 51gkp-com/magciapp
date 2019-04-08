package com.enation.javashop.android.component.member.adapter

import android.graphics.Color
import android.support.v4.app.Fragment
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberCouponItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.utils.base.tool.BaseToolActivity

/**
 * @author LDD
 * @Date   2018/5/3 下午2:55
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   优惠券适配器
 */
class CouponAdapter(val data :ArrayList<CouponViewModel>)  :BaseDelegateAdapter<BaseRecyclerViewHolder<MemberCouponItemBinding>,CouponViewModel>() {

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午2:55
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午2:56
     * @Note   点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午2:57
     * @Note   构建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberCouponItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.member_coupon_item)
    }

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午3:23
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午3:23
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(1.dpToPx(),data.size).then {
            self ->
            self.setDividerHeight(10.dpToPx())
            self.setMargin(10.dpToPx(),10.dpToPx(),10.dpToPx(),10.dpToPx())
        }
    }

    /**
     * @author LDD
     * @From   CouponAdapter
     * @Date   2018/5/3 下午3:23
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberCouponItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
            binding.root.setBackgroundColor(Color.TRANSPARENT)
            if (!getItem(position).isGet && !getItem(position).isDateed && !getItem(position).isUseed){
                binding.memberCouponItemUseTv.setOnClickListener {
                    if(binding.root.context is Fragment){
                        (binding.root.context as Fragment).push("/shop/detail",{
                            it.withInt("shopId",getItem(position).shopId)
                        })
                    }else if (binding.root.context is BaseToolActivity){
                        (binding.root.context as BaseToolActivity).push("/shop/detail",{
                            it.withInt("shopId",getItem(position).shopId)
                        })
                    }
                }
            }
        }
    }
}