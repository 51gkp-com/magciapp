package com.enation.javashop.android.lib.vo

import com.enation.javashop.net.engine.model.NetState

/**
 * 网络监听事件实体类
 */
data class NetStateEvent constructor(val state: NetState)

/**
 * 【扩展】
 *  类型过滤，相应类型执行相应方法
 */
fun NetState.filter(onMobile:()->Unit,onWifi:()->Unit,offline:()->Unit){
    when (this) {
        NetState.MOBILE -> {
            onMobile.invoke()
        }
        NetState.WIFI -> {
            onWifi.invoke()
        }
        NetState.NONE -> {
            offline.invoke()
        }
    }
}