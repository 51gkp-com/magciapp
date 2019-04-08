package com.enation.javashop.android.middleware.logic.presenter.goods

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsIntroduceContract
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/30 上午10:11
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Note   商品介绍页逻辑控制器
 */
class GoodsIntroducePresenter @Inject constructor() : RxPresenter<GoodsIntroduceContract.View>(), GoodsIntroduceContract.Presenter {

    /**
     * @author LDD
     * @From   GoodsIntroducePresenter
     * @Date   2018/4/8 下午1:04
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }
}