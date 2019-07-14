package com.enation.javashop.android.component.member.adapter

import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberListHeaderLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.utils.base.tool.CommonTool
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/3/15 下午4:08
 * @From   com.enation.javashop.android.component.member.adapter
 * @Note   用户信息适配器
 */
 class MemberInfoAdapter(val fragment :WeakReference<Fragment>, var member: MemberViewModel): BaseDelegateAdapter<BaseRecyclerViewHolder<MemberListHeaderLayBinding>, MemberViewModel>(){

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:11
         * @Note   数据提供者
         */
        override fun dataProvider(): Any {
            return member
        }

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:11
         * @Note   item点击过滤
         * @param  position item坐标
         */
        override fun itemFilter(position: Int): Boolean {
            return false
        }

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:11
         * @Note   构建ViewHolder
         */
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<MemberListHeaderLayBinding> {
            return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.member_list_header_lay,parent,false))
        }

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:12
         * @Note   获取Item数量
         */
        override fun getItemCount(): Int {
            return 1
        }

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:12
         * @Note   创建LayoutHelper
         */
        override fun onCreateLayoutHelper(): LayoutHelper {
            return LinearLayoutHelper(0,1)
        }

        /**
         * @author LDD
         * @From   MemberInfoAdapter
         * @Date   2018/3/15 下午4:13
         * @Note   绑定ViewHolder
         */
        override fun onBindViewHolder(holder: BaseRecyclerViewHolder<MemberListHeaderLayBinding>?, position: Int) {
            holder?.bind {
                self ->
                self.memberOrderWaitComment.setOnClickListener {
                    fragment.get()?.push("/member/comment/list",isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberCoupon.setOnClickListener {
                    fragment.get()?.push("/member/coupon/list",isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberGoods.setOnClickListener {
                    fragment.get()?.push("/member/collect/main",{
                        it.withInt("type",0)
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberShop.setOnClickListener {
                    fragment.get()?.push("/member/collect/main",{
                        it.withInt("type",1)
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberOrderAftersale.setOnClickListener {
                    fragment.get()?.push("/aftersale/list",isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberInfoPointLevelLay.setOnClickListener {
                    fragment.get()?.push("/member/point/main",{
                        it.withString("title","等级积分")
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberInfoPointMoneyLay.setOnClickListener {
                    fragment.get()?.push("/member/point/main",{
                        it.withString("title","消费积分")
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberPoint.setOnClickListener {
                    fragment.get()?.push("/member/point/main",{
                        it.withString("title","积分明细")
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberAddress.setOnClickListener {
                    fragment.get()?.push("/member/address/list",isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberUserService.setOnClickListener {
                    CommonTool.createVerifyDialog("是否拨打玛吉克商城官方客服热线","取消","确定",fragment.get()?.activity,object :CommonTool.DialogInterface{

                        override fun yes() {

                        }

                        override fun no() {

                        }

                    }).show()
                }
                self.memberSecurity.setOnClickListener {
                    fragment.get()?.push("/member/security/main",isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberInfoUnloginHint.setOnClickListener {
                    fragment.get()?.activity?.push("/member/login/main",requstCode = 101,isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberInfoUnloginIv.setOnClickListener {
                    fragment.get()?.activity?.push("/member/login/main",requstCode = 101,isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberInvoke.setOnClickListener {
                    fragment.get()?.push("/common/web",{
                        it.withString("title","邀请注册")
                        it.withString("url", "http://m.51gkp.com/imgUrl?uname=" + member.username)
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                self.memberAddSoOn.setOnClickListener {
                    fragment.get()?.push("/member/invite/list",{
                        it.withString("uname", member.username)
                    },isNeedLogin = !MemberState.manager.getLoginState())
                }
                if(member.username == ""){
                    self.memberHeaderBg.setImageResource(R.drawable.javashop_member_header_nologin_bg)
                    self.memberInfoUserfaceIv.setImageResource(R.mipmap.member_nologin_userface)
                    self.memberInfoUserfaceIv.setCircleBackgroundColorResource(R.color.javashop_color_member_userface_nologin_bg_color)
                    self.loginLayout.visibility = View.GONE
                    self.unloginLayout.visibility = View.VISIBLE
                }else{
                    self.memberHeaderBg.setImageResource(R.drawable.javashop_member_header_login_bg)
                    self.memberInfoUserfaceIv.setCircleBackgroundColorResource(R.color.javashop_color_member_userface_login_bg_color)
                    self.loginLayout.visibility = View.VISIBLE
                    self.unloginLayout.visibility = View.GONE
                    if (member.face.isNullOrEmpty()){
                        self.memberInfoUserfaceIv.setImageResource(R.mipmap.member_login_userface)
                    }else{
                        Glide.with(self.root.context).load(member.face).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.member_login_userface).into(self.memberInfoUserfaceIv)
                    }
                }
                self.data = member
            }
        }

    }