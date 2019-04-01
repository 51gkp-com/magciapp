package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.PointViewModel

/**
 * @author LDD
 * @Date   2018/5/4 下午4:54
 * @From   com.enation.javashop.android.middleware.logic.contract.member
 * @Note   积分页面逻辑接口
 */
interface MemberPointContract {

    /**
     * @author LDD
     * @Date   2018/5/4 下午4:55
     * @From   MemberPointContract
     * @Note   视图借口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/4 下午4:55
         * @Note   渲染积分列表
         * @param  data 积分数据
         */
        fun renderPointUi(data :ArrayList<PointViewModel>)

    }
    
    /**
     * @author LDD
     * @Date   2018/5/4 下午4:55
     * @From   MemberPointContract
     * @Note   逻辑接口
     */
    interface Presenter:BaseContract.BasePresenter {

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/4 下午4:57
         * @Note   加载积分数据
         * @param  page  分页
         * @param  state 类型
         */
        fun loadPointData(page: Int, state: Int)

    }
}