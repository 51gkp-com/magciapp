package com.enation.javashop.android.middleware.router

import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.enation.javashop.android.jrouter.external.annotation.Interceptor
import com.enation.javashop.android.jrouter.logic.datainfo.Postcard
import com.enation.javashop.android.jrouter.logic.listener.InterceptorListener
import com.enation.javashop.android.jrouter.logic.template.BaseInterceptor
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask

@Interceptor(priority = 1)
class RouterInterceptor : BaseInterceptor{

    override fun process(postcard: Postcard, callback: InterceptorListener) {
        if(postcard.path.contains("extra/scan")){
            if (ContextCompat.checkSelfPermission(JavaShopActivityTask.instance.peekTopActivity()!!, "android.permission.CAMERA") != 0) {
                ActivityCompat.requestPermissions(JavaShopActivityTask.instance.peekTopActivity()!!, arrayOf("android.permission.CAMERA"), 1)
            }else{
                callback.onContinue(postcard)
            }
        }else{
            callback.onContinue(postcard)
        }
    }

    override fun init(context: Context?) {

    }

}