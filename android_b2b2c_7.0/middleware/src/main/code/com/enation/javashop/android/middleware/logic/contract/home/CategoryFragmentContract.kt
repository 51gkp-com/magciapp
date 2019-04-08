package com.enation.javashop.android.middleware.logic.contract.home

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.ChildCategoryShell
import com.enation.javashop.android.middleware.model.ParentCategoryViewModel

/**
 * @author LDD
 * @Date   2018/1/22 下午12:14
 * @From   com.enation.javashop.android.middleware.logic.contract.home
 * @Note   分类Fragment接口控制器
 */
interface CategoryFragmentContract {

    /**
     * @author LDD
     * @Date   2018/1/22 下午12:14
     * @From   CategoryFragmentContract
     * @Note   分类Fragment基础接口
     */
    interface View:BaseContract.BaseView{

        /**
         * @author LDD
         * @From   CategoryFragmentContract.View
         * @Date   2018/1/22 下午12:14
         * @Note   展示子分类
         * @param  childCategoryList 子分类数据
         */
        fun showChildCatList(childCategoryList: ArrayList<ChildCategoryShell>)

        /**
         * @author LDD
         * @From   CategoryFragmentContract.View
         * @Date   2018/1/22 下午12:15
         * @Note   展示父分类
         * @param  parentCategoryList 父分类数据
         */
        fun showParentCatList(parentCategoryList: ArrayList<ParentCategoryViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/1/22 下午12:15
     * @From   CategoryFragmentContract
     * @Note   分类Fragement逻辑控制器接口
     */
    interface Presenter : BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   CategoryFragmentContract.Presenter
         * @Date   2018/1/22 下午12:16
         * @Note   加载父分类数据
         */
        fun loadParentCat()

        /**
         * @author LDD
         * @From   CategoryFragmentContract.Presenter
         * @Date   2018/1/22 下午12:17
         * @Note   加载子分类数据
         * @param  parentId 父分类ID
         */
        fun loadChildCat(parentId :Int)
    }

}