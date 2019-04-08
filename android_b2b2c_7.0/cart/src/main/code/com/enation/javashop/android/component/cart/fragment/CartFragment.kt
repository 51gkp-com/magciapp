package com.enation.javashop.android.component.cart.fragment

import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.activity.CartActivity
import com.enation.javashop.android.component.cart.adapter.CartGoodsItemAdapter
import com.enation.javashop.android.component.cart.adapter.CartPromotionItemAdapter
import com.enation.javashop.android.component.cart.adapter.CartShopItemAdapter
import com.enation.javashop.android.component.cart.adapter.CartSinglePromotionItemAdapter
import com.enation.javashop.android.component.cart.databinding.CartFragLayBinding
import com.enation.javashop.android.component.cart.launch.CartLaunch
import com.enation.javashop.android.component.cart.util.CartActionAgreement
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.adapter.TextViewDelegateAdapter
import com.enation.javashop.android.lib.adapter.VlayoutHolderAdapter
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopCommonView
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.event.CartDataChange
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.event.LogoutEvent
import com.enation.javashop.android.middleware.logic.contract.cart.CartFragmentContract
import com.enation.javashop.android.middleware.logic.presenter.cart.CartFragmentPresenter
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.cart_frag_lay.*
import java.util.*

/**
 * @author LDD
 * @From   com.enation.javashop.android.component.cart.fragment
 * @Date   2018/2/24 下午4:31
 * @Note   购物车Fragment
 */
@Router(path = "/cart/fragment")
class CartFragment :BaseFragment<CartFragmentPresenter,CartFragLayBinding>(),CartFragmentContract.View ,CartActionAgreement {

    private var hasSelectGoods = false

