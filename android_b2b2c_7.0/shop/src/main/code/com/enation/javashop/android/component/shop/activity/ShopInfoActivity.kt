package com.enation.javashop.android.component.shop.activity

import android.graphics.Bitmap
import android.graphics.Color
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopInfoActLayBinding
import com.enation.javashop.android.component.shop.launch.ShopLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.logic.contract.shop.ShopInfoActivityContract
import com.enation.javashop.android.middleware.logic.presenter.shop.ShopInfoPresenter
import com.enation.javashop.android.middleware.model.ShopViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.shop_info_act_lay.*

/**
 * @author LDD
 * @Date   2018/4/24 下午2:44
 * @From   com.enation.javashop.android.component.shop.activity
 * @Note   店铺信息页面
 */
@Router(path = "/shop/info")
class ShopInfoActivity : BaseActivity<ShopInfoPresenter, ShopInfoActLayBinding>(), ShopInfoActivityContract.View {

    /**
     * @Name  shopId
     * @Type  Int
     * @Note  店铺id（自动注入）
     */
    @Autowired(name = "shopId", required = true)
    @JvmField
    var shopId: Int = 0

    /**
     * @author LDD
     * @From    ShopInfoActivity
     * @Date   2018/4/24 下午2:44
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.shop_info_act_lay
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:45
     * @Note   依赖注入
     */
    override fun bindDagger() {
        ShopLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:45
     * @Note   初始化操作
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        presenter.loadShopInfo(shopId)
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:45
     * @Note   绑定事件
     */
    override fun bindEvent() {
        mViewBinding.shopInfoActTopbar.setLeftClickListener {
            pop()
        }.setRightClickListener {
            presenter.share(this,
                    JavaShopConfigCenter.INSTANCE.WAP_SELLER_URL+"$shopId",
                    mViewBinding.data.logo,"JavaShop网商店铺",mViewBinding.data.name)
        }
        mViewBinding.shopInfoCollectTop.setOnClickListener {
            presenter.collectShop(shopId,!mViewBinding.data.favorited)
        }
        ecode_lay.setOnClickListener {
            ecode_lay.gone()
        }
        ecode_title.setOnClickListener {
            presenter.buildQrCode(shopId)
        }
        ecode_title.setOnClickListener {
            presenter.buildQrCode(shopId)
        }
        ecode_action_iv.setOnClickListener {
            presenter.buildQrCode(shopId)
        }
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:46
     * @Note   初始化店铺信息
     * @param  data
     */
    override fun initShopInfo(data: ShopViewModel) {
        mViewBinding.data = data
        if (data.favorited){
            mViewBinding.shopInfoCollectTop.setImageResource(R.drawable.javashop_icon_collected)
        }else{
            mViewBinding.shopInfoCollectTop.setImageResource(R.drawable.javashop_icon_uncollect)
        }
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:47
     * @Note   显示二维码
     * @param  bitmap 二维码
     */
    override fun showQrCode(bitmap: Bitmap) {
        ecode_lay.visable()
        ecode_iv.setImageBitmap(bitmap)
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:48
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:48
     * @Note   完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
        if (message == "关注成功"){
            mViewBinding.shopInfoCollectTop.setImageResource(R.drawable.javashop_icon_collected)
            mViewBinding.data.favorited = true
        }else if(message == "取消关注成功"){
            mViewBinding.shopInfoCollectTop.setImageResource(R.drawable.javashop_icon_uncollect)
            mViewBinding.data.favorited = false
        }
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:48
     * @Note   开始
     */
    override fun start() {
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:49
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   ShopInfoActivity
     * @Date   2018/4/24 下午2:49
     * @Note   销毁
     */
    override fun destory() {

    }
}