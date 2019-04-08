package com.enation.javashop.android.middleware.model

/**
 * @author LDD
 * @Date   2018/4/12 上午9:27
 * @From   com.enation.javashop.android.middleware.model
 * @Note   店铺分类VM
 */
data class ShopCategoryViewModel (val cId :Int ,       /**分类ID*/
                                  val cName :String ,  /**分类名称*/
                                  val child :ArrayList<ShopCategoryViewModel>?) /**子分类*/