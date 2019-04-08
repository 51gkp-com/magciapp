package com.enation.javashop.android.middleware.bind

/**
 * Created by LDD on 2018/4/8.
 */
data class ViewAttrModel(val percent : Double,val prop:Double){
    companion object {
        @JvmStatic
        fun build(percent : Double,prop:Double):ViewAttrModel{
            return ViewAttrModel(percent,prop)
        }
    }
}