package com.enation.javashop.android.lib.utils

import android.util.Log
import android.view.View

/**
 * @author LDD
 * @Data   2018/1/5 上午10:01
 * @From   com.enation.javashop.android.lib.utils
 * @Note
 */
class OnClickListenerAntiViolence(val event:((view :View) -> Unit),val interval : Int = 1000) :View.OnClickListener {

    /**
     * @Name  time
     * @Type  Long
     * @Note  点击事件记录
     */
    private var time :Long = -12345678910


    /**
     * @author  LDD
     * @From    OnClickListenerAntiViolence
     * @Data   2018/1/5 上午10:04
     * @Note   点击相应
     * @param  view  所点击的View
     */
    override fun onClick(view: View) {

        if (time == -12345678910){
            time = System.currentTimeMillis()
            event(view)
        }else if (System.currentTimeMillis()-time > interval){
            time = System.currentTimeMillis()
            event(view)
        }
    }
}