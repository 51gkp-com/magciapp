package com.enation.javashop.android.component.member.di

import com.enation.javashop.android.component.member.activity.*
import com.enation.javashop.android.component.member.fragment.MemberFragment
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * @author LDD
 * @Date   2018/2/24 下午3:54
 * @From   com.enation.javashop.android.component.member.di
 * @Note   会员模块依赖注入入口
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface MemberComponent {
    fun inject(fragment: MemberFragment)

    fun inject(activity: CommentListActivity)

    fun inject(activity: PostCommentActivity)

    fun inject(activity: MemberCouponActivity)

    fun inject(activity: MemberCollectActivity)

    fun inject(activity: MemberPointActivity)

    fun inject(activity: MemberAddressActivity)

    fun inject(activity: MemberInviteActivity)

    fun inject(activity: MemberAddressEditActivity)

    fun inject(activity: MemberSecurityActivity)

    fun inject(activity: MemberCheckVcodeActivity)

    fun inject(activity: MemberSendMessageActivity)

    fun inject(activity: MemberPasswordActivity)

    fun inject(activity: MemberLoginActivity)

    fun inject(activity: MemberInfoEditActivity)

}