package com.enation.javashop.android.component.goods.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.adapter.GoodsSearchFilterBrandAdapter
import com.enation.javashop.android.component.goods.databinding.GoodsSearachFilterValueFragLayBinding
import com.enation.javashop.android.lib.utils.ChinaeseSortHelper
import com.enation.javashop.android.lib.utils.NoAlphaItemAnimator
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.middleware.model.GoodsFilterValue
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author LDD
 * @Date   2018/3/23 上午10:44
 * @From   com.enation.javashop.android.component.goods.fragment
 * @Note   品牌筛选Fragment
 */
class GoodsSearchFilterValueFragment :Fragment() {

    /**
     * @Name  datas
     * @Type  ArrayList<GoodsFilterValue>
     * @Note  排序后的数据列表
     */
    private lateinit var datas :ArrayList<GoodsFilterValue>

   /**
    * @Name  strList
    * @Type  ArrayList<String>
    * @Note  排序后的首字母集合
    */
    private lateinit var strList:ArrayList<String>

    /**
     * @Name  orgData
     * @Type  GoodsFilterViewModel
     * @Note  原始数据
     */
    private lateinit var orgData :GoodsFilterViewModel

    /**
     * @Name  dataBinding
     * @Type  GoodsSearachFilterValueFragLayBinding
     * @Note  databinding对象
     */
    private val dataBinding by lazy {
        DataBindingUtil.bind<GoodsSearachFilterValueFragLayBinding>(layoutInflater?.inflate(R.layout.goods_searach_filter_value_frag_lay,null))
    }

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private lateinit var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/22 下午1:51
     * @Note   初始化
     */
    fun init(){
        /**初始化列表视图*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        dataBinding.goodsSearchFilterValueRv.layoutManager = virtualLayoutManager
        dataBinding.goodsSearchFilterValueRv.adapter = delegateAdapter
        dataBinding.goodsSearchFilterValueRv.itemAnimator = NoAlphaItemAnimator().closeDefaultAnimator()
        val viewPool = RecyclerView.RecycledViewPool()
        dataBinding.goodsSearchFilterValueRv.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(1, 10)
        viewPool.setMaxRecycledViews(2, 10)
        delegateAdapter.addAdapter(GoodsSearchFilterBrandAdapter(activity,datas))

        /**设置返回事件*/
        dataBinding.goodsSearchFilterValueFragBack.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }

        /**初始化首字母查询View*/
        dataBinding.goodsSearchFilterValueFastFindLay.setLetter(strList)
        dataBinding.goodsSearchFilterValueFastFindLay.setOnTouchingLetterChangedListener {
            letter ->
            for (data in datas) {
                if (data.letter == letter){
                    dataBinding.goodsSearchFilterValueRv.scrollToPosition(datas.indexOf(data))
                    return@setOnTouchingLetterChangedListener
                }
            }
        }

        /**设置确认事件*/
        dataBinding.goodsSearchFilterValueFragConfrim.setOnClickListener {
            confirm()
            activity.supportFragmentManager.popBackStack()
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/22 下午1:51
     * @Note   销毁回调
     */
    fun destory(){

    }


    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/21 下午3:02
     * @Note   创建View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dataBinding.root
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/21 下午3:03
     * @Note   View创建完成后回调
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/21 下午3:03
     * @Note   View销毁时的回调
     */
    override fun onDestroyView() {
        super.onDestroyView()
        destory()
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/23 上午10:47
     * @Note   注入列表数据 并初始化排序
     * @param  filter 筛选数据
     */
    fun setData(filter : GoodsFilterViewModel){
        orgData = filter
        initSoreBrandData()
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/23 上午10:47
     * @Note   确认操作
     */
    fun confirm(){
        datas.forEach {
            item ->
                if (item.selectedObservable.get()){
                    orgData.selectedValueObser.set(item)
                    orgData.valueList.forEach {
                        if (item == it){
                            it.selectedObservable.set(true)
                        }else{
                            it.selectedObservable.set(false)
                        }
                    }
                }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterValueFragment
     * @Date   2018/3/23 上午10:48
     * @Note   初始化按字母排序
     */
    private fun initSoreBrandData(){
        strList = ArrayList()

        /**循环筛选首字母*/
        orgData.valueList.forEach {
            item ->
            val str = ChinaeseSortHelper.getFirstSpell(item.name)
            item.letter = str
            if (!strList.contains(str)){
                strList.add(str)
            }
        }

        /**首字母排序*/
        val cmp = Collator.getInstance(Locale.ENGLISH)
        Collections.sort(strList, cmp)
        datas = ArrayList()
        strList.forEach {
            s->
            orgData.valueList.forEach {
                item ->
                if (item.letter == s && !datas.contains(item)){
                    datas.add(item.copy())
                }
            }
        }
    }


}