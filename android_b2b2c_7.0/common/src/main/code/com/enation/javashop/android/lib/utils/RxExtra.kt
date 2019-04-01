package com.enation.javashop.android.lib.utils

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * Created by LDD on 2018/11/9.
 */
object RxExtra {

    fun createNetErrorObservable (message :String) : Observable<ResponseBody>{
        return  Observable.create { observableEmitter ->
            observableEmitter.onError(Throwable(message))
        }
    }


}