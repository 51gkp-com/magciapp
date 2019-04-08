package com.enation.javashop.android.component.cart.adapter

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Outline
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.cart.R
import com.enation.javashop.android.component.cart.databinding.CartGoodsItemBinding
import com.enation.javashop.android.component.cart.util.BaseCartItemAdapter
import com.enation.javashop.android.component.cart.util.CartActionAgreement
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.CartGoodsItemViewModel

import com.enation.javashop.android.middleware.model.SingglePromotionViewModel
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/2/6 下午4:33
 * @From   com.enation.javashop.android.component.cart.adapter
 * @Note   购物车商品Item适配器
 */
class CartGoodsItemAdapter(agreement: CartActionAgreement, private val datas: List<CartGoodsItemViewModel>) : BaseCartItemAdapter<BaseRecyclerViewHolder<CartGoodsItemBinding>, CartGoodsItemViewModel>(agreement) {

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:42
     * @Note   数据提供者
     * @return 数据
     */
    override fun dataProvider(): Any {
        return datas
    }

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:42
     * @Note   响应事件过滤
     * @param  position item坐标
     * @return 是否过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:43
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<CartGoodsItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.cart_goods_item, parent, false))
    }

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:43
     * @Note   item总数
     * @return 数量总数
     */
    override fun getItemCount(): Int {
        return datas.size
    }

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:44
     * @Note   ViewHelper提供者
     * @return ViewHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0, datas.size)
    }

    /**
     * @author LDD
     * @From   CartGoodsItemAdapter
     * @Date   2018/2/9 下午2:45
     * @Note   绑定ViewHolder
     * @param  holder ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<CartGoodsItemBinding>?, position: Int) {
        holder?.bind { binding ->
            binding.cartGoodsItemErrorEdit.setOnClickListener {
                CommonTool.createVerifyDialog(datas[position].errorMessage,"我已知晓","移除商品",binding.root.context,object : CommonTool.DialogInterface{
                    override fun yes() {
                        actionProvider().deleteGoods(datas[position].skuId)
                    }

                    override fun no() {

                    }
                }).show()
            }
            binding.promotionTagParent.removeAllViews()
            binding.goodsItemData = datas[position]
            binding.cartGoodsPriceTv.text = String.format("￥%.2f", datas[position].price)
            binding.cartGoodsItemSingglePromotionLay.visibility = (datas[position].promotionList == null || datas[position].promotionList!!.count() == 0).judge(View.GONE, View.VISIBLE)
            goodsEdit(binding.cartGoodsAddBtn, position, getItem(position).skuId, getItem(position).currentNum.toInt() + 1, null)
            goodsEdit(binding.cartGoodsReduceBtn, position, getItem(position).skuId, getItem(position).currentNum.toInt() - 1, null)
            goodsEdit(binding.cartGoodsItemCheck, position, getItem(position).skuId, null, !getItem(position).isCheck)
            goodsMoreAction(binding.cartGoodsItemContent, position, getItem(position).skuId, getItem(position).goodsId)
            toGoods(binding.cartGoodsItemContent, getItem(position).goodsId)
            selectSingglePromotion(binding.cartGoodsItemSingglePromotionEdit, datas[position].skuId, datas[position].shopId, getItem(position).promotionList
                    ?: emptyList())
            datas[position].promotionList?.forEach {
                when (it.type) {
                    "MINUS" -> {
                        buildItem(binding.promotionTagParent, "立减")
                    }
                    "GROUPBUY" -> {
                        buildItem(binding.promotionTagParent, "团购")
                    }
                    "EXCHANGE" -> {
                        buildItem(binding.promotionTagParent, "积分商城")
                    }
                    "HALF_PRICE" -> {
                        buildItem(binding.promotionTagParent, "第二件半价")
                    }
                    "SECKILL" -> {
                        buildItem(binding.promotionTagParent, "秒杀")
                    }
                }
            }
        }
    }

    private val goodsEdit = { view: View, position: Int, productId: Int, num: Int?, check: Boolean? ->
        view.setOnClickListener {
            actionProvider().goodsEdit(productId, num, check)
        }
    }

    /**
     * @Name  goodsMoreAction
     * @Type  Block
     * @Note  商品更多操作
     */
    private val goodsMoreAction = { view: View, position: Int, productId: Int, goodsId: Int ->
        view.setOnLongClickListener {
            actionProvider().showGoodsMoreMask(productId, goodsId)
            return@setOnLongClickListener true
        }
    }

    /**
     * @Name  toGoods
     * @Type  Block
     * @Note  去商品页面
     */
    private val toGoods = { view: View, goodsId: Int ->
        view.setOnClickListener {
            actionProvider().toGoods(goodsId)
        }
    }

    /**
     * @Name  selectSingglePromotion
     * @Type  Block
     * @Note  选择单品促销
     */
    private val selectSingglePromotion = { view: View, skuId: Int, sellerId: Int, singglePromotionList: List<SingglePromotionViewModel> ->
        view.setOnClickListener {
            actionProvider().selectSingglePromotion(sellerId, skuId, singglePromotionList)
        }
    }

    private fun buildItem(parentView: ViewGroup, single: String) {
        var tv = TextView(parentView.context)
        tv.gravity = Gravity.CENTER
        tv.setPadding(5.dpToPx(), 0, 5.dpToPx(), 0)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTool.getScreenWidth(parentView.context) / 40)
        tv.setBackgroundResource(R.drawable.javashop_red_radis_bg)
        tv.setTextColor(Color.WHITE)
        tv.text = single
        parentView.addView(tv)
        tv.reLayout<LinearLayout.LayoutParams> {
            it.setMargins(0, 2.dpToPx(), 5.dpToPx(), 2.dpToPx())
            it.width = LinearLayout.LayoutParams.WRAP_CONTENT
            it.height = LinearLayout.LayoutParams.MATCH_PARENT
        }

    }
}