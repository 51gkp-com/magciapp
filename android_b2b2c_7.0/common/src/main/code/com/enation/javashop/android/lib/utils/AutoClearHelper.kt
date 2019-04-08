package com.enation.javashop.android.lib.utils

import android.util.Log
import java.util.IdentityHashMap


/**
 * @author  LDD
 * @Data   2017/12/21 下午5:25
 * @Note   自动清除数据辅助类 在Activity销毁时，自动处理其中参数，作用于该类
 * {@link com.enation.javashop.android.lib.utils.AutoClearValue}
 */
class AutoClearHelper {

    companion object {
        val intance:AutoClearHelper by lazy {  AutoClearHelper() }
    }

    private constructor()

    /**
     * 储存
     */
    private var valueMap : IdentityHashMap<String, () -> Unit> = IdentityHashMap()

    /**
     * 创建
     */
    fun create(key:String,value:()->Unit){
        valueMap.put(key,value)
    }

    /**
     * Activity销毁时调用
     */
    fun destory(name :String){
        valueMap.keys.filter { it == name }.forEach { valueMap[it]!!.invoke() }
        valueMap.clear()
    }

    /**
     * 获取当前AutoValuemap中Value总数
     */
    fun valueCount():Int{
        return valueMap.size
    }
}
