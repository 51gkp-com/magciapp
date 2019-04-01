package com.enation.javashop.android.middleware.logic.contract.order

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.AftersaleDetailModel
import com.enation.javashop.android.middleware.model.AftersaleListModel

/**
 * Created by LDD on 2018/10/16.
 */
interface AftersaleListContract{

    interface View : BaseContract.BaseView{

        fun render(datas : ArrayList<AftersaleListModel>)

    }

    interface Presenter : BaseContract.BasePresenter{

        fun load(page :Int)

    }
}