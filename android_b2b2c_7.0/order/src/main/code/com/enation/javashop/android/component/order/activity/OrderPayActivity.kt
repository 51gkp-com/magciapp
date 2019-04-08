package com.enation.javashop.android.component.order.activity

import android.app.Activity
import android.graphics.Color
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderPayActLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.event.OrderDetailDataChange
import com.enation.javashop.android.middleware.event.OrderListDataChange
import com.enation.javashop.android.middleware.event.PayEvent
import com.enation.javashop.android.middleware.event.PayState
import com.enation.javashop.android.middleware.logic.contract.order.OrderPayContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderPayPresenter
import com.enation.javashop.android.middleware.model.OrderPayModel
import com.enation.javashop.android.middleware.model.PayMethodViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.pay.core.alipay.AlipayHelper
import com.enation.javashop.pay.core.wechat.WechatHelper
import com.enation.javashop.pay.iinter.PaymentActControl
import kotlinx.android.synthetic.main.order_create_payship_lay.*
import kotlinx.android.synthetic.main.order_pay_act_lay.*

/**
 * @author LDD
 * @Date   2018/4/22 上午10:20
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单支付页面
 */
@Router(path = "/order/pay")
class OrderPayActivity :BaseActivity<OrderPayPresenter,OrderPayActLayBinding>(),OrderPayContract.View,PaymentActControl {

    @Autowired(name= "pay",required = true)
    @JvmField var pay : OrderPayModel  = OrderPayModel()

    private lateinit var paymethods :ArrayList<PayMethodViewModel>

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:01
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_pay_act_lay
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:01
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:02
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.orderPayPriceTv.text = String.format("￥%.2f",pay.payPrice)
        presenter.loadPayMethod()
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:02
     * @Note   绑定事件
     */
    override fun bindEvent() {
        javashop_order_pay_header.setLeftClickListener {
            pop()
        }
        wechat_pay.setOnClickListener {
            paymethods.forEach {
                if(it.name.contains("微信")){
                    pay.payId = it.id
                }
            }
            order_pay_alipay_check_box.isChecked = false
            order_pay_wechat_check_box.isChecked = true
        }
        alipay_pay.setOnClickListener {
            paymethods.forEach {
                if(it.name.contains("支付宝")){
                    pay.payId = it.id
                }
            }
            order_pay_alipay_check_box.isChecked = true
            order_pay_wechat_check_box.isChecked = false
        }
        pay_info_tv.setOnClickListener {
            presenter.toPay(pay)
        }
        getEventCenter().register(PayEvent::class.java) { result ->
            when(result.state){
                PayState.CANCLE ->{
                    showMessage("支付取消")
                }
                PayState.FAILD ->{
                    showMessage("支付失败")
                }
                PayState.SUCCESS ->{
                    showMessage("支付成功")
                    getEventCenter().post(OrderDetailDataChange())
                    getEventCenter().post(OrderListDataChange())
                    pop()
                }
            }
            pop()
        }.joinManager(disposableManager)

    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:02
     * @Note   支付回调
     * @param  state 支付状态
     */
    override fun callApp(str: String) {
       if (pay.payId.contains("alipay")){
          AlipayHelper(str,this).pay()
       } else {
          WechatHelper(str,this).pay()
       }
    }
    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:03
     * @Note   渲染支付方式UI
     * @param  methods 支付方式
     */
    override fun renderPayMethod(methods: ArrayList<PayMethodViewModel>) {
        paymethods = methods
        methods.forEach {
            if (it.name.contains("微信")){
                wechat_pay.visable()
            }
            if (it.name.contains("支付宝")){
                alipay_pay.visable()
            }
        }

        if(methods.count() > 1){
            order_pay_line_b.visable()
        }else{
            order_pay_line_b.gone()
        }
        pay.payId = paymethods[0].id
        if(paymethods[0].name.contains("微信")){
            order_pay_alipay_check_box.isChecked = false
            order_pay_wechat_check_box.isChecked = true
            pay_info_tv.text = String.format("微信支付￥%.2f元",pay.payPrice)
        }
        if(paymethods[0].name.contains("付宝")){
            order_pay_alipay_check_box.isChecked = true
            order_pay_wechat_check_box.isChecked = false
            pay_info_tv.text = String.format("支付宝支付￥%.2f元",pay.payPrice)
        }
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:03
     * @Note   错误回调
     * @param  message 信息
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:04
     * @Note   请求完成
     * @param  message 完成信息
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:05
     * @Note   开始
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:05
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }
    
    /**
     * @author LDD
     * @From   OrderPayActivity
     * @Date   2018/4/24 下午4:06
     * @Note   销毁回调
     */
    override fun destory() {

    }

    override fun getActivity(): Activity {
        return this
    }

    override fun paymentCallback(code: Int, message: String?) {
        if(code == 1){
            getEventCenter().post(OrderDetailDataChange())
            getEventCenter().post(OrderListDataChange())
        }
        showMessage(message!!)
        pop()
    }
}