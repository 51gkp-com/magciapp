package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.OrderExpressHeaderModel
import com.enation.javashop.android.middleware.model.OrderExpressModel

/**
 * Created by LDD on 2018/11/1.
 */
interface ExpressContract {

    interface View :BaseContract.BaseView{

        fun render(headerInfo :OrderExpressHeaderModel,message :ArrayList<OrderExpressModel>)

    }

    interface Presenter : BaseContract.BasePresenter{

        fun load(sn :String)

    }

}