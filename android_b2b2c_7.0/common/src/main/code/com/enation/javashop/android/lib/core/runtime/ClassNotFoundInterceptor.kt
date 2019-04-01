package com.enation.javashop.android.lib.core.runtime

import android.content.Context
import android.content.Intent

/**
 * @author  LDD
 * @Data   2017/12/26 上午8:56
 * @From   com.enation.javashop.android.lib.core.runtime
 * @Note   拦截器接口
 */
interface ClassNotFoundInterceptor {

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.runtime ClassNotFoundInterceptor
     * @Data   2017/12/26 上午8:57
     * @Note   拦截器回调方法
     * @param  context 上下文
     * @param  intent  Intent
     */
    fun call(context:Context,intent :Intent?):Intent?
}