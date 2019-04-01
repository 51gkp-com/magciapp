package com.enation.javashop.android.lib.widget

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
import com.enation.javashop.android.lib.utils.errorLog

/**
 * @author LDD
 * @Date   2018/4/10 下午6:02
 * @From   com.enation.javashop.android.lib.widget
 * @Note   兼容android7.0以上
 */
open class PopWindowCompatible : PopupWindow {


    constructor(width: Int, height: Int) : super(width, height)


    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val realdis = DisplayMetrics()
            if(isNavigationBarExist(anchor.context as Activity)){
                JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getMetrics(realdis)
            }else{
                JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getRealMetrics(realdis)
            }
            val h = realdis.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor, xoff, yoff)
    }

    override fun showAsDropDown(anchor: View) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            var realdis  = DisplayMetrics()
            if(isNavigationBarExist(anchor.context as Activity)){
                JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getMetrics(realdis)
            }else{
                JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getRealMetrics(realdis)
            }
            val h = realdis.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }


    private val NAVIGATION = "navigationBarBackground"

    fun isNavigationBarExist(activity: Activity): Boolean {
        val vp = activity.window.decorView as ViewGroup
            for (i in 0 until vp.childCount) {
                vp.getChildAt(i).context.packageName
                if (vp.getChildAt(i).id != -1 && NAVIGATION == activity.resources.getResourceEntryName(vp.getChildAt(i).id)) {
                    return true
                }
            }
        return false
    }
}