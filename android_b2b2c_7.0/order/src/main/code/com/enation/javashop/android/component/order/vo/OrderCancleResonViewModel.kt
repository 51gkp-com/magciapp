package com.enation.javashop.android.component.order.vo

import android.databinding.Observable
import android.databinding.ObservableField
import com.enation.javashop.android.lib.utils.then

/**
 * @author LDD
 * @Date   2018/4/19 下午3:44
 * @From   com.enation.javashop.android.component.order.vo
 * @Note   取消订单选择VO
 */
data class OrderCancleResonViewModel(val title:String,
                                     var selected:Boolean,
                                     val index :Int){

    /**
     * @Name  selectorObservable
     * @Type  ObservableField<Boolean>
     * @Note  绑定监听
     */
    val selectObservable = ObservableField(selected)

    companion object {

        /**
         * @author LDD
         * @From   OrderCancleResonViewModel
         * @Date   2018/4/19 下午3:56
         * @Note   快速构建取消订单原因数据源
         */
        fun createResons() :ArrayList<OrderCancleResonViewModel>{
            return ArrayList<OrderCancleResonViewModel>().then {
                self ->
                self.add(OrderCancleResonViewModel("订单不能按预计时间送达",false,0))
                self.add(OrderCancleResonViewModel("操作有误(商品,地址选错等)",false,1))
                self.add(OrderCancleResonViewModel("重复下单/误下单",false,2))
                self.add(OrderCancleResonViewModel("其他渠道价格更低",false,3))
                self.add(OrderCancleResonViewModel("该商品降价了",false,4))
                self.add(OrderCancleResonViewModel("不想买了",false,5))
                self.add(OrderCancleResonViewModel("商品无货",false,6))
                self.add(OrderCancleResonViewModel("其他原因",false,7))
            }.then {
                self ->
                self.forEach {
                    item ->
                    item.selectObservable.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                            if (item.selectObservable.get()){
                                item.selected = true
                                self.forEach {
                                    itemChild ->
                                    if (itemChild != item){
                                        itemChild.selectObservable.set(false)
                                        itemChild.selected = false
                                    }
                                }
                            }else{
                                item.selected = false
                            }
                        }
                    })
                }
            }
        }
    }
}