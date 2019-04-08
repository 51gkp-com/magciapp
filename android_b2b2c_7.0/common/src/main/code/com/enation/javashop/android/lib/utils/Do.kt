package com.enation.javashop.android.lib.utils

import android.os.Looper
import java.util.concurrent.Executors

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:06
 * @From   com.enation.javashop.android.lib.utils
 * @Note   快速切换线程工具类 保证线程执行顺序
 */
  class Do :SendResultInterface,NomalResultInterface {

    /**
     * 伴生对象
     */
    companion object {

        /**
         * @author  LDD
         * @From   com.enation.javashop.android.lib.utils.Do Companion
         * @Data   2017/12/22 下午4:09
         * @Note   初始化
         */
        fun prepare(): NomalResultInterface {
            return Do()
        }
    }

    /**
     * @Name  threadPool
     * @Type  ExecutorService
     * @Note  可缓存线程池，用于处理非UI线程任务
     */
    private var threadPool = Executors.newFixedThreadPool(1)

    /**
     * @Name  threadQueue
     * @Type  ArrayList<Any>
     * @Note  任务序列
     */
    private var threadQueue : ArrayList<Any> = ArrayList()

    /**
     * @Name  onUI
     * @Type  Int
     * @Note  UI线程处理标记
     */
    private val onUI = 1

    /**
     * @Name  onThread
     * @Type  Int
     * @Note  非UI线程处理标记
     */
    private val onThread = 2

    /**
     * @Name  threadPool
     * @Type  Handler
     * @Note  UI任务处理Hander
     */
    private val uiThread = android.os.Handler(Looper.getMainLooper())

    /**
     * @Name  index
     * @Type  int
     * @Note  处理标记，编辑当前处理到第几个任务
     */
    private var index= 0

    /**
     * @Name  runFlag
     * @Type  boolean
     * @Note  标识当前任务序列是否执行完成
     */
    private var runFlag = false

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:16
     * @Note   开始执行方法 递归处理事件队列
     */
    override fun execute(){
        if (threadQueue.size>0){
            val data = threadQueue[index]
            if (runFlag){
                execute()
                return
            }
            runFlag = true
            when (data) {
                is DoModel -> {
                    nomalDo()
                }
                is DoSendModel -> {
                    sendDo()
                }
            }
        }
    }
    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:17
     * @Note   常规事件执行方法
     */
    private fun nomalDo(){
        val data:DoModel = threadQueue[index] as DoModel
        if (data.thread == onUI){
            uiThread.post{
                data.call.invoke {
                    index+=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoModel -> {
                                nomalDo()
                            }
                            is DoSendModel -> {
                                sendDo()
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }else if(data.thread == onThread){
            threadPool.execute {
                data.call.invoke {
                    index+=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoModel -> {
                                nomalDo()
                            }
                            is DoSendModel -> {
                                sendDo()
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:18
     * @Note   发送参数事件处理方法
     */
    private fun sendDo(){
        val data:DoSendModel = threadQueue[index] as DoSendModel
        if (data.thread == onUI){
            uiThread.post{
                data.call.invoke {
                    value ->
                    index +=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoResultModel ->{
                                resultDo(value)
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }else if(data.thread == onThread){
            threadPool.execute {
                data.call.invoke {
                    value ->
                    index +=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoResultModel ->{
                                resultDo(value)
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:19
     * @Note   接受参数事件处理方法
     * @param  value 所接受的参数
     */
    private fun resultDo(value :Any){
        val data:DoResultModel = threadQueue[index] as DoResultModel
        if (data.thread == onUI){
            uiThread.post{
                data.call.invoke(value) {
                    index +=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoModel -> {
                                nomalDo()
                            }
                            is DoSendModel -> {
                                sendDo()
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }else if(data.thread == onThread){
            threadPool.execute {
                data.call.invoke(value) {
                    index +=1
                    if (threadQueue.size > index){
                        when (threadQueue[index]) {
                            is DoModel -> {
                                nomalDo()
                            }
                            is DoSendModel -> {
                                sendDo()
                            }
                        }
                    }else{
                        clear()
                    }
                }
            }
        }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:20
     * @Note   清除线程池 索引 复原运行标记
     */
    private fun clear(){
        index = 0
        threadQueue.clear()
        runFlag = false
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:20
     * @Note   在UI主线程执行普通方法
     * @param  call 方法回调
     */
    override fun doOnUI(call: (call :() -> Unit) -> Unit):NomalResultInterface{
        threadQueue.add(DoModel(onUI,call))
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:33
     * @Note   在非UI线程执行普通方法
     * @param  call 方法回调
     */
    override fun doOnBack(call: (call :() -> Unit) -> Unit):NomalResultInterface{
        threadQueue.add(DoModel(onThread,call))
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:37
     * @Note   在UI线程执行传递参数方法
     * @param  call 方法回调
     */
    override fun onUISend(call: ((value :Any) -> Unit) -> Unit):SendResultInterface{
        threadQueue.add(DoSendModel(onUI,call))
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:37
     * @Note   在非UI线程执行传递参数方法
     * @param  call 方法回调
     */
    override fun onBackSend(call: ((value :Any) -> Unit) -> Unit):SendResultInterface{
        threadQueue.add(DoSendModel(onThread,call))
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:38
     * @Note   在主线程执行接受参数方法
     * @param  call 方法回调
     */
    override fun onUIResult(call: (Any,() -> Unit) -> Unit):NomalResultInterface{
        threadQueue.add(DoResultModel(onUI,call))
        return this
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils Do
     * @Data   2017/12/22 下午4:39
     * @Note   在非UI线程执行结束参数方法
     * @param  call 方法回调
     */
    override fun onBackResult(call: (Any,() -> Unit) -> Unit):NomalResultInterface{
        threadQueue.add(DoResultModel(onThread,call))
        return this
    }

}

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:42
 * @From   com.enation.javashop.android.lib.utils
 * @Note   执行完发送参数方法后 返回对应接口
 */
interface SendResultInterface {
    fun onBackResult (call: (Any,() -> Unit) -> Unit):NomalResultInterface
    fun onUIResult(call: (Any,() -> Unit) -> Unit):NomalResultInterface
}

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:42
 * @From   com.enation.javashop.android.lib.utils
 * @Note   执行完普通方法和接受参数方法后 返回对应接口
 */
interface NomalResultInterface{
    fun onBackSend(call: ((value :Any) -> Unit) -> Unit):SendResultInterface
    fun onUISend(call: ((value :Any) -> Unit) -> Unit):SendResultInterface
    fun doOnBack(call: (call :() -> Unit) -> Unit):NomalResultInterface
    fun doOnUI(call: (call :() -> Unit) -> Unit):NomalResultInterface
    fun execute()
}

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:43
 * @From   com.enation.javashop.android.lib.utils
 * @Note   普通方法 Model
 */
data class DoModel constructor(var thread :Int, var call :(() -> Unit) -> Unit)

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:43
 * @From   com.enation.javashop.android.lib.utils
 * @Note   发送参数方法 Model
 */
data class DoSendModel constructor(var thread :Int, var call :((value : Any) -> Unit) -> Unit)

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:43
 * @From   com.enation.javashop.android.lib.utils
 * @Note   接收参数方法 Model
 */
data class DoResultModel constructor(var thread :Int, var call :(result :Any,() -> Unit) -> Unit)