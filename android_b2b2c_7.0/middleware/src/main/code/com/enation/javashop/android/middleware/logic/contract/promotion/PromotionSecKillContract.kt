package com.enation.javashop.android.middleware.logic.contract.promotion

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.SecKillListViewModel

/**
 * @author LDD
 * @Date   2018/5/21 下午3:52
 * @From   com.enation.javashop.android.middleware.logic.contract.promotion
 * @Note   秒杀页面接口控制器
 */
interface PromotionSecKillContract {

    /**
     * @author LDD
     * @Date   2018/5/21 下午3:53
     * @From   PromotionSecKillContract
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView {

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/21 下午3:55
         * @Note   渲染秒杀页面
         * @param  data 秒杀数据
         */
        fun renderSecKill(data :ArrayList<SecKillListViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/5/21 下午3:54
     * @From   PromotionSecKillContract
     * @Note   逻辑接口
     */
    interface Presenter : BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/21 下午3:56
         * @Note   加载秒杀
         */
        fun loadSecKill()

    }

}