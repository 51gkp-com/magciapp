package com.enation.javashop.android.lib.utils

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination


/**
 * @author LDD
 * @Date   2018/3/27 下午5:46
 * @From   com.enation.javashop.android.lib.utils
 * @Note   获取汉字首字母
 */
object ChinaeseSortHelper {
    fun getFirstSpell(string: String): String {
        val pybf = StringBuffer()
        val arr = string.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (i in arr.indices) {
            if (arr[i].toInt() > 128) { //如果已经是字母就不用转换了
                try {
                    //获取当前汉字的全拼
                    val temp = PinyinHelper.toHanyuPinyinStringArray(
                            arr[i], defaultFormat)
                    if (temp != null) {
                        pybf.append(temp[0][0])// 取首字母
                    }
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    e.printStackTrace()
                }

            } else {
                if (arr[i] in 'a'..'z') {
                    arr[i].minus(32)
                }
                pybf.append(arr[i])
            }
        }
        return pybf.toString().substring(0,1).toUpperCase()
    }

}
