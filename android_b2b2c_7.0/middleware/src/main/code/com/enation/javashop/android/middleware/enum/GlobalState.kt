package com.enation.javashop.android.middleware.enum


object GlobalState{

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.REGISTER
     * @Type  Int
     * @Note  注册
     */
    const val REGISTER_USER = 1

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.BIND
     * @Type  Int
     * @Note  绑定手机号
     */
    const val BIND_PHONE = 2

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.FIND
     * @Type  Int
     * @Note  找回密码
     */
    const val FIND_PASSWORD= 3


    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.EDIT_PASSWORD
     * @Type  Int
     * @Note  修改密码
     */
    const val EDIT_PASSWORD = 4

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.BIND
     * @Type  Int
     * @Note  更换手机号
     */
    const val UPDATE_PHONE = 5


    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.DYNAMIC_LOGIN
     * @Type  Int
     * @Note  动态登录
     */
    const val VALIDATE_MOBILE = -1

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.DYNAMIC_LOGIN
     * @Type  Int
     * @Note  动态登录
     */
    const val DYNAMIC_LOGIN = 6

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.LOGIN
     * @Type  Int
     * @Note  普通登录
     */
    const val LOGIN = 7

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.ORDER_CREATE_PAYSHIP
     * @Type  String
     * @Note  支付配送
     */
    const val ORDER_CREATE_PAYSHIP = 8

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.ORDER_CREATE_ADDRESS
     * @Type  String
     * @Note  地址
     */
    const val ORDER_CREATE_ADDRESS = 9

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.ORDER_CREATE_RECEIPT
     * @Type  String
     * @Note  发票
     */
    const val ORDER_CREATE_RECEIPT = 10

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.ORDER_CREATE_RECEIPT
     * @Type  String
     * @Note  优惠券
     */
    const val ORDER_CREATE_COUPON = 11

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.PAY_ONLINE
     * @Type  String
     * @Note  在线支付
     */
    const val PAY_ONLINE = 12

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.PAY_COD
     * @Type  String
     * @Note  货到付款
     */
    const val PAY_COD = 13

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.SHIP_EVERYTIME
     * @Type  String
     * @Note  任何时间送货
     */
    const val SHIP_EVERYTIME = 14

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.SHIP_WORKTIME
     * @Type  String
     * @Note  仅工作日送货
     */
    const val SHIP_WORKTIME = 15

    /**
     * @Name  com.enation.javashop.android.middleware.enum.GlobalState.SHIP_RESTTIME
     * @Type  String
     * @Note  仅休息日送货
     */
    const val SHIP_RESTTIME = 16


    /**
     * @author LDD
     * @From   GlobalState
     * @Date   2018/5/27 下午4:30
     * @Note   text转state
     * @param  text text
     */
    fun asState(text :String) :Int{
        when(text){
            "货到付款" ->{
                return PAY_COD
            }
            "在线支付" ->{
                return PAY_ONLINE
            }
            "任何时间" ->{
                return SHIP_EVERYTIME
            }
            "仅休息日" ->{
                return SHIP_RESTTIME
            }
            "仅工作日" ->{
                return SHIP_WORKTIME
            }
            else ->{
                return -1
            }
        }
    }

    /**
     * @author LDD
     * @From   GlobalState
     * @Date   2018/5/27 下午3:42
     * @Note   转换对应文字
     * @param  state 状态
     */
    fun asText(state : Int):String{
        when(state){
            PAY_COD ->{
                return "货到付款"
            }
            PAY_ONLINE ->{
                return "在线支付"
            }
            SHIP_EVERYTIME ->{
                return "任何时间"
            }
            SHIP_RESTTIME ->{
                return "仅休息日"
            }
            SHIP_WORKTIME ->{
                return "仅工作日"
            }
            else ->{
                return "error"
            }
        }
    }

}
