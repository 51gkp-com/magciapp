package com.enation.javashop.android.component.shop.activity

import android.graphics.Color
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.agreement.ShopCategoryAgreement
import com.enation.javashop.android.component.shop.databinding.ShopCategoryLayBinding
import com.enation.javashop.android.component.shop.fragment.ShopCategoryFragment
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.middleware.event.ShopCategorySelectEvent
import com.enation.javashop.android.middleware.logic.contract.shop.ShopCategoryActivityContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopCategoryActivityPresenter
import com.enation.javashop.android.middleware.model.ShopCategoryViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.shop_category_lay.*

/**
 * @author LDD
 * @Date   2018/4/12 上午9:52
 * @From   com.enation.javashop.android.component.shop.activity
 * @Note   店铺分类页面
 */
@Router(path = "/shop/category")
class ShopCategoryActivity : BaseActivity<ShopCategoryActivityPresenter, ShopCategoryLayBinding>(), ShopCategoryActivityContract.View, ShopCategoryAgreement {

    /**
     * @Name  shopId
     * @Type  Int
     * @Note  店铺id（自动注入）
     */
    @Autowired(name= "shopId",required = true)
    @JvmField var shopId: Int = 0

    /**
     * @Name  fragCount
     * @Type  Int
     * @Note  fragment加载个数
     */
    private var fragCount = 1

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:33
     * @Note   提供layoutId
     */
    override fun getLayId(): Int {
        return R.layout.shop_category_lay
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:34
     * @Note   依赖注入
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:35
     * @Note   初始化操作
     * @param
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.loadCategory(shopId)
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:35
     * @Note   绑定事件
     */
    override fun bindEvent() {
        shop_category_header.setLeftClickListener {
            if (fragCount > 1) {
                fragCount -= 1
                supportFragmentManager.popBackStack()
                return@setLeftClickListener
            }
            pop()
        }
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:36
     * @Note   初始化分类
     * @param
     */
    override fun initCategory(data: ArrayList<ShopCategoryViewModel>) {
        val fragment = ShopCategoryFragment()
        fragment.data = data
        supportFragmentManager.beginTransaction().add(R.id.shop_category_placeholder, fragment, "$fragCount").commit()
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:37
     * @Note   当还存在下层分类数据时 回调该方法 push新的Fragment
     * @param  data 分类数据
     */
    override fun pushFragment(data: ArrayList<ShopCategoryViewModel>) {
        val fragment = ShopCategoryFragment()
        fragment.data = data
        fragCount += 1
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                .add(R.id.shop_category_placeholder, fragment, "$fragCount")
                .addToBackStack(null)
                .commit()
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:38
     * @Note   pushActivity 成功选择某分类进行后调
     * @param  data 选中的分类Item
     */
    override fun pushActivity(data: ShopCategoryViewModel) {
        JavaShopActivityTask.instance.getActivityArray().forEach {
            if (it.get() is ShopSearchGoodsActivity) {
                getEventCenter().post(ShopCategorySelectEvent("${data.cId}", data.cName))
                pop()
                return
            }
        }
        push("/shop/search",{postcard ->
            postcard.withInt("shopId", shopId)
            postcard.withString("category", "${data.cId}")
        })
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:39
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:41
     * @Note   完成回调
     * @param  message 完成回调
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:41
     * @Note   开始回调
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:41
     * @Note   网络实时回调
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:42
     * @Note   重写退出键监听
     */
    override fun onBackPressed() {
        if (fragCount > 1) {
            fragCount -= 1
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

    /**
     * @author LDD
     * @From   ShopCategoryActivity
     * @Date   2018/4/24 下午2:43
     * @Note   销毁回调
     */
    override fun destory() {

    }
}