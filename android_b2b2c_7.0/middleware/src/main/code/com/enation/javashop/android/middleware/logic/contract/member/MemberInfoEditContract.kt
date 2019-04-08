package com.enation.javashop.android.middleware.logic.contract.member

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel

/**
 * @author LDD
 * @Date   2018/5/15 下午2:59
 * @From   com.enation.javashop.android.lib.base.BaseContract
 * @Note   会员修改页面逻辑控制
 */
interface MemberInfoEditContract {

    /**
     * @author LDD
     * @Date   2018/5/15 下午3:00
     * @From   MemberInfoEditContract
     * @Note   视图接口
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/5/15 下午3:01
         * @Note   渲染会员信息UI
         * @param  member 会员信息
         */
        fun renderMemberUi(member:MemberViewModel)

        /**
         * @author LDD
         * @From   View
         * @Date   2018/8/15 上午9:45
         * @Note   渲染地址
         * @param  datas 数据
         */
        fun renderRegion(datas :ArrayList<RegionViewModel>)


        fun refreshUserface(face :String)
    }

    /**
     * @author LDD
     * @Date   2018/5/15 下午3:00
     * @From   MemberInfoEditContract
     * @Note   逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/15 下午3:01
         * @Note   加载会员信息
         */
        fun loadMember()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/5/15 下午3:02
         * @Note   修改会员信息
         * @param  member 会员信息
         */
        fun editMember(member:MemberViewModel)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/8/15 上午9:44
         * @Note   加载地址
         * @param  id 索引
         */
        fun loadRegion(id :Int)


        fun uploader(filePath :String)

    }

}