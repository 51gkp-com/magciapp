package com.enation.javashop.android.middleware.logic.presenter.goods

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.NetStateEvent
import com.enation.javashop.android.middleware.api.CartApi
import com.enation.javashop.android.middleware.api.GoodsApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.enum.Promotion
import com.enation.javashop.android.middleware.logic.contract.goods.GoodsSearchContract
import com.enation.javashop.android.middleware.model.*
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/3/9 下午12:10
 * @From   com.enation.javashop.android.middleware.logic.presenter.goods
 * @Note   商品搜索逻辑控制器
 */
class GoodsSearchPresenter @Inject constructor() : RxPresenter<GoodsSearchContract.View>(), GoodsSearchContract.Presenter {

    /**
     * @Name  cartApi
     * @Type  CartApi
     * @Note  购物车API
     */
    @Inject
    protected lateinit var cartApi: CartApi

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



    private val observer = object: ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(data: Any, connectionQuality: ConnectionQuality) {

            if (data is Int){
                when(data){
                    -1 -> {
                        providerView().showGoodsList(ArrayList())
                    }
                    -2 -> {
                        providerView().initFilterPage(ArrayList())
                    }
                }
                return
            }


            val result = data as ArrayList<*>

            if (result.count() == 0){
                return
            }

            when (result[0]) {
                is GoodsItemViewModel -> {
                    providerView().showGoodsList(data as ArrayList<GoodsItemViewModel>)
                }
                is GoodsFilterViewModel -> {
                    providerView().initFilterPage(data as ArrayList<GoodsFilterViewModel>)
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
     * @From   GoodsSearchPresenter
     * @Date   2018/3/9 下午12:12
     * @Note   初始化依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   GoodsSearchPresenter
     * @Date   2018/3/9 下午12:53
     * @Note   搜索商品
     * @param  params 搜索条件
     */
    override fun searchGoods(page :Int,params: HashMap<String, Any>) {

        goodsApi.searchGoodsList(page,10,params).map { responseBody ->

            var jsonPage = JSONObject(responseBody.getJsonString())

            var jsonArray = JSONArray(jsonPage.get("data").toString())

            var goodsItemList = ArrayList<GoodsItemViewModel>()

            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                goodsItemList.add(GoodsItemViewModel.goodssearchMap(jsonObject))
            }
            if (goodsItemList.size > 0){
                return@map goodsItemList
            }else{
                return@map -1
            }

        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   GoodsSearchPresenter
     * @Date   2018/3/9 下午12:53
     * @Note   加载筛选条件
     * @param  params 搜索参数
     */
    override fun loadFilter(params: HashMap<String, Any>) {


        goodsApi.searchGoodsSelector(1,10000,params).map { responseBody ->

            //所有的筛选调教
            var filters = ArrayList<GoodsFilterViewModel>()
            var jsonSelector = JSONObject(responseBody.getJsonString())


            //品牌条件
            var brandArray = jsonSelector.valueJsonArray("brand")
            var brandList = ArrayList<GoodsFilterValue>()
            for (i in 0..(brandArray.length() - 1)) {
                val jsonObject = brandArray.getJSONObject(i)
                brandList.add(GoodsFilterValue(
                        jsonObject.valueString("name"),
                        jsonObject.valueString("value"),
                        false
                ))
            }

            if (brandArray.length() > 0){
                filters.add(GoodsFilterViewModel("品牌",null,"brand",brandList))
            }

            //分类条件
            var catArray = jsonSelector.valueJsonArray("cat")
            var catList = ArrayList<GoodsFilterValue>()
            for (i in 0..(catArray.length() - 1)) {
                val jsonObject = catArray.getJSONObject(i)
                catList.add(GoodsFilterValue(
                        jsonObject.valueString("name"),
                        jsonObject.valueString("value"),
                        false
                ))
            }
            if (catArray.length() > 0 ){
                filters.add(GoodsFilterViewModel("分类",null,"cat",catList))
            }

            //参数条件
            var propArray = jsonSelector.valueJsonArray("prop")
            for (i in 0..(propArray.length() - 1)) {
                val jsonParam = propArray.getJSONObject(i)
                val jsonParamList = ArrayList<GoodsFilterValue>()

                val jsonParamValueArray = jsonParam.getJSONArray("value")
                for (i in 0..(jsonParamValueArray.length() - 1)) {
                    val jsonParamValue = jsonParamValueArray.getJSONObject(i)

                    jsonParamList.add(GoodsFilterValue(
                            jsonParamValue.valueString("name"),
                            jsonParamValue.valueString("value"),
                            false
                    ))

                }
                if (jsonParamList.count() > 0) {
                    filters.add(GoodsFilterViewModel(jsonParam.valueString("key"),null,"prop",jsonParamList))
                }
            }
            if (filters.size == 0){
                return@map -2
            }else{
                return@map filters
            }
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)

    }

    /**
     * @author LDD
     * @From   GoodsSearchPresenter
     * @Date   2018/3/9 下午12:54
     * @Note   添加进购物车
     * @param  skuId 商品的规格商品ID
     * @param  num 需要添加的数量
     */
    override fun addCart(skuId: Int,num : Int) {
//        cartApi.addCart(skuId,num).compose(ThreadFromUtils.defaultSchedulers())
//                .subscribe(GoodsSearchObserable {
//                    providerView().complete("添加购物车完成")
//                })

    }

    /**
     * @author LDD
     * @From   GoodsSearchPresenter
     * @Date   2018/3/9 下午12:54
     * @Note   收藏商品
     * @param  goodsId 商品Id
     */
    override fun collectionGoods(goodsId: Int) {
//        memberApi.collectGoods(goodsId)
//                .compose(ThreadFromUtils.defaultSchedulers())
//                .subscribe(GoodsSearchObserable {
//                    providerView().complete("收藏商品成功")
//                })
    }
}