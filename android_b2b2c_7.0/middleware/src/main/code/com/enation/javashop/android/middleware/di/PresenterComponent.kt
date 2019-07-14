package com.enation.javashop.android.middleware.di

import com.enation.javashop.android.middleware.logic.presenter.cart.CartFragmentPresenter
import com.enation.javashop.android.middleware.logic.presenter.extra.ScanPresenter
import com.enation.javashop.android.middleware.logic.presenter.goods.*
import com.enation.javashop.android.middleware.logic.presenter.home.CategoryFragmentPresenter
import com.enation.javashop.android.middleware.logic.presenter.home.HomeActivityPresenter
import com.enation.javashop.android.middleware.logic.presenter.home.HomeFragmentPresenter
import com.enation.javashop.android.middleware.logic.presenter.member.*
import com.enation.javashop.android.middleware.logic.presenter.order.*
import com.enation.javashop.android.middleware.logic.presenter.promotion.*
import com.enation.javashop.android.middleware.logic.presenter.setting.SettingActivityPresenter
import com.enation.javashop.android.middleware.logic.presenter.shop.*
import com.enation.javashop.android.middleware.logic.presenter.welcome.WelcomePresenter
import dagger.Component


/**
 * @author LDD
 * @Date   2018/1/19 下午6:20
 * @From   com.enation.javashop.android.middleware.di
 * @Note   逻辑控制者依赖注入模块
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface PresenterComponent {

    fun inject(presenter: HomeFragmentPresenter)

    fun inject(presenter: HomeActivityPresenter)

    fun inject(presenter: WelcomePresenter)

    fun inject(presenter: CategoryFragmentPresenter)

    fun inject(presenter: CartFragmentPresenter)

    fun inject(presenter: MemberFragmentPresenter)

    fun inject(presenter: SettingActivityPresenter)

    fun inject(presenter: GoodsSearchPresenter)

    fun inject(presenter: SearchPresenter)

    fun inject(presenter: GoodsActivityPresenter)

    fun inject(presenter: GoodsInfoPresenter)

    fun inject(presenter: GoodsIntroducePresenter)

    fun inject(presenter: GoodsCommentPresenter)

    fun inject(presenter: ShopActivityPersenter)

    fun inject(presenter: ShopAllPersenter)

    fun inject(presenter: ShopHomePresenter)

    fun inject(presenter: ShopTagPresenter)

    fun inject(presenter: ShopListPersenter)

    fun inject(presenter: ShopCategoryActivityPresenter)

    fun inject(presenter: ShopInfoPresenter)

    fun inject(presenter: OrderListPresenter)

    fun inject(presenter: OrderDetailPresenter)

    fun inject(presenter: OrderPayPresenter)

    fun inject(presenter: OrderCreatePresenter)

    fun inject(presenter: OrderAfterSalePresenter)

    fun inject(presenter: PostCommentPresenter)

    fun inject(presenter: CommentListPresenter)

    fun inject(presenter: MemberCouponPresenter)

    fun inject(presenter: MemberCollectPresenter)

    fun inject(presenter: MemberPointPresenter)

    fun inject(presenter: MemberAddressPresenter)

    fun inject(presenter: MemberInvitePresenter)

    fun inject(presenter: MemberAddressEditPresenter)

    fun inject(presenter: MemberSecurityPresenter)

    fun inject(presenter: MemberCheckVcodePresenter)

    fun inject(presenter: MemberSendMessagePresenter)

    fun inject(presenter: MemberPasswordPresenter)

    fun inject(presenter: MemberLoginPresenter)

    fun inject(presenter: ImageVcodePresenter)

    fun inject(presenter: MemberInfoEditPresenter)

    fun inject(presenter: PromotionSecKillFragPresenter)

    fun inject(presenter: PromotionSecKillPresenter)

    fun inject(presenter: PromotionGroupBuyFragPresenter)

    fun inject(presenter: PromotionGroupBuyPresenter)

    fun inject(presenter: PromotionPointShopFragPresenter)

    fun inject(presenter: PromotionPointShopPresenter)

    fun inject(presenter: OrderCreatePayShipPresenter)

    fun inject(presenter: OrderCreateGoodsPresenter)

    fun inject(presenter: OrderCreateCouponPresenter)

    fun inject(presenter: OrderCreateReceiptPresenter)

    fun inject(presenter: AftersaleListPresenter)

    fun inject(presenter: AftersaleDetailPresenter)

    fun inject(presenter: WebPresenter)

    fun inject(presenter: CouponHallPresenter)

    fun inject(presenter: OrderExpressPresenter)

    fun inject(presenter: ScanPresenter)

}