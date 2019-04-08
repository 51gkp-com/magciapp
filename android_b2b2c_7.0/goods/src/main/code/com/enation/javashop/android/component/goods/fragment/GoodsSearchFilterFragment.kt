package com.enation.javashop.android.component.goods.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.adapter.GoodsSearchFilterChildAdapter
import com.enation.javashop.android.component.goods.adapter.GoodsSearchFilterParentAdapter
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterFragLayBinding
import com.enation.javashop.android.lib.utils.NoAlphaItemAnimator
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool
import java.lang.ref.WeakReference


/**
 * @author LDD
 * @Date   2018/3/20 下午2:27
 * @From   com.enation.javashop.android.component.goods.fragment
 * @Note   商品列表筛选Fragment
 */
class GoodsSearchFilterFragment : Fragment() {

    /**
     * @Name  datas
     * @Type  ArrayList<GoodsFilterViewModel>
     * @Note  筛选数据源
     */
    private lateinit var datas: ArrayList<GoodsFilterViewModel>

    /**
     * @Name  dataBinding
     * @Type  GoodsSearchFilterFragLayBinding
     * @Note  databinding对象
     */
    private val dataBinding by lazy {
        DataBindingUtil.bind<GoodsSearchFilterFragLayBinding>(LayoutInflater.from(context).inflate(R.layout.goods_search_filter_frag_lay, null))
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
     * @Name  confirmFlag
     * @Type  Boolean
     * @Note  标识是否点击了确认按钮
     */
    private var confirmFlag = false

    /**
     * @Name  adapterList
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapterList: ArrayList<DelegateAdapter.Adapter<*>> = ArrayList()

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午2:58
     * @Note   初始化操作
     */
    fun init() {

        /**初始化列表视图*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        dataBinding.goodsSearchFilterFragRv.layoutManager = virtualLayoutManager
        dataBinding.goodsSearchFilterFragRv.adapter = delegateAdapter
        dataBinding.goodsSearchFilterFragRv.itemAnimator = NoAlphaItemAnimator().closeDefaultAnimator()
        val weakActivity = WeakReference<FragmentActivity>(activity)
        val viewPool = RecyclerView.RecycledViewPool()
        dataBinding.goodsSearchFilterFragRv.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(1, 10)
        viewPool.setMaxRecycledViews(2, 10)
        /**初始化适配器 当item为最后一个时 设置tag为-1*/
        datas.forEach { item ->
            var parent = GoodsSearchFilterParentAdapter(weakActivity.get()!!, item)
            var child = GoodsSearchFilterChildAdapter(weakActivity.get()!!, item).then { self ->
                if (datas.indexOf(item) == datas.size - 1) {
                    self.tag = -1
                }
            }
            /**设置弱引用*/
            var weakChild = child.weak()

            /**设置打开子视图回调*/
            parent.setCallBack {
                weakChild.get()?.notifyChild()
            }
            adapterList.add(parent)
            adapterList.add(child)
        }

        /**设置adapter*/
        delegateAdapter.addAdapters(adapterList)

        /**设置回归事件*/
        dataBinding.goodsSearchFilterFragReset.setOnClickListener {
            datas.forEach { item ->
                item.selectedFilterValue = null
                item.selectedValueObser.set(null)
                item.valueList.forEach { itemValue ->
                    itemValue.selectedObservable.set(false)
                }
            }
        }

        /**设置确认事件*/
        dataBinding.goodsSearchFilterFragConfirm.setOnClickListener {
            confirmFlag = true
            (dataBinding.root.parent.parent as DrawerLayout).closeDrawer(Gravity.RIGHT)
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:00
     * @Note   drawlayout闭合事件回到 有GoodsSearchActivity调用
     */
    fun colseCallBack(){

        /**如果点击过了确认按钮 循环执行注入数据方法并初始化flag  如果没有 循环执行回归数据方法*/
        if (confirmFlag){
            adapterList.forEach { item ->
                if (item is GoodsSearchFilterChildAdapter) {
                    item.injectData()
                }
            }
            confirmFlag = false
        }else{
            adapterList.forEach { item ->
                if (item is GoodsSearchFilterChildAdapter) {
                    item.resetData()
                }
            }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:02
     * @Note   销毁调用
     */
    fun destory() {

    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:02
     * @Note   设置筛选数据
     */
    fun setFilterData(datas: ArrayList<GoodsFilterViewModel>) {
        this.datas = datas
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:02
     * @Note   创建View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dataBinding.root
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:03
     * @Note   View创建完成后回调
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    /**
     * @author LDD
     * @From   GoodsSearchFilterFragment
     * @Date   2018/3/21 下午3:03
     * @Note   View销毁时的回调
     */
    override fun onDestroyView() {
        super.onDestroyView()
        destory()
    }

}