package com.enation.javashop.android.lib.utils

import android.util.Log
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.connection.ConnectionClassManager
import com.enation.javashop.net.engine.plugin.connection.DeviceBandwidthSampler


/**
 * @Coder  LDD
 * @Data   2017/12/22 下午3:58
 * @From   com.enation.javashop.android.lib.utils
 * @Note   网络状态监听
 */
class ConnectionQualityMonitor {

    /**伴生对象，其中的方法相当于Java的静态方法*/
    companion object {
        /**单利设计模式 懒汉式*/
        val INSTANCE:ConnectionQualityMonitor by lazy { ConnectionQualityMonitor() }
    }

    /**
     * @Coder  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionQualityMonitor
     * @Data   2017/12/22 下午3:59
     * @Note   开始网络取样
     */
    fun startSample(){
        DeviceBandwidthSampler.getInstance().startSampling()
    }

    /**
     * @Coder  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionQualityMonitor
     * @Data   2017/12/22 下午3:59
     * @Note   停止网络取样
     */
    fun stopSample(){
        DeviceBandwidthSampler.getInstance().stopSampling()
    }

    /**
     * @Coder  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionQualityMonitor
     * @Data   2017/12/22 下午4:00
     * @Note   获取当前网络状态
     * @return 当前网络状态
     */
    fun getConnectionQuanlity() :ConnectionQuality{
        val quanlity = ConnectionClassManager.getInstance().currentBandwidthQuality
        return quanlity
    }

    /**
     * @Coder  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionQualityMonitor
     * @Data   2017/12/22 下午4:00
     * @Note   获取当前网络速率
     * @return 当前网络速率
     */
    fun getConnectionBits() :Double{
      return  ConnectionClassManager.getInstance().downloadKBitsPerSecond
    }

}