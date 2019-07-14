package com.enation.javashop.android.component.member.adapter

import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.activity.MemberAddressActivity
import com.enation.javashop.android.component.member.activity.MemberAddressEditActivity
import com.enation.javashop.android.component.member.databinding.MemberAddressItemBinding
import com.enation.javashop.android.component.member.databinding.MemberInviteItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.android.middleware.model.InviteViewModel
import com.enation.javashop.android.middleware.model.MemberAddressViewModel

/**
 * @author LDD
 * @Date   2018/5/7 下午5:01
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   地址适配器
 */
class MemberInviteAdapter(val data: ArrayList<InviteViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<MemberInviteItemBinding>, InviteViewModel>() {

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:14
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {

        return data

    }

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:16
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {

        return true

    }

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:16
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberInviteItemBinding> {

        return BaseRecyclerViewHolder.build(parent, R.layout.member_invite_item)

    }

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:17
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {

        return data.size

    }

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:18
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {

        return LinearLayoutHelper(10.dpToPx(),data.size)

    }

    /**
     * @author LDD
     * @From   MemberAddressAdapter
     * @Date   2018/5/7 下午5:18
     * @Note   绑定ViewHolper
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberInviteItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
        }
    }
}