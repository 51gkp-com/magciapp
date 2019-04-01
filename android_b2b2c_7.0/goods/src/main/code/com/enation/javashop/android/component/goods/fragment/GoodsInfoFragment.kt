package com.enation.javashop.android.component.goods.fragment

import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.adapter.*
import com.enation.javashop.android.component.goods.databinding.GoodsInfoFragLayBinding
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.component.goods.weiget.GoodsSpecView
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.base.LIFE_CYCLE_RESUME
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.CartDataChange
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsInfoContract
import com.enation.javashop.android.middleware.logic.presenter.goods.GoodsInfoPresenter
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.goods_act_lay.*
import kotlinx.android.synthetic.main.goods_info_frag_lay.*
import kotlinx.android.synthetic.main.goods_introduce_lay.*

/**
 * @author LDD
 * @Date   2018/3/27 下午3:58
 * @From   com.enation.javashop.android.component.goods.fragment
 * @Note   商品详情
 */
class GoodsInfoFragment :BaseFragment<GoodsInfoPresenter,GoodsInfoFragLayBinding>(),GoodsInfoContract.View,SpecAgreement{

    var shopId = 0

    var goodsId = 0

    var promotionId : Int? = null

    var skuList = ArrayList<SkuGoods>()

    var specList = ArrayList<Any>()

    var currentNum : Int = 1

    private val specDialog by lazy {  GoodsSpecView.build(activity,skuList,specList).setSkuSelectObserver { sku , num ->
        currentNum = num
        specAdapter.text = sku!!.getSpecString() + "(${num}件)"
        specAdapter.notifyDataSetChanged()
    }.setAddCart { skuGoods, i ->
        presenter.addCart(skuGoods.skuId,i,promotionId)
    }}

    lateinit var goodsInfoAdapter :GoodsInfoMessageAdapter

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private lateinit var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    private val specAdapter = GoodsInfoSpecAdapter(this,"默认(1件)")

    /**
     * @Name  adapters
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapters = ArrayList<DelegateAdapter.Adapter<*>>()
    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:00
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.goods_info_frag_lay
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:01
     * @Note   依赖注入
     */
    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:01
     * @Note   初始化
     */
    override fun init() {
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapters(adapters)
        /**配置到RecycleView*/
        goods_info_rv.layoutManager = virtualLayoutManager
        goods_info_rv.adapter = delegateAdapter
        presenter.loadData(goodsId,shopId)
    }
    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:01
     * @Note   绑定事件
     */
    override fun bindEvent() {

    }

    override fun initList(goodsDetail: GoodsViewModel, gallery: ArrayList<GoodsGallery>, promotion: PromotionDetailViewModel, skuGoodsList: ArrayList<SkuGoods>, couponList: ArrayList<CouponViewModel>, commentList: HashMap<String,Any>, seller: ShopViewModel) {
        if (promotion.groupBuy != null && promotion.groupBuy!!.id > 0){
            promotionId = promotion.groupBuy!!.id
        }else if ((promotion.secKill != null && promotion.secKill!!.id > 0)){
            promotionId = promotion.secKill!!.id
        }else if ((promotion.exchange != null && promotion.exchange!!.id > 0)){
            promotionId = promotion.exchange!!.id
        }
        adapters.clear()
        activity.to<GoodsActivity>().collectState(goodsDetail.collect)
        skuList = skuGoodsList
        specList = SkuGoods.mapSpec(skuList)
        specList.add(1)
        goodsInfoAdapter = GoodsInfoMessageAdapter(this,goodsDetail,couponList,promotion)
        adapters.add(GoodsInfoGalleryAdapter(this.weak(),gallery))
        if(promotion.groupBuy!= null || (promotion.secKill != null && promotion.secKill!!.disEndTime > 0) || promotion.exchange != null){
            adapters.add(GoodsPromotionDetailAdapter(this.weak(),promotion,goodsDetail) {
                presenter.loadData(goodsId,shopId)
            })
        }
        adapters.add(goodsInfoAdapter)
        adapters.add(specAdapter)
        adapters.add(GoodsInfoWeigetAdapter(goodsDetail.weight))
        if (commentList.count() > 0){
            adapters.add(GoodsInfoCommentAdapter(activity.to<GoodsActivity>().weak(),commentList))
        }
        adapters.add(GoodsInfoShopAdapter(seller))
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun specShow() {
       if (skuList.count() > 0){
           specDialog.show(activity.goods_act_topbar_holder)
       }
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:08
     * @Note   请求完成
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        getUtils().dismissDialog()
        if (message == "加入购物车成功"){
            showMessage(message)
            getEventCenter().post(CartDataChange())
        }else if (message == "立即购买"){
            getEventCenter().post(CartDataChange())
            push("/order/create")
        }else{
            showMessage(message)
        }
    }

    /**
     * 立即购买
     */
    fun buyNow(){
        presenter.buyNum(specDialog.getSelectSku().skuId,currentNum,promotionId)
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:08
     * @Note   请求错误
     * @param  错误信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        getUtils().dismissDialog()
        if (message.contains("登录")){
            push("/member/login/main")
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:09
     * @Note   请求开始
     */
    override fun start() {
        getUtils().showDialog()
    }

    fun getCoupon(id :Int){
        presenter.getCoupon(id)
    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:09
     * @Note   网络实时监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   GoodsInfoFragment
     * @Date   2018/3/27 下午4:10
     * @Note   销毁
     */
    override fun destory() {

    }

}