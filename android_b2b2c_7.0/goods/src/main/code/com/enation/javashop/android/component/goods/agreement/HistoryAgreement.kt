package com.enation.javashop.android.component.goods.agreement;

import com.enation.javashop.android.middleware.model.HistoryModel;

interface HistoryAgreement {

    fun delete(item :HistoryModel)

    fun add(item:HistoryModel)

    fun clear()
}