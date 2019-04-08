package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.AftersaleDetailModel

/**
 * Created by LDD on 2018/10/16.
 */
interface AftersaleDetailContract{

    interface View :BaseContract.BaseView{

        fun render(detail :AftersaleDetailModel)

    }

    interface Presenter :BaseContract.BasePresenter{

        fun load(sn :String)

    }

}