package com.enation.javashop.android.middleware.logic.presenter.order

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreateGoodsContract
import com.enation.javashop.android.middleware.model.CartGoodsItemViewModel
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/23 下午6:44
 * @From   com.enation.javashop.android.middleware.logic.presenter.order
 * @Note   订单创建商品逻辑控制器
 */
class OrderCreateGoodsPresenter  @Inject constructor() : RxPresenter<OrderCreateGoodsContract.View>() , OrderCreateGoodsContract.Presenter {

    @Inject
    protected lateinit var cartApi : CartApi
    /**
     * @author LDD
     * @From   OrderCreateGoodsPresenter
     * @Date   2018/5/23 下午6:49
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

}