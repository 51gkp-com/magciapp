package com.enation.javashop.android.component.goods.launch

import com.enation.javashop.android.component.goods.di.GoodsComponent
import com.enation.javashop.android.component.goods.di.DaggerGoodsComponent
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseLaunch
import com.enation.javashop.android.lib.utils.ChinaeseSortHelper
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.middleware.di.DaggerManager

/**
 * @author LDD
 * @Date   2018/3/9 上午9:06
 * @From   com.enation.javashop.android.component.goods.launch
 * @Note   商品模块启动入口
 */
@Router(path = "/goods/launch")
class GoodsLaunch : BaseLaunch() {

    companion object {
        lateinit var component :GoodsComponent
    }

    override fun moduleInit() {
        component = DaggerGoodsComponent.builder()
                .applicationComponent(DaggerManager.APPLICATION_COMPONENT)
                .build()
    }
}