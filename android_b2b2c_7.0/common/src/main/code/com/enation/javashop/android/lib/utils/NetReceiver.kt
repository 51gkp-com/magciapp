package com.enation.javashop.android.lib.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.enation.javashop.android.lib.vo.NetStateEvent
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.utils.VoiNetTool

/**
 * @author  LDD
 * @Data   2017/12/22 下午5:19
 * @From   com.enation.javashop.android.lib.utils
 * @Note   监听网络变化的 广播接收者
 */
class NetReceiver : BroadcastReceiver() {

    companion object {
        private var lastType :VoiNetTool.netType? = null
    }

    override fun onReceive(p0: Context, p1: Intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (p1.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val netWorkState  = VoiNetTool.getAPNType(p0)
            if (lastType != null){
                if (lastType != netWorkState){
                    postEvent(netWorkState)
                }
            }else{
                postEvent(netWorkState)
            }
        }
    }

    private fun postEvent(netType :VoiNetTool.netType){
        lastType = netType
        when (netType) {
            VoiNetTool.netType.noneNet -> {
                getEventCenter().post(NetStateEvent(NetState.NONE))
            }
            VoiNetTool.netType.wifi -> {
                getEventCenter().post(NetStateEvent(NetState.WIFI))
            }
            else -> {
                getEventCenter().post(NetStateEvent(NetState.MOBILE))
            }
        }
    }
}