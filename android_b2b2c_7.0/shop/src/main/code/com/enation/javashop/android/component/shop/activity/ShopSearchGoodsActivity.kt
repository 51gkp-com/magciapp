package com.enation.javashop.android.component.shop.activity

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopSearchGoodsLayBinding
import com.enation.javashop.android.component.shop.fragment.ShopAllFragment
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.DisposableManager
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.ShopCategorySelectEvent
import com.enation.javashop.utils.base.tool.BaseToolActivity
import kotlinx.android.synthetic.main.shop_search_goods_lay.*

/**
 * @author LDD
 * @Date   2018/4/11 下午4:49
 * @From   com.enation.javashop.android.component.shop.activity
 * @Note   店铺商品搜索列表
 */
@Router(path = "/shop/search")
class ShopSearchGoodsActivity : BaseToolActivity() {

    /**
     * @Name  shopId
     * @Type  Int
     * @Note  店铺id（自动注入）
     */
    var shopId: Int = 0

    /**
     * @Name category
     * @Type String
     * @Note 店铺分类id
     */
    var category: String = ""

    /**
     * @Name keyword
     * @Type String
     * @Note 搜索关键字
     */
    var keyword: String = ""

    /**
     * @Name  diaposableManager
     * @Type  DisposableManager
     * @Note  Rx索引管理器
     */
    private val diaposableManager = DisposableManager()

    /**
     * @Name  fragment
     * @Type  ShopAllFragment
     * @Note  店铺商品Fragment
     */
    private val fragment by lazy {
        ShopAllFragment().then {
            it.shopId = shopId
            it.category = category
            it.keyword = keyword
        }
    }

    /**
     * @Name  binding
     * @Type  ShopSearchGoodsLayBinding
     * @Note  视图绑定
     */
    private val binding by lazy {
        DataBindingUtil.bind<ShopSearchGoodsLayBinding>(layoutInflater.inflate(R.layout.shop_search_goods_lay, null))
    }

    /**
     * @author LDD
     * @From   ShopSearchGoodsActivity
     * @Date   2018/4/24 下午2:53
     * @Note   初始化创建
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = intent.getIntExtra("shopId", 0)
        if(intent.hasExtra("category")) {
            category = intent.getStringExtra("category")
        }
        if(intent.hasExtra("keyword")) {
            keyword = intent.getStringExtra("keyword")
        }

        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.shop_search_goods_frag_placeholder, fragment, "detail").commit()
        bindEvent()
    }

    /**
     * @author LDD
     * @From   ShopSearchGoodsActivity
     * @Date   2018/4/24 下午2:54
     * @Note   绑定事件
     */
    fun bindEvent() {
        getEventCenter().register(ShopCategorySelectEvent::class.java) { data ->
            fragment.refreshData(data.category, 2)
        }.joinManager(diaposableManager)
        shop_search_goods_category.setOnClickListener {
            push("/shop/category", {postcard ->
                postcard.withInt("shopId", shopId)
            })
        }
        shop_search_goods_cancel.setOnClickListener {
            pop()
        }
        if(keyword != null){
            shop_search_goods_title_tv.text = keyword
        }
    }

    /**
     * @author LDD
     * @From   ShopSearchGoodsActivity
     * @Date   2018/4/24 下午2:54
     * @Note   销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        diaposableManager.unDisposable()
    }
}