package com.enation.javashop.android.middleware.logic.presenter.home

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.CategoryApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.home.CategoryFragmentContract
import com.enation.javashop.android.middleware.model.ChildCategoryShell
import com.enation.javashop.android.middleware.model.ChildCategoryViewModel
import com.enation.javashop.android.middleware.model.ParentCategoryViewModel
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import org.json.JSONArray
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/1/22 下午12:17
 * @From   com.enation.javashop.android.middleware.logic.presenter.home
 * @Note   分类Fragment逻辑控制器
 */
class CategoryFragmentPresenter @Inject constructor() :RxPresenter<CategoryFragmentContract.View>(),CategoryFragmentContract.Presenter {

    /**
     * @Name  categoryApi
     * @Type  CategoryApi
     * @Note  分类API
     */
    @Inject
    protected lateinit var categoryApi :CategoryApi

    /**
     * 数据监听者
     */
    private val observer = object : ConnectionObserver<ArrayList<*>>(){

        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: ArrayList<*>, connectionQuality: ConnectionQuality) {
            providerView().complete()
            result.getOrNull(0)?.more { data ->
                when(data){
                    is ParentCategoryViewModel ->{
                        providerView().showParentCatList(result as ArrayList<ParentCategoryViewModel>)
                    }
                    is ChildCategoryShell ->{
                        providerView().showChildCatList(result as ArrayList<ChildCategoryShell>)
                    }
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
     * @From   CategroyFragmentPresenter
     * @Date   2018/1/22 下午12:33
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    /**
     * @author LDD
     * @From   CategroyFragmentPresenter
     * @Date   2018/1/22 下午12:33
     * @Note   加载分类数据
     */
    override fun loadParentCat() {
        load(0)
    }

    /**
     * @author LDD
     * @From   CategroyFragmentPresenter
     * @Date   2018/1/22 下午12:33
     * @Note   加载子分类
     * @param  parentId 父分类ID
     */
    override fun loadChildCat(parentId: Int) {
        load(parentId)
    }


    private fun load(id :Int){
        /** 获取父分类数据 */
        categoryApi.getCat(id).map { respone ->
            /** 过滤数据 转换为需要的格式 */

            /**获取json字符串*/
            val jsonResult = respone.getJsonString()
            /**初始化Result数据*/
            val result = ArrayList<Any>()

            if (id == 0){
                if (jsonResult != ""){

                    val jsonArray = JSONArray(jsonResult)

                    for (i in 0..(jsonArray.length() - 1)){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val item = ParentCategoryViewModel.map(jsonObject)
                        item.selected.set(i==0)
                        result.add(item)
                    }

                }
            }else{
                if (jsonResult != ""){

                    val jsonArray = JSONArray(jsonResult)

                    for (i in 0..(jsonArray.length() - 1)){

                        val item = ChildCategoryShell.map(jsonArray.getJSONObject(i))

                        result.add(item)

                    }

                }
            }

            return@map result
            /**设置操作线程 默认 网络请求在io线程执行 UI更新在主线程执行*/
        }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}