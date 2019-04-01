package com.enation.javashop.android.component.order.activity

import android.databinding.ObservableField
import android.graphics.Color
import com.enation.javashop.android.component.order.R
import com.enation.javashop.android.component.order.databinding.OrderCreatePayshipLayBinding
import com.enation.javashop.android.component.order.launch.OrderLaunch
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.logic.contract.order.OrderCreatePayShipContract
import com.enation.javashop.android.middleware.logic.presenter.order.OrderCreatePayShipPresenter
import com.enation.javashop.android.middleware.model.SingleIntViewModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.order_create_payship_lay.*

/**
 * @author LDD
 * @Date   2018/5/23 下午6:56
 * @From   com.enation.javashop.android.component.order.activity
 * @Note   订单创建支付方式页面
 */
@Router(path = "/order/create/payship")
class OrderCreatePayShipActivity : BaseActivity<OrderCreatePayShipPresenter, OrderCreatePayshipLayBinding>(), OrderCreatePayShipContract.View  {

    /**伴生对象*/
    companion object {

        /**
         * @Name  com.enation.javashop.android.component.order.activity.OrderCreatePayShipActivity.Companion.SHIP
         * @Type  String
         * @Note  快递时间
         */
        const val PARAMS_KEY_SHIP_TIME = "SHIP_TIME"

        /**
         * @Name  com.enation.javashop.android.component.order.activity.OrderCreatePayShipActivity.Companion.PAY_METHOD
         * @Type  String
         * @Note  支付方式
         */
        const val PARAMS_KEY_PAY_METHOD = "PAY_METHOD"

    }

    /**
     * @Name  payData
     * @Type  SingleIntViewModel
     * @Note  支付方式
     */
    @Autowired(name = PARAMS_KEY_PAY_METHOD,required = true)
    @JvmField var payData = SingleIntViewModel(ObservableField(GlobalState.PAY_ONLINE))

    /**
     * @Name  shipData
     * @Type  SingleIntViewModel
     * @Note  配送方式
     */
    @Autowired(name = PARAMS_KEY_SHIP_TIME,required = true)
    @JvmField var shipData = SingleIntViewModel(ObservableField(GlobalState.SHIP_EVERYTIME))


    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:31
     * @Note   获取LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.order_create_payship_lay
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:31
     * @Note   依赖注入
     */
    override fun bindDagger() {
        OrderLaunch.component.inject(this)
        JRouter.prepare().inject(this)
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:30
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.pay = payData
        mViewBinding.ship = shipData
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:30
     * @Note   绑定事件
     */
    override fun bindEvent() {

        order_create_payship_topbar.setLeftClickListener {
            pop()
        }

        order_create_payship_confrim.setOnClickListener {
            val shipState = shipData.state.get()
            val paystate =  payData.state.get()
            var shipParams = ""
            var payParams = ""
            if (shipState == GlobalState.SHIP_EVERYTIME){
                shipParams = "任何时间"
            }else if (shipState == GlobalState.SHIP_WORKTIME){
                shipParams = "仅工作日"
            }else if (shipState == GlobalState.SHIP_RESTTIME){
                shipParams = "仅休息日"
            }

            if (paystate == GlobalState.PAY_COD){
                payParams = "COD"
            }else if (paystate == GlobalState.PAY_ONLINE){
                payParams = "ONLINE"
            }

            presenter.setData(shipParams,payParams)
        }

        order_create_payship_pay_cod.setOnClickListener {
            payData.state.set(GlobalState.PAY_COD)
        }

        order_create_payship_pay_noline.setOnClickListener {
            payData.state.set(GlobalState.PAY_ONLINE)
        }

        order_create_payship_ship_everytime.setOnClickListener {
            shipData.state.set(GlobalState.SHIP_EVERYTIME)
        }

        order_create_payship_ship_resttime.setOnClickListener {
            shipData.state.set(GlobalState.SHIP_RESTTIME)
        }

        order_create_payship_ship_worktime.setOnClickListener {
            shipData.state.set(GlobalState.SHIP_WORKTIME)
        }

    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:10
     * @Note   错误回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun onError(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:10
     * @Note   完成回调
     * @param  message 信息
     * @param  type    类型
     */
    override fun complete(message: String, type: Int) {
        setResult(GlobalState.ORDER_CREATE_PAYSHIP,intent)
        pop()
    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:10
     * @Note   开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:10
     * @Note   网络监控
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   OrderCreatePayShipActivity
     * @Date   2018/5/23 下午7:11
     * @Note   销毁回调
     */
    override fun destory() {

    }
}