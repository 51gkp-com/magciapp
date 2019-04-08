package com.enation.javashop.android.component.order.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.agreement.AfterSaleAgreement
import com.enation.javashop.android.component.order.databinding.OrderAfterSalePayItemBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.AccountInfo

/**
 * @author LDD
 * @Date   2018/4/24 下午4:12
 * @From   com.enation.javashop.android.component.order.adapter
 * @Note   售后退款Item适配器
 */
class OrderAfterSalePayAdapter(var payData :AccountInfo, val agreement: AfterSaleAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<OrderAfterSalePayItemBinding>,Int>(),TextWatcher {

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:27
     * @Note   数据提供
     */
    override fun dataProvider(): Any {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:29
     * @Note   item点击过滤器
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:30
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<OrderAfterSalePayItemBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.order_after_sale_pay_item)
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:32
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:32
     * @Note   创建LayHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return VlayoutHelper.createLinearLayoutHelper()
    }

    /**
     * @author LDD
     * @From   OrderAfterSalePayAdapter
     * @Date   2018/4/24 下午4:32
     * @Note   绑定数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<OrderAfterSalePayItemBinding>?, position: Int) {
        holder?.bind { 
            binding ->
            binding.data = payData
            if (payData.accountType == "ORG"){
                binding.orderAfterSalePayWechat.text = "原路退回"
                binding.orderAfterSalePayAlipay.gone()
                binding.orderAfterSalePayUnion.gone()
                binding.orderAfterSalePayAccount.gone()
                binding.orderAfterSalePayAccountInfoTitle.gone()
                binding.orderAfterSalePayBankAccount .gone()
                binding.orderAfterSalePayBankCreateBankname .gone()
                binding.orderAfterSalePayBankCreateUsername .gone()
                binding.orderAfterSalePayBankName .gone()
            }else{
                binding.orderAfterSalePayAlipay.setOnClickListener {
                    payData.accountType = "ALIPAY"
                    payData.reset()
                    agreement.payInfo(payData)
                }
                binding.orderAfterSalePayUnion.setOnClickListener {
                    payData.accountType = "BANKTRANSFER"
                    payData.reset()
                    agreement.payInfo(payData)
                }
                binding.orderAfterSalePayWechat.setOnClickListener {
                    payData.accountType = "WEIXINPAY"
                    payData.reset()
                    agreement.payInfo(payData)
                }
                binding.orderAfterSalePayAlipay.visable()
                binding.orderAfterSalePayUnion.visable()
                binding.orderAfterSalePayAccount.visable()
                binding.orderAfterSalePayAccountInfoTitle.visable()
                binding.orderAfterSalePayBankAccount .visable()
                binding.orderAfterSalePayBankCreateBankname .visable()
                binding.orderAfterSalePayBankCreateUsername .visable()
                binding.orderAfterSalePayBankName .visable()
                binding.orderAfterSalePayAlipay.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_navy))
                binding.orderAfterSalePayAlipay.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                binding.orderAfterSalePayWechat.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_navy))
                binding.orderAfterSalePayWechat.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                binding.orderAfterSalePayUnion.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_navy))
                binding.orderAfterSalePayUnion.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                when(payData.accountType){
                    "ALIPAY" ->{
                        binding.orderAfterSalePayAlipay.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_pay_red))
                        binding.orderAfterSalePayAlipay.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                        binding.orderAfterSalePayAccount.visable()
                        binding.orderAfterSalePayBankAccount .gone()
                        binding.orderAfterSalePayBankCreateBankname .gone()
                        binding.orderAfterSalePayBankCreateUsername .gone()
                        binding.orderAfterSalePayBankName .gone()
                    }
                    "WEIXINPAY" ->{
                        binding.orderAfterSalePayWechat.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_pay_red))
                        binding.orderAfterSalePayWechat.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                        binding.orderAfterSalePayAccount.visable()
                        binding.orderAfterSalePayBankAccount .gone()
                        binding.orderAfterSalePayBankCreateBankname .gone()
                        binding.orderAfterSalePayBankCreateUsername .gone()
                        binding.orderAfterSalePayBankName .gone()
                    }
                    "BANKTRANSFER" ->{
                        binding.orderAfterSalePayUnion.setTextColor(binding.root.context.getColorCompatible(R.color.javashop_color_pay_red))
                        binding.orderAfterSalePayUnion.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                        binding.orderAfterSalePayAccount.gone()
                        binding.orderAfterSalePayBankAccount .visable()
                        binding.orderAfterSalePayBankCreateBankname .visable()
                        binding.orderAfterSalePayBankCreateUsername .visable()
                        binding.orderAfterSalePayBankName .visable()
                    }
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

}