package com.enation.javashop.android.lib.utils

import java.util.regex.Pattern

/**
 * Created by LDD on 2018/11/16.
 */
class RegularHelper {

    fun getMiddleString(content : String , start :String , endString: String , first :Boolean = false) : ArrayList<String>  {

        val result = ArrayList<String>()

        val pattern = Pattern.compile("$start(.*?)$endString")

        val finder = pattern.matcher(content)

        while (finder.find()){
            result.add(finder.group(1))
        }

        return result

    }

}