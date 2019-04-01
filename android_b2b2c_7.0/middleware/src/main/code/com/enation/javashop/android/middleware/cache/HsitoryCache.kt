package com.enation.javashop.android.middleware.cache

import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.middleware.model.HistoryModel
import com.enation.javashop.utils.base.cache.ACache
import com.google.gson.Gson

/**
 * Created by LDD on 2018/10/15.
 */
class HsitoryCache() {

    private val cache = ACache.get(BaseApplication.appContext)

    private val jsonFormat = Gson()

    private val HISTORY_CACHE_KEY = "HISTORY_CACHE_KEY"

    fun add(item :HistoryModel){
        var json :String? = cache.getAsString(HISTORY_CACHE_KEY)
        if (json == null){
            val data = HistoryDisk(arrayListOf(item))
            val newJson = jsonFormat.toJson(data)
            cache.put(HISTORY_CACHE_KEY,newJson)
        }else{
            var data = jsonFormat.fromJson(json,HistoryDisk::class.java)
            var deleteItems = ArrayList<HistoryModel>()
            data.data.forEach { child ->
                if (item.text == child.text){
                    deleteItems.add(child)
                }
            }
            data.data.removeAll(deleteItems)
            data.data.add(0,item)
            val newJson = jsonFormat.toJson(data)
            cache.put(HISTORY_CACHE_KEY,newJson)
        }
    }

    fun clear(){
        cache.remove(HISTORY_CACHE_KEY)
    }

    fun get():ArrayList<HistoryModel>{
        var data = ArrayList<HistoryModel>()
        var json :String? = cache.getAsString(HISTORY_CACHE_KEY)
        if (json != null){
            var history = jsonFormat.fromJson(json,HistoryDisk::class.java)
            data = history.data
        }
        return  data
    }

    fun delete(item :HistoryModel){
        var json :String? = cache.getAsString(HISTORY_CACHE_KEY)
        if (json != null){
            var data = jsonFormat.fromJson(json,HistoryDisk::class.java)
            var deleteItems = ArrayList<HistoryModel>()
            data.data.forEach { child ->
                if (item.text == child.text){
                    deleteItems.add(child)
                }
            }
            data.data.removeAll(deleteItems)
            val newJson = jsonFormat.toJson(data)
            cache.put(HISTORY_CACHE_KEY,newJson)
        }
    }



}

class HistoryDisk(var data :ArrayList<HistoryModel>)