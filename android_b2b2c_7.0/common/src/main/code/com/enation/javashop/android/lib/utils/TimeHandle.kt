package com.enation.javashop.android.lib.utils

import java.util.*

/**
 *  事件处理器
 */
object TimeHandle {

    fun getCurrentDay24Mill() :Long{
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 24)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:20
     * @Note   获取小时
     * @param  time 总时间
     */
    fun getHour(time: Int): Int {
        return time % (60 * 60 * 24) / (60 * 60)
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:21
     * @Note   获取分钟
     * @param  time 总时间
     */
    fun getMinute(time: Int): Int {
        return time % (60 * 60 * 24) % (60 * 60) / 60
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:21
     * @Note   获取秒
     * @param  time 总时长
     */
    fun getSecond(time: Int): Int {
        return time % (60 * 60 * 24) % (60 * 60) % 60
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:22
     * @Note   获取天数
     * @param  time 总时长
     */
    fun getDay(time: Int): Int {
        return time / (60 * 60 * 24)
    }
}