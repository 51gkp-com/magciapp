package com.enation.javashop.android.lib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.utils.base.cache.ACache


/**
 * @author LDD
 * @Date   2018/8/9 下午4:31
 * @From   com.enation.javashop.android.lib.utils
 * @Note   UUID 工具类
 */
class UUID {

    /**
     * 伴生对象
     */
    companion object {

        /*UUID*/
        var uuid = get()

        /**
         * @author LDD
         * @From   UUID
         * @Date   2018/8/9 下午4:31
         * @Note   获取UUID
         * @param  uuid
         */
        @SuppressLint("MissingPermission", "WifiManagerLeak", "HardwareIds")
        private fun get() : String{
            var macAddress = ACache.get(BaseApplication.appContext).getAsString("UUID")
            if (macAddress == null){
                val wifiManager :WifiManager? = BaseApplication.appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifiManager?.connectionInfo
                if (!wifiManager!!.isWifiEnabled) {
                    //必须先打开，才能获取到MAC地址
                    wifiManager.isWifiEnabled = true
                    wifiManager.isWifiEnabled = false
                }
                if (null != info) {
                    macAddress = info.macAddress
                }
            }
            return macAddress
        }

        fun refreshUUID(id : String){
            uuid = id
            ACache.get(BaseApplication.appContext).put("UUID", uuid)
        }

    }

}