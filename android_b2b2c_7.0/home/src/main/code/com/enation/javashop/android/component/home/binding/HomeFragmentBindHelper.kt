package com.enation.javashop.android.component.home.binding

import android.databinding.ObservableField



/**
 * @author LDD
 * @From   com.enation.javashop.android.component.home.binding
 * @Date   2018/1/30 下午3:03
 * @Note   首页UIVM
 */
class HomeFragmentBindHelper {

    /**
     * @Name  scrollY
     * @Type  ObservableField<Int>
     * @Note  滑动距离
     */
    val scrollY = ObservableField(0)


    /**
     * @Name  alpha
     * @Type  ObservableField<Int>
     * @Note  搜索栏alpha
     */
    val alpha = ObservableField(0)

    /**
     * @Name  image
     * @Type  String
     * @Note  测试图片
     */
    var image = "http://onghqryqs.bkt.clouddn.com/ChMkJlauzbOIb6JqABF4o12gc_AAAH9HgF1sh0AEXi7441.jpeg"

    /**
     * @Name  isHide
     * @Type  ObservableField<Boolean>
     * @Note  是否隐藏
     */
    val isHide  = ObservableField(true)


}