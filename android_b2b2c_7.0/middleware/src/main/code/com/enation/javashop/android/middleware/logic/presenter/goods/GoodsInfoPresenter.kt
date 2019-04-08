package com.enation.javashop.android.middleware.logic.presenter.goods

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.*
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsInfoContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function7
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/27 下午3:26
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Note   逻辑仓库
 */
class GoodsInfoPresenter @Inject constructor() : RxPresenter<GoodsInfoContract.View>() , GoodsInfoContract.Presenter {

    /**
     * @Name  goodsApi
     * @Type  GoodsApi
     * @Note  商品Api
     */
    @Inject
    protected lateinit var goodsApi: GoodsApi

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  会员Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi

    /**
     * @Name  shopApi
     * @Type  ShopApi
     * @Note  店铺Api
     */
    @Inject
    protected lateinit var shopApi: ShopApi

    /**
     * @Name  promotionApi
     * @Type  PromotionApi
     * @Note  促销Api
     */
    @Inject
    protected lateinit var promotionApi: PromotionApi

    @Inject
    protected lateinit var cartApi: CartApi


    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {
            when (data) {
                is ArrayList<*> -> {
                    val list = data as ArrayList<Any>
                    providerView().initList(list[0] as GoodsViewModel,
                            list[1] as ArrayList<GoodsGallery>,
                            list[2] as PromotionDetailViewModel,
                            list[3] as ArrayList<SkuGoods>,
                            list[4] as ArrayList<CouponViewModel>,
                            list[5] as HashMap<String,Any>,
                            list[6] as ShopViewModel)
                    providerView().complete("")
                }
                is ResponseBody ->{
                    providerView().complete("领取成功")
                }
                is String ->{
                    providerView().complete(data)
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoPresenter
     * @Date   2018/3/27 下午3:28
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsInfoPresenter
     * @Date   2018/10/6 下午9:38
     * @Note   加载数据
     * @param  goodsId 商品ID
     * @param  shopId 店铺ID
     */
    override fun loadData(goodsId: Int, shopId: Int) {

        val goodsDetail = goodsApi.getGoodsDetail(goodsId)
        val promotion = promotionApi.getPromotion(goodsId)
        val sku = goodsApi.getGoodsSku(goodsId)
        val collect = goodsApi.getCollectionState(goodsId)
        val coupon = promotionApi.getSellerCoupon(shopId)
        val comment = goodsApi.listComment(goodsId,null,null,1,10)
        val shop = shopApi.getShop(shopId)
        Observable.zip(goodsDetail,promotion,sku,collect,coupon,comment,shop, Function7<ResponseBody,ResponseBody,ResponseBody,Result<ResponseBody>,ResponseBody,ResponseBody,ResponseBody,ArrayList<Any>>{
            goodsBody,promotionBody,skuBody,collectBody,couponBody,commentBody,shopBody ->

            var goodsData = GoodsViewModel.map(goodsBody.toJsonObject())

            var gallery = ArrayList<GoodsGallery>()
            var galleryJson = goodsBody.toJsonObject().valueJsonArray("gallery_list")
            for (i in 0..(galleryJson.length() - 1)){
               gallery.add(GoodsGallery(galleryJson.getJSONObject(i).valueString("original"),
                       galleryJson.getJSONObject(i).valueString("original"),
                       galleryJson.getJSONObject(i).valueString("original")))
            }

            var promotionList = promotionBody.toJsonArray()
            var promotion = PromotionDetailViewModel()
            if (promotionList.length() > 0){
                for (i in 0..(promotionList.length() - 1)){
                    val child = PromotionDetailViewModel.map(promotionList.getJSONObject(i))
                    promotion.inject(child)
                }
            }

            var skuGoodsList = ArrayList<SkuGoods>()
            var skuJson = skuBody.toJsonArray()
            //遍历sku列表
            for (i in 0..(skuJson.length() - 1)){
                val jsonObject = skuJson.getJSONObject(i)
                skuGoodsList.add(SkuGoods.map(jsonObject))
            }

            if (!collectBody.isError && collectBody.response()!!.code() == 200){
                goodsData.collect = collectBody.response()!!.body()!!.toJsonObject().valueBool("message")
            }


            var couponJson = couponBody.toJsonArray()

            var couponList = ArrayList<CouponViewModel>()

            //遍历优惠券数据
            for (i in 0..(couponJson.length() - 1)){
                val jsonObject = couponJson.getJSONObject(i)

                couponList.add(CouponViewModel(
                        jsonObject.valueDouble("coupon_price"),
                        jsonObject.valueDouble("coupon_threshold_price"),
                        jsonObject.valueString("seller_name"),
                        false,
                        false,
                        false,
                        jsonObject.valueInt("coupon_id"),
                        jsonObject.valueDate("start_time")+"-"+jsonObject.valueDate("end_time"),
                        jsonObject.valueInt("seller_id"),
                        jsonObject.valueString("title"),
                        isGet = true
                ))
            }

            var commentParent = HashMap<String,Any>()
            var commentJson = commentBody.toJsonObject().valueJsonArray("data")
            var commentList = ArrayList<GoodsCommentViewModel>()
            if (commentJson.length() > 0 ){
                for (i in 0..(commentJson.length() - 1)) {
                    val commentObject = commentJson.getJSONObject(i)
                    val item = GoodsCommentViewModel.map(commentObject)
                    commentList.add(item)
                }
            }
            if (commentList.count() > 0){
                commentParent["num"] = commentBody.toJsonObject().valueInt("data_total").toString()
                commentParent["rate"] = "${goodsData.commentGrade}%"
                commentParent["data"] = commentList
            }



            var sellerJson = shopBody.toJsonObject()

            var seller =  ShopViewModel.map(sellerJson)




            return@Function7 arrayListOf(goodsData,gallery,promotion,skuGoodsList,couponList,commentParent,seller)
        }).compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)


    }


    override fun getCoupon(couponId: Int) {
        memberApi.receiveCoupon("$couponId")
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)

    }

    override fun addCart(id: Int, num: Int , actId :Int?) {
        cartApi.add(id,num,actId).map { "加入购物车成功" }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    override fun buyNum(id: Int, num: Int , actId :Int?) {
        cartApi.buy(id,num,actId).map { "立即购买" }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

}