package com.enation.javashop.android.lib.utils

import android.content.Context
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.VoiNetTool
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException

/**
 * @author  LDD
 * @Data   2017/12/22 下午3:43
 * @From   com.enation.javashop.android.lib.utils
 * @Note   可以监听网络状态的观察者
 */
abstract class ConnectionObserver<T> : BaseObserver<T>(BaseApplication.appContext) {

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:44
     * @Note   实现onStar方法 在此开始执行网络监听 并执行onStartWithConnection抽象方法
     */
    override fun onStart() {
        ConnectionQualityMonitor.INSTANCE.startSample()
        onStartWithConnection()
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:46
     * @Note   实现onNext方法 在此停止网络监听 并执行onNextWithConnection
     *           connectionBitsOfSecond方法 传递网络状态，网络速率
     */
    override fun onNext(p0: T) {
        ConnectionQualityMonitor.INSTANCE.stopSample()
        onNextWithConnection(p0,ConnectionQualityMonitor.INSTANCE.getConnectionQuanlity())
        connectionBitsOfSecond(ConnectionQualityMonitor.INSTANCE.getConnectionBits())
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:50
     * @Note   实现onError方法 传递网络状态，网络速率
     */
    override fun onError(p0: ExceptionHandle.ResponeThrowable) {
        ConnectionQualityMonitor.INSTANCE.stopSample()
        onErrorWithConnection(p0,ConnectionQualityMonitor.INSTANCE.getConnectionQuanlity())
        connectionBitsOfSecond(ConnectionQualityMonitor.INSTANCE.getConnectionBits())
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:51
     * @Note   传递当前网络速率 需要继承者重写
     * @param  bits 网络速率
     */
    open fun connectionBitsOfSecond(bits: Double){}

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:53
     * @Note   抽象方法，子类可以监听网络请求开始，并可以获得当前网络状态
     */
    abstract fun onStartWithConnection()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:55
     * @Note   抽象方法，子类可以监听网络请求成功，并获取当前网络状态
     * @param  result 网络请求结果
     * @param  connectionQuality 网络状态
     */
    abstract fun onNextWithConnection(result:T,connectionQuality :ConnectionQuality)

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.utils ConnectionObserver
     * @Data   2017/12/22 下午3:56
     * @Note   抽象方法，子类可以监听网络请求失败，并获取当前网络状态
     * @param  error 自定义网络错误
     * @param  connectionQuality 当前网络状态
     */
    abstract fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable,connectionQuality :ConnectionQuality)
}

abstract class BaseObserver<T>(private val context: Context) : Observer<T> {

    override fun onSubscribe(disposable: Disposable) {
        if (VoiNetTool.getAPNType(this.context) == VoiNetTool.netType.noneNet) {
            disposable.dispose()
            this.onNoneNet()
        } else {
            this.onStart()
            this.attachSubscribe(disposable)
        }

    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {
            var errorJSon = e.response().errorBody()!!.getJsonString()
            if (errorJSon.contains("html")){
                this.onError(ExceptionHandle.handleException(e))
                return
            }
            val obj = JSONObject(errorJSon)
            if (obj.has("code")){
                val result = ExceptionHandle.ResponeThrowable(e,420)
                result.customMessage = obj.getString("message")
                this.onError(result)
            }else{
                this.onError(ExceptionHandle.handleException(e))
            }
        } else {
            this.onError(ExceptionHandle.ResponeThrowable(e, 1000).then {
                if (e.message == null) {
                    it.customMessage = "加载失败！"
                } else {
                    it.customMessage = e.message
                }
            })
        }

    }

    abstract fun onError(var1: ExceptionHandle.ResponeThrowable)

    abstract fun onStart()

    abstract fun attachSubscribe(var1: Disposable)

    open fun onNoneNet() {}

    override fun onComplete() {}
}