package com.enation.javashop.android.middleware.logic.contract.shop

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ShopItem
import com.enation.javashop.android.middleware.model.ShopViewModel


interface ShopListContract {

    interface View :BaseContract.BaseView{

        fun render(data :ArrayList<ShopItem>)

    }

    interface Presenter :BaseContract.BasePresenter{

        fun loadData(keyWord :String ,page : Int)

    }

}