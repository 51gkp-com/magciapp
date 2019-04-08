package com.enation.javashop.android.lib.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author LDD
 * @Date   2018/4/11 下午4:33
 * @From   com.enation.javashop.android.lib.base
 * @Note   RX引用管理器
 */
open class DisposableManager {

    /**
     * @Name  compositeDisposable
     * @Type  io.reactivex.disposables.CompositeDisposable
     * @Note  Disposable管理器
     */
    protected var compositeDisposable: CompositeDisposable? = null


    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.DisposableManager
     * @Data   2017/12/26 下午12:28
     * @Note   移除监听
     */
    fun unDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }

    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.DisposableManager
     * @Data   2017/12/26 下午12:29
     * @Note   添加Disposable
     * @param  disposable 监听引用
     */
    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

}