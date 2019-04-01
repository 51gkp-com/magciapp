package com.enation.javashop.android.lib.utils

import java.sql.Time
import java.util.concurrent.Executors

/**
 * @author LDD
 * @Date   2018/5/9 下午6:19
 * @From   com.enation.javashop.android.lib.utils
 * @Note   倒计时工具类
 */
class TimeEngine {

    companion object {
        /**
         * @author LDD
         * @From   TimeEngine
         * @Date   2018/5/10 上午8:46
         * @Note   静态构建
         * @param
         */
        fun build(timeLength: Long,current :Boolean = false) :TimeEngine{
            return TimeEngine(timeLength,current)
        }
    }

    private var isOnlySingleProcess = false

    private var workState = false

    fun needSingleProcess() : TimeEngine{
        isOnlySingleProcess = true
        return this
    }



    /**
     * @Name  threadPool
     * @Type  ExecutorService
     * @Note  可缓存线程池，用于处理非UI线程任务
     */
    private var threadPool = Executors.newFixedThreadPool(1)

    /**
     * @Name  complete
     * @Type  Block
     * @Note  结束代码块
     */
    private var complete :(()->Unit)? = null

    /**
     * @Name  timeLong
     * @Type  Long
     * @Note  距离结束总时长
     */
    private var timeLength :Long

    /**
     * @Name  survival
     * @Type  Boolean
     * @Note  标记当前线程是否应该存活
     */
    private var survival = true

    /**
     * @Name  timeResult
     * @Type  Int
     * @Note  经过时间递减结果
     */
    private var timeResult :Long

    /**
     * @Name  current
     * @Type  Boolean
     * @Note  是否使用当前时间
     */
    private var current :Boolean

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:12
     * @Note   构造
     * @param  timeLength 时长 单位：秒
     * @param  current  是否使用当前时间差
     */
    private constructor(timeLength: Long,current :Boolean = false) {
        this.timeLength = timeLength
        this.timeResult = timeLength
        this.current = current
    }

    fun alive() :Boolean{
        return survival
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:13
     * @Note   开始执行
     * @param  call 回调
     */
    fun execute(call :(day :Int,hour:Int,min:Int,sec :Int)->Unit , complete :(()->Unit)? = null){

        var run = true

        if (isOnlySingleProcess){
            if (workState){
                return
            }
        }

        if (complete != null){
            this.complete = complete
        }

        workState = true

        threadPool.execute {
            if (!run){
                return@execute
            }
            while (survival && timeResult>0){
                if (current){
                    timeResult = (timeLength - System.currentTimeMillis() / 1000)
                }else{
                    timeResult-=1
                }
                call.invoke(getDay(timeResult),getHour(timeResult),getMinute(timeResult),getSecond(timeResult))
                try {
                    Thread.sleep(1000)
                }catch (e :InterruptedException){
                    debugLog("TimeEngine","线程池停止")
                }
                if (timeResult == 0L){
                    this.complete?.invoke()
                    workState = false
                    run = false
                }
            }
        }

    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/21 下午3:01
     * @Note   设置complete监听
     * @param  complete 监听高级函数
     */
    fun setComplete(complete: (() -> Unit)){
        this.complete = complete
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:29
     * @Note   恢复初始状态
     */
    fun reset(){
        timeResult = timeLength
        survival = true
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:20
     * @Note   获取小时
     * @param  time 总时间
     */
    private fun getHour(time: Long): Int {
        return (time % (60 * 60 * 24) / (60 * 60)).toInt()
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:21
     * @Note   获取分钟
     * @param  time 总时间
     */
    private fun getMinute(time: Long): Int {
        return (time % (60 * 60 * 24) % (60 * 60) / 60).toInt()
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:21
     * @Note   获取秒
     * @param  time 总时长
     */
    private fun getSecond(time: Long): Int {
        return (time % (60 * 60 * 24) % (60 * 60) % 60).toInt()
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:22
     * @Note   获取天数
     * @param  time 总时长
     */
    private fun getDay(time: Long): Int {
        return (time / (60 * 60 * 24)).toInt()
    }

    /**
     * @author LDD
     * @From   TimeEngine
     * @Date   2018/5/10 上午9:14
     * @Note   销毁TimeEngine
     */
    fun destory(){
        if (survival){
            workState = false
            survival = false
            threadPool.shutdownNow()
        }
    }
}