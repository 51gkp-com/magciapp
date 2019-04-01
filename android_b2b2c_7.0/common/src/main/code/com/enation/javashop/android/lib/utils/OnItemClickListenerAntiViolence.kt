package com.enation.javashop.android.lib.utils

import android.view.View
import android.widget.AdapterView

/**
 * @author  LDD
 * @Data   2018/1/5 上午10:37
 * @From   com.enation.javashop.android.lib.utils
 * @Note   防暴力点击 ItemClickListener
 */
class OnItemClickListenerAntiViolence(val event :((adapterView: AdapterView<*>?, itemView: View?, position: Int, itemId: Long) -> Unit),val interval: Int = 1000) :AdapterView.OnItemClickListener {

    /**
     * @Name  time
     * @Type  Long
     * @Note  点击事件记录
     */
    private var time :Long = -12345678910

    /**
     * @Name  index
     * @Type  Int
     * @Note  点击下标记录
     */
    private var index :Int = -1

    /**
     * @author LDD
     * @From   OnItemClickListenerAntiViolence
     * @Data   2018/1/5 上午10:18
     * @Note   Item点击事件
     * @param  p0    列表视图
     * @param  p1    所点击的视图
     * @param  p2    position
     * @param  p3    item ID
     */
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (index == p2){
            if (time == -12345678910){
                index = p2
                event(p0,p1,p2,p3)
                time = System.currentTimeMillis()
            }else{
                if (System.currentTimeMillis()-time >interval){
                    index = p2
                    event(p0,p1,p2,p3)
                    time = System.currentTimeMillis()
                }
            }
        }else{
            index = p2
            event(p0,p1,p2,p3)
            time = System.currentTimeMillis()
        }
    }
}