    /**
     * @Name  adapterList
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapterList = ArrayList<DelegateAdapter.Adapter<*>>()

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  七巧板LayoutManager
     */
    private lateinit var virtualLayoutManager : VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter : DelegateAdapter

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:35
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.cart_frag_lay
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:35
     * @Note   依赖注入
     */
    override fun bindDagger() {
        CartLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:35
     * @Note   初始化
     */
    override fun init() {
        virtualLayoutManager =  VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mViewDataBinding.cartFragRcList.layoutManager = virtualLayoutManager
        mViewDataBinding.cartFragRcList.adapter = delegateAdapter
        mViewDataBinding.cartFragRcList.setBackgroundColor(Color.WHITE)
        presenter.loadCartData()
        mViewDataBinding.cartFragHeader.setLineBgColor(Color.TRANSPARENT)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:35
     * @Note   绑定事件及数据
     */
    override fun bindEvent() {
        if (getActivity() is CartActivity){
            cart_frag_header.setLeftImage(R.drawable.javashop_back_black).setLeftClickListener {
                getActivity().to<CartActivity>().pop()
            }
        }
        cart_frag_create_order_tv.setOnClickListener {
            if (!MemberState.manager.getLoginState()){
                showMessage("未登录")
                return@setOnClickListener
            }
            if (!hasSelectGoods){
                showMessage("请选择结算商品")
                return@setOnClickListener
            }
            push("/order/create")
        }
        cart_frag_all_ck.setOnClickListener {
            presenter.allCheck(cart_frag_all_ck.isChecked)
        }
        cart_refresh_layout.setHeaderHeight((ScreenTool.getScreenWidth(activity) * 0.25).pxToDp().toFloat())
        cart_refresh_layout.setEnableRefresh(true)
        cart_refresh_layout.setRefreshHeader(ClassicsHeader(activity))
        cart_refresh_layout.setOnRefreshListener {
            Handler().postDelayed({
                presenter.loadCartData()
                it.finishRefresh()
            },1000)
        }
        getEventCenter().register(LoginEvent::class.java) {
            presenter.loadCartData()
        }.joinManager(disposableManager)
        getEventCenter().register(LogoutEvent::class.java) {
            showCartView(listOf(),0.0,0.0,0.0)
        }.joinManager(disposableManager)
        getEventCenter().register(CartDataChange::class.java) {
            presenter.loadCartData()
        }.joinManager(disposableManager)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:37
     * @Note   显示购物车数据
     * @param  cart 购物车数据
     */
    override fun showCartView(cart: List<Any>,price :Double,cashPrice :Double ,orgPrice :Double) {
        var allCheck = true
        adapterList.clear()
        adapterList = ArrayList<DelegateAdapter.Adapter<*>>().then {
            self ->
            cart.forEach { item ->
                when(item){
                    is CartShopItemViewModel ->{
                        if (allCheck){
                            allCheck = item.isCheck
                        }
                        if (!hasSelectGoods){
                            hasSelectGoods = item.isCheck
                        }
                        self.add(CartShopItemAdapter(this,item))
                    }
                    is String ->{
                        if (item == "Holder"){
                            /**
                             * 占位视图
                             */
                            val holderView = VlayoutHolderAdapter(object : VlayoutHolderAdapter.HolderCallBack{
                                override fun bindView(holder: RecyclerView.ViewHolder) {
                                    holder.itemView.setBackgroundColor(Color.WHITE)
                                    holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.dip2px(holder.itemView.context,15f)))
                                }

                                override fun layoutHelperProvider(): LayoutHelper {
                                    return LinearLayoutHelper(0,1)
                                }

                                override fun viewHolderProvider(parent : ViewGroup): RecyclerView.ViewHolder {
                                    return VlayoutHolderAdapter.PlaceHolder(View(parent.context))
                                }

                            })
                            self.add(holderView)
                        }else{
                            self.add(CartPromotionItemAdapter(this,item))
                        }
                    }
                    is CartGoodsItemViewModel ->{
                        if (allCheck){
                            allCheck = item.isCheck
                        }
                        if (!hasSelectGoods){
                            hasSelectGoods = item.isCheck
                        }
                        self.add(CartGoodsItemAdapter(this, arrayListOf(item)))
                    }
                }
            }
        }
        if (adapterList.count() == 0){
            allCheck = false
            hasSelectGoods = false
        }
        delegateAdapter.clear()
        delegateAdapter.notifyDataSetChanged()
        delegateAdapter.addAdapters(adapterList)
        delegateAdapter.notifyDataSetChanged()
        cart_frag_all_ck.isChecked = allCheck
        cart_frag_price_tv.text = String.format("合计:￥%.2f",price)
        cart_frag_promotion_price_tv.text = String.format("总额:￥%.2f 立减:￥%.2f",orgPrice,cashPrice)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:36
     * @Note   列表店铺Item选中回调
     * @param  shopId 店铺ID
     * @param  checked 是否选中
     */
    override fun shopCheck(shopId: Int, checked: Boolean) {
        presenter.shopCheck(shopId,checked)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:38
     * @Note   商品选中回调
     * @param  productId 商品ProductId
     * @param  checked 是否选中
     */
    override fun goodsEdit(productId: Int, num: Int?, checked: Boolean?) {
        presenter.editItem(productId,checked,num)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:39
     * @Note   组合促销详细回调
     * @param  cartPromotionItemViewModel 组合促销详细数据
     */
    override fun groupPromotionDetail(cartPromotionItemViewModel: CartPromotionItemViewModel) {

    }

    override fun showCoupon(coupons: ArrayList<CouponViewModel>) {
        showPopView("优惠券", TextViewDelegateAdapter("可领取优惠券",23, Color.BLACK,10.dpToPx(),10.dpToPx(),10.dpToPx(),10.dpToPx(), Color.TRANSPARENT),
                ReflexHelper.build("com.enation.javashop.android.component.member.adapter.CouponAdapter")
                        .newInstance(coupons).getInstance<BaseDelegateAdapter<*, CouponViewModel>>().then {
                            it.setOnItemClickListener { data, position ->
                                presenter.getCoupon(data.id)
                            }
                        })
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:41
     * @Note   单品促销选择回调
     * @param  singglepromotionList 单品促销列表
     */
    override fun selectSingglePromotion(sellerId: Int, skuId: Int, singglepromotionList: List<SingglePromotionViewModel>?) {
        var data : List<SingglePromotionViewModel> = ArrayList()
        data += singglepromotionList!!
        data += SingglePromotionViewModel(0,false,"取消促销","NONE")
        val dialog = PopCommonView.build(activity)
        dialog.setAdapters(CartSinglePromotionItemAdapter(data).then {
                    it.setOnItemClickListener { data, position ->
                        presenter.editPromotion(sellerId,skuId,data.promotionId,data.type)
                        dialog.dismiss()
                    }
                })
                .setTitle("促销")
                .setConfirmVisable(false)
                .show(cart_frag_header.holderViewProvider())
    }

    private fun showPopView(title: String ,vararg adapters : DelegateAdapter.Adapter<*>){
        PopCommonView.build(activity)
                .setAdapters(adapters.asList())
                .setTitle(title)
                .setConfirmVisable(false)
                .show(cart_frag_header.holderViewProvider())
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:42
     * @Note   显示商品Item更多操作Mask层
     * @param  productId 商品productId
     * @param  goodsId 商品Id
     */
    override fun showGoodsMoreMask(productId: Int, goodsId: Int) {
       CommonTool.createVerifyDialog("请选择您要进行的操作","删除","收藏",activity,object :CommonTool.DialogInterface{
           override fun yes() {
               presenter.collectionGoods(goodsId)
           }

           override fun no() {
               presenter.deleteGoods(productId)
           }
       }).then {
           it.setCanceledOnTouchOutside(true)
       }.show()
    }

    override fun deleteGoods(productId: Int) {
        presenter.deleteGoods(productId)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:43
     * @Note   显示店铺可领取优惠券
     * @param  shopId 店铺Id
     */
    override fun showShopBonus(shopId: Int) {
       presenter.loadCoupon(shopId)
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:43
     * @Note   去店铺页面
     * @param  shopId 店铺Id
     */
    override fun toShop(shopId: Int) {
        push("/shop/detail",{
            it.withInt("shopId",shopId)
        })
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:43
     * @Note   去商品页面
     * @param  goodsId 商品ID
     */
    override fun toGoods(goodsId: Int) {
        push("/goods/detail",{
            it.withInt("goodsId",goodsId)
        })
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:44
     * @Note   逻辑操作失败回调
     * @param  message 失败原因
     */
    override fun onError(message: String, type: Int) {
        if (message != "未登录"){
            showMessage(message)
        }
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:44
     * @Note   逻辑操作完成回调
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        getUtils().dismissDialog()
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:44
     * @Note   逻辑操作开始回调
     */
    override fun start() {
        getUtils().showDialog()
    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:45
     * @Note   网络状态实时监听回调
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   CartFragment
     * @Date   2018/2/24 下午4:36
     * @Note   页面销毁时调用
     */
    override fun destory() {

    }
}