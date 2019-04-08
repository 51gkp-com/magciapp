package com.enation.javashop.android.middleware.model

/**
 * @author Snow
 * @Date   2018/8/21 下午12:24
 * @Note   订单可进行的操作Model
 */
class OrderActionModel(
        val allowCancel :Boolean,       /** 是否允许被取消 */
        val allowConfirm :Boolean,      /** 是否允许被确认 */
        val allowPay :Boolean,          /** 是否允许被支付 */
        val allowShip :Boolean,         /** 是否允许被发货 */
        val allowRog :Boolean,          /** 是否允许被收货 */
        val allowComment :Boolean,         /** 是否允许被评论 */
        val allowComplete :Boolean,        /** 是否允许被完成 */
        val allowApplyService :Boolean,    /** 是否允许申请售后 */
        val allowServiceCancel :Boolean,    /** 是否允许取消(售后) */
        val allowExpress :Boolean){
        fun getCancelState():Boolean{
                return allowServiceCancel || allowCancel
        }
        fun all() :Boolean{
                return allowCancel || allowRog || allowPay || allowExpress || allowComment || getCancelState()
        }
}