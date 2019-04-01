package com.enation.javashop.android.component.order.adapter

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreateShopLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.OrderShopModel
import com.enation.javashop.utils.base.tool.ScreenTool
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @author LDD
 * @Date   2018/5/23 上午10:20
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   创建订单商品item
 */
class OrderCreateShopAdapter(var data : ArrayList<OrderShopModel>) : BaseDelegateAdapter<BaseRecyclerViewHolder<OrderCreateShopLayBinding>,OrderShopModel>() {

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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderCreateShopLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_create_shop_lay)
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:50
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.count()
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:53
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper(top = 10.dpToPx(),left = 5.dpToPx(),right = 5.dpToPx()).then {
            it.itemCount = data.count()
            it.setDividerHeight(10.dpToPx())
        }
    }

    /**
     * @author LDD
     * @From   OrderCreateGoodsAdapter
     * @Date   2018/5/23 上午10:53
     * @Note   数据绑定
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderCreateShopLayBinding>?, position: Int) {
        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((ScreenTool.getScreenWidth(holder?.databinding?.root?.context!!)- 20.dpToPx()) * (15.toFloat()/36.toFloat())).toInt())
        holder?.bind {
            binding ->
            var count = 0
            var shop = data[position]
            shop.cartGoods.forEach {
                count += it.currentNum.toInt()
            }
            binding.image1.invisable()
            binding.image2.invisable()
            binding.image3.invisable()
            shop.cartGoods.getOrNull(0)?.then {
                loadImageRound(it.imageUrl,binding.image1,RoundedCornersTransformation.CornerType.ALL)
                binding.image1.visable()
            }
            shop.cartGoods.getOrNull(1)?.then {
                loadImageRound(it.imageUrl,binding.image2,RoundedCornersTransformation.CornerType.ALL)
                binding.image2.visable()
            }
            shop.cartGoods.getOrNull(2)?.then {
                loadImageRound(it.imageUrl,binding.image3,RoundedCornersTransformation.CornerType.ALL)
                binding.image3.visable()
            }
            binding.name.text = shop.shopName
            binding.count.text = "共${count}件"

            binding.root.setOnClickListener {
                (binding.root.context as AppCompatActivity).push("/order/create/goods",{
                    it.withObject("shop",shop)
                })
            }
        }
    }
}