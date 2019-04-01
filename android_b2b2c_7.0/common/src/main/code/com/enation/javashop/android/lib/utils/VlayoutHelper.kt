package com.enation.javashop.android.lib.utils

import com.alibaba.android.vlayout.layout.LinearLayoutHelper

/**
 * @author LDD
 * @Date   2018/4/23 上午11:18
 * @From   com.enation.javashop.android.lib.utils
 * @Note   Vlayout工具类
 */
object VlayoutHelper {

    /**
     * @author LDD
     * @From   VlayoutHelper
     * @Date   2018/4/23 上午11:22
     * @Note   快速构建列表Layhelper
     * @param  diviHeight  间距
     * @param  size  item总数
     * @param  left   左侧margin  默认0
     * @param  right  右侧margin  默认0
     * @param  top    上方margin  默认10dp
     * @param  bottom 下方margin  默认0
     */
    @JvmStatic
    fun createLinearLayoutHelper(diviHeight : Int = 0 ,
                                 size :Int = 1 ,
                                 left :Int = 0,
                                 right :Int = 0,
                                 top :Int = AppTool.SystemUI.dpToPx(10F),
                                 bottom :Int = 0):LinearLayoutHelper{
        return LinearLayoutHelper(diviHeight,size).then {
                self ->
                self.setMargin(left,top,right,bottom)
        }
    }

}