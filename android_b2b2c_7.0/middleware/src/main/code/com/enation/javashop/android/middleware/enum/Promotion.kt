package com.enation.javashop.android.middleware.enum

/**
 * @author LDD
 * @From   com.enation.javashop.android.middleware.enum
 * @Date   2018/2/2 下午1:38
 * @Note   促销活动类型
 */
enum class Promotion {
    FULL_DICOUNT{
        override fun toString(): String {
            return "满折"
        }
    }, FULL_MINUS{
        override fun toString(): String {
            return "满减"
        }
    }, POINT{
        override fun toString(): String {
            return "积分"
        }
    }, GIFT{
        override fun toString(): String {
            return "赠品"
        }
    }, BOUNS{
        override fun toString(): String {
            return "赠券"
        }
    }, SHIP{
        override fun toString(): String {
            return "免邮"
        }
    }, GROUP_BUY{
        override fun toString(): String {
            return "团购"
        }
    }, EXCHANGE{
        override fun toString(): String {
            return "积分商品"
        }
    }, SINGLE_MINUS{
        override fun toString(): String {
            return "单品立减"
        }
    }, HALFPRICE{
        override fun toString(): String {
            return "第二件半价"
        }
    }, SECKILL{
        override fun toString(): String {
            return "秒杀"
        }
    }
}

