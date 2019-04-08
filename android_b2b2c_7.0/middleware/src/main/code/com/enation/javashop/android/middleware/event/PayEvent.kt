package com.enation.javashop.android.middleware.event

/**
 * Created by LDD on 2018/10/24.
 */
data class PayEvent(val state :PayState) {
}

enum class PayState{
    SUCCESS,FAILD,CANCLE
}