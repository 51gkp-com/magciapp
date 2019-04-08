package com.enation.javashop.android.component.shop.adapter

import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.agreement.ShopActionAgreement
import com.enation.javashop.android.component.shop.databinding.ShopHomeItemLayBinding
import com.enation.javashop.android.component.shop.databinding.ShopItemLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.ShopItem

/**
 * @author LDD
 * @Date   2018/4/10 上午11:59
 * @From   com.enation.javashop.android.component.shop.adapter
 * @Note   商品主页Item适配器
 */
class ShopitemAdapter(val data :ArrayList<ShopItem>) :BaseDelegateAdapter<BaseRecyclerViewHolder<ShopItemLayBinding> ,ShopItem>() {

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:03
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:03
     * @Note   item事件过滤
     * @param  position item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ShopItemLayBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.shop_item_lay)
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,data.size)
    }

    /**
     * @author LDD
     * @From   ShopHomeItemAdapter
     * @Date   2018/4/24 下午3:04
     * @Note   创建ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<ShopItemLayBinding>?, position: Int) {
        holder?.bind {
            binding ->
            val itemData = getItem(position)
            val goodsA = itemData.childGoods.getOrNull(0)
            val goodsB = itemData.childGoods.getOrNull(1)
            val goodsC = itemData.childGoods.getOrNull(2)
            loadImage(itemData.logo,binding.shopItemIv)
            binding.shopItemNameTv.text = itemData.shopName
            binding.shopItemCollectTv.text = "${itemData.collectNum}人关注"
            if (goodsA == null){
                binding.shopGoodsA.visibility = View.GONE
            }else{
                binding.shopGoodsA.visibility = View.VISIBLE
                loadImage(goodsA.goodsImage,binding.goodsAImage)
                binding.goodsAPrice.text = String.format("￥%.2f",goodsA.goodsPrice)
            }
            if (goodsB == null){
                binding.shopGoodsB.visibility = View.GONE
            }else{
                binding.shopGoodsB.visibility = View.VISIBLE
                loadImage(goodsB.goodsImage,binding.goodsBImage)
                binding.goodsBPrice.text = String.format("￥%.2f",goodsB.goodsPrice)
            }
            if (goodsC == null){
                binding.shopGoodsC.visibility = View.GONE
            }else{
                binding.shopGoodsC.visibility = View.VISIBLE
                loadImage(goodsC.goodsImage,binding.goodsCImage)
                binding.goodsCPrice.text = String.format("￥%.2f",goodsC.goodsPrice)
            }
        }
    }
}