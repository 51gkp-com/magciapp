package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.activity.GoodsActivity
import com.enation.javashop.android.component.goods.databinding.GoodsInfoSpecItemBinding
import com.enation.javashop.android.component.goods.weiget.GoodsSpecView
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.goods_act_lay.*

/**
 * @author LDD
 * @Date   2018/4/8 下午1:47
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品信息规格适配器
 */
class GoodsInfoSpecAdapter(val agreement: SpecAgreement,var text :String) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoSpecItemBinding>,Int>() {

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:48
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:48
     * @Note   Itme过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:48
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoSpecItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_spec_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:48
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:49
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then {
            self ->
            self.setMargin(0,0,0, ScreenTool.dip2px(BaseApplication.appContext,10f))
        }
    }

    /**
     * @author LDD
     * @From   GoodsInfoSpecAdapter
     * @Date   2018/4/8 下午1:49
     * @Note   绑定viewholder数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoSpecItemBinding>?, position: Int) {
            holder?.bind {
                binding ->
                binding.goodsInfoSpecIv.setOnClickListener {
                    agreement.specShow()
                }
                binding.goodsInfoSpecContentTv.text = text
            }
    }
}

interface SpecAgreement{

    fun specShow()

}