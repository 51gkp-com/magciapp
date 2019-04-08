package com.enation.javashop.android.component.order.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.OrderListAgreement
import com.enation.javashop.android.component.order.databinding.OrderListItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.AutoSizeTextView
import com.enation.javashop.android.middleware.model.OrderItemGoodsViewModel
import com.enation.javashop.android.middleware.model.OrderItemViewModel
import com.enation.javashop.android.middleware.model.OrderPayModel
import com.enation.javashop.android.middleware.model.TradeType
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/17 上午11:02
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   订单Item适配器
 */
class OrderItemAdapter(var data :ArrayList<OrderItemViewModel>,val agreement: OrderListAgreement?) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderListItemLayBinding>,OrderItemViewModel>() {

    constructor(data :ArrayList<OrderItemViewModel>) : this(data,null)

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:05
     * @Note   item事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderListItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_list_item_lay)
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   提供Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:06
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.count()).then {
            self ->
            self.setDividerHeight((ScreenTool.getScreenWidth(BaseApplication.appContext)/25).toInt())
            self.setMargin(0, (ScreenTool.getScreenWidth(BaseApplication.appContext)/25).toInt(),0, (ScreenTool.getScreenWidth(BaseApplication.appContext)/25).toInt())
        }
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 上午11:07
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderListItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = getItem(position)
            createOrderGoods(binding.orderListItemShopGoodsContent,getItem(position).goodsList)
            binding.orderListItemActionComment.setOnClickListener {
                (binding.root.context as BaseToolActivity).push("/member/comment/post",{
                    it.withString("orderSn",binding.data.orderSn)
                })
            }
            binding.orderListItemActionComment.setOnClickListener {
                agreement?.comment(getItem(position).orderSn)
                if (agreement == null){
                    (binding.root.context as BaseToolActivity).push("/member/comment/post",{
                        it.withString("orderSn",binding.data.orderSn)
                    })
                }
            }
            binding.orderListItemActionCancle.setOnClickListener {
                if(getItem(position).orderAction.allowServiceCancel){
                    agreement?.applyServerCancel(getItem(position).orderSn)
                }else{
                    agreement?.cancel(getItem(position).orderSn)
                }
            }
            binding.orderListItemActionLogistics.setOnClickListener {
                agreement?.express(getItem(position).orderSn)
            }
            binding.orderListItemActionPay.setOnClickListener {
                val data = OrderPayModel()
                data.tradeType = TradeType.Order
                data.sn = getItem(position).orderSn
                data.payPrice = getItem(position).payedPrice
                agreement?.pay(data)
            }
            binding.orderListItemActionRog.setOnClickListener {
                agreement?.rog(getItem(position).orderSn)
            }

            if (!getItem(position).isComment){
                binding.orderListItemPriceLay.visibility = View.VISIBLE
                binding.orderListItemActionComment.visibility = (getItem(position).orderAction.allowComment).judge(View.VISIBLE,View.GONE)
                binding.orderListItemActionAfter.visibility = (getItem(position).orderAction.allowApplyService).judge(View.VISIBLE,View.GONE)
                binding.orderListItemActionCancle.visibility = (getItem(position).orderAction.getCancelState()).judge(View.VISIBLE,View.GONE)
                binding.orderListItemActionLogistics.visibility = (getItem(position).orderAction.allowExpress).judge(View.VISIBLE,View.GONE)
                binding.orderListItemActionPay.visibility = (getItem(position).orderAction.allowPay).judge(View.VISIBLE,View.GONE)
                binding.orderListItemActionRog.visibility = (getItem(position).orderAction.allowRog).judge(View.VISIBLE,View.GONE)
                binding.root.setOnClickListener {
                    (binding.root.context as BaseToolActivity).push("/order/detail",{
                        it.withString("orderSn",binding.data.orderSn)
                    })
                }
            }else{
                binding.orderListItemPriceLay.visibility = View.GONE
                binding.orderListItemPriceLay.visibility = View.VISIBLE
                binding.orderListItemActionComment.visibility = View.VISIBLE
                binding.orderListItemActionAfter.visibility = View.GONE
                binding.orderListItemActionCancle.visibility = View.GONE
                binding.orderListItemActionLogistics.visibility = View.GONE
                binding.orderListItemActionPay.visibility = View.GONE
                binding.orderListItemActionRog.visibility = View.GONE
                binding.root.setOnClickListener {
                    (binding.root.context as BaseToolActivity).push("/member/comment/post",{
                        it.withString("orderSn",binding.data.orderSn)
                    })
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   OrderItemAdapter
     * @Date   2018/4/17 下午1:33
     * @Note   创建订单商品
     * @param  parent 父视图
     * @param  data   商品数据
     */
    private fun createOrderGoods(parent :LinearLayout ,data :ArrayList<OrderItemGoodsViewModel>){

        val tendp = AppTool.SystemUI.dpToPx(10f)

        /**清除已存在的子视图*/
        parent.removeAllViews()


        /**判断创建子视图*/

        if (data.size <= 0){
            return
        }

        if (data.size > 1){
            data.forEach {
                item ->

                val iv = ImageView(parent.context).then {
                    self ->
                    self.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                    self.setPadding(tendp/5,tendp/5,tendp/5,tendp/5)
                }

                parent.addView(iv)

                iv.reLayout<LinearLayout.LayoutParams> {
                    params ->
                    params.setMargins(tendp,tendp,0,tendp)
                    params.width = (ScreenTool.getScreenWidth(parent.context)*0.25-tendp*2).toInt()
                    params.height = params.width
                }

                GlideUtils.setImage(parent.context,item.goodsImage,iv)
            }
        }else{
            val goods = data[0]

            val iv = ImageView(parent.context).then {
                self ->
                self.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                self.setPadding(tendp/5,tendp/5,tendp/5,tendp/5)
            }

            parent.addView(iv)

            iv.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.setMargins(tendp,tendp,0,tendp)
                params.width = (ScreenTool.getScreenWidth(parent.context)*0.25-tendp*2).toInt()
                params.height = params.width
            }

            val tv = AutoSizeTextView(parent.context).then {
                self ->
                self.setLines(3)
                self.gravity = Gravity.CENTER_VERTICAL
                self.setTextColor(Color.BLACK)
            }

            parent.addView(tv)

            tv.reLayout<LinearLayout.LayoutParams> {
                params ->
                params.setMargins(tendp,tendp,0,tendp)
                params.height = (ScreenTool.getScreenWidth(parent.context)*0.25-tendp*2).toInt()
                params.width = (ScreenTool.getScreenWidth(parent.context)*0.6).toInt()
            }

            GlideUtils.setImage(parent.context,goods.goodsImage,iv)
            tv.text = goods.goodsName
        }

    }
}