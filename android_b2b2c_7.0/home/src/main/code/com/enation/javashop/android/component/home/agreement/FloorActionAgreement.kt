package com.enation.javashop.android.component.home.agreement

import android.view.MenuItem
import android.view.View
import com.enation.javashop.android.middleware.model.BannerModel
import com.enation.javashop.android.middleware.model.FloorItem
import com.enation.javashop.android.middleware.model.FloorMenuModel

/**
 * Created by LDD on 2018/10/16.
 */
interface FloorActionAgreement {

    fun pointMall()

    fun secKill()

    fun groupMall()

    fun couponHall()

    fun toShop(id :Int)

    fun toWeb(url :String)

    fun searchGoodsForKeyWord(keyword :String)

    fun searchGoodsForCatrgory(catId :Int,text :String)

    fun goods(goodsId :Int)

    fun floorHandle(view :View , item :Any){

        view.setOnClickListener {
            event(item)
        }

    }

    fun event(item :Any){
        if (item is BannerModel){
            if(item.action == "URL"){
                if(item.value.contains("point")){
                    pointMall()
                }else if(item.value.contains("group")){
                    groupMall()
                }else if(item.value.contains("sec")){
                    secKill()
                }else{
                    toWeb(item.value)
                }
            }else if(item.action == "KEYWORD"){
                searchGoodsForKeyWord(item.value)
            }else if(item.action == "GOODS"){
                goods(item.value.toInt())
            }else if(item.action == "CATEGORY"){
                searchGoodsForCatrgory(item.value.toInt(),"搜索结果")
            }else if(item.action == "SHOP"){
                toShop(item.value.toInt())
            }
        }
        if (item is FloorItem){
            if(item.opt!!.type == "URL"){
                if(item.opt!!.value.contains("point")){
                    pointMall()
                }else if(item.opt!!.value.contains("group")){
                    groupMall()
                }else if(item.opt!!.value.contains("sec")){
                    secKill()
                }else{
                    toWeb(item.opt!!.value)
                }
            }else if(item.opt!!.type == "KEYWORD"){
                searchGoodsForKeyWord(item.opt!!.value)
            }else if(item.opt!!.type == "GOODS"){
                goods(item.opt!!.value.toInt())
            }else if(item.opt!!.type == "CATEGORY"){
                searchGoodsForCatrgory(item.opt!!.value.toInt(),"搜索结果")
            }else if(item.opt!!.type == "SHOP"){
                toShop(item.opt!!.value.toInt())
            }
        }
        if (item is FloorMenuModel){
            if (item.action.contains("points-mall")){
                pointMall()
            }else if (item.action.contains("group-buy")){
                groupMall()
            }else if (item.action.contains("seckill")){
                secKill()
            }else if (item.action.contains("goods")){
                goods(item.action.removePrefix("/goods/").toInt())
            }else if(item.action.contains("coupons")){
                couponHall()
            }else{
                toWeb(item.action)
            }
        }
    }





}