package com.enation.javashop.android.component.order.adapter

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.bumptech.glide.Glide
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateGoodsItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.CartGoodsItemViewModel
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.utils.base.tool.ScreenTool
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   创建订单商品item
 */
class OrderCreateGoodsAdapter(var data : ArrayList<CartGoodsItemViewModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateGoodsItemBinding>,GoodsItemViewModel>() {

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:48
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:49
     * @Note   点击过滤
     * @param  position  坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:49
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateGoodsItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_goods_item)
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:50
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:53
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx())
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:53
     * @Note   数据绑定
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateGoodsItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.orderCreateGoodsContent.removeAllViews()
            var size = 0
            data.forEach {
                item ->
                size += item.currentNum.toInt()
                val imageView = ImageView(binding.root.context)
                binding.orderCreateGoodsContent.addView(imageView)
                imageView.reLayout<LinearLayout.LayoutParams> {
                    params ->
                    val value = ScreenTool.getScreenWidth(binding.root.context)*0.7/3*0.9
                    params.width = value.toInt()
                    params.height = value.toInt()
                    params.leftMargin = 10.dpToPx()
                }
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(imageView.context)
                        .load(item.imageUrl)
                        .bitmapTransform(RoundedCornersTransformation(imageView.context,5.dpToPx(),0,RoundedCornersTransformation.CornerType.ALL))
                        .error(R.drawable.image_error)
                        .into(imageView)
            }
            binding.orderCreateGoodsCountTv.text = "共${size}件"
            binding.root.setOnClickListener {
                (binding.root.context as AppCompatActivity).push("/order/create/goods")
            }
        }
    }
}