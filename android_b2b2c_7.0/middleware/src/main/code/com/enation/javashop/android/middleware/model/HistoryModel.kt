package com.enation.javashop.android.middleware.model

/**
 * 搜索历史模型
 */
data class HistoryModel(val searchMode:Boolean , val text :String) {

    fun getViewText() :String{
        if (searchMode) {
            return text
        }else {
            return " \"$text\" 店铺"
        }
    }

}