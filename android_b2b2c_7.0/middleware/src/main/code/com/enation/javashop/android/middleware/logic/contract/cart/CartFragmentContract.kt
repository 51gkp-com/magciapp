package com.enation.javashop.android.middleware.logic.contract.cart

import com.enation.javashop.android.lib.base.BaseContract
import com.enation.javashop.android.middleware.model.CouponViewModel
import com.enation.javashop.android.middleware.model.MemberViewModel

/**
 * @author LDD
 * @Date   2018/2/9 上午10:38
 * @From   com.enation.javashop.android.middleware.logic.contract.cart
 * @Note   购物车接口控制
 */
interface CartFragmentContract {

    /**
     * @author LDD
     * @Date   2018/2/9 上午10:38
     * @From   CartFragmentContract
     * @Note   View接口控制
     */
    interface View :BaseContract.BaseView{

        /**
         * @author LDD
         * @From   View
         * @Date   2018/2/9 上午10:38
         * @Note   展示购物车数据
         * @param  cart 购物车数据
         */
        fun showCartView(cart :List<Any>,price :Double,cashPrice :Double ,orgPrice :Double)


        fun showCoupon(coupons :ArrayList<CouponViewModel>)

    }

    /**
     * @author LDD
     * @Date   2018/2/9 上午10:39
     * @From   CartFragmentContract
     * @Note   数据逻辑接口
     */
    interface Presenter :BaseContract.BasePresenter{

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午10:40
         * @Note   加载购物车数据
         */
        fun loadCartData()

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午10:41
         * @Note   更改商品数量
         * @param  productId 商品ItemId
         * @param  num 需要更改的数量
         */
        fun editItem(productId : Int ,checked : Boolean?, num :Int?)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午10:43
         * @Note   店铺选中
         * @param  checked 选中状态
         * @param  shopId  店铺ID
         */
        fun shopCheck(shopId : Int ,checked: Boolean)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午10:44
         * @Note   删除商品
         * @param  productId 商品ID
         */
        fun deleteGoods(productId: Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午10:45
         * @Note   收藏商品
         * @param  goodsId 商品ID
         */
        fun collectionGoods(goodsId:Int)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午11:51
         * @Note   批量收藏商品
         * @param  goodsIds goodsId集合
         */
        fun collectionGoods(goodsIds: ArrayList<Int>)

        /**
         * @author LDD
         * @From   Presenter
         * @Date   2018/2/9 上午11:54
         * @Note   删除商品
         * @param  productIds 商品id集合
         */
        fun deleteGoods(productIds :ArrayList<Int>)


        fun allCheck(check :Boolean)

        fun loadCoupon(shopId :Int)

        fun getCoupon(couponId :Int)

        fun editPromotion(sellerId :Int , skuId :Int , actId : Int , actType :String)
    }

}