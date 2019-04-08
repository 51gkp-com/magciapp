package com.enation.javashop.android.middleware.logic.contract.goods

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.HistoryModel


/**
 * Created by LDD on 2018/10/15.
 */
interface SearchContract {


    /**
     * @author LDD
     * @Date   2018/3/23 下午2:49
     * @Note   视图接口
     */
    interface View : BaseContract.BaseView{

        fun render(data :ArrayList<Any>)

    }

    /**
     * @author LDD
     * @Date   2018/3/23 下午2:50
     * @Note   数据接口
     */
    interface Presenter : BaseContract.BasePresenter{

        fun loadData()

        fun clearHistory()

        fun addHistory(item :HistoryModel)

        fun deleteHistory(item :HistoryModel)

    }

}