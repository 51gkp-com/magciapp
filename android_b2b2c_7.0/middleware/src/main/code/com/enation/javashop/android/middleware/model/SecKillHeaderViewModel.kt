package com.enation.javashop.android.middleware.model

import com.enation.javashop.android.lib.utils.ObserableString

/**
 * @author LDD
 * @Date   2018/5/21 下午2:28
 * @From   com.enation.javashop.android.middleware.model
 * @Note   秒杀列表头部ViewModel
 */
data class SecKillHeaderViewModel(val title :String , val type :String ,val time :Int, var hour :ObserableString = ObserableString("00") , var min :ObserableString = ObserableString("00"),var sec :ObserableString = ObserableString("00"))