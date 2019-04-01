package com.enation.javashop.android.component.home.fragment

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.activity.HomeActivity
import com.enation.javashop.android.component.home.adapter.ChildCategoryAdapter
import com.enation.javashop.android.component.home.adapter.ParentCategoryAdapter
import com.enation.javashop.android.component.home.databinding.CategoryFragLayBinding
import com.enation.javashop.android.component.home.databinding.CategroyLeftItemBinding
import com.enation.javashop.android.component.home.launch.HomeLaunch
import com.enation.javashop.android.lib.adapter.ListViewBaseAdapter
import com.enation.javashop.android.lib.adapter.VlayoutHolderAdapter
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.middleware.logic.contract.home.CategoryFragmentContract
import com.enation.javashop.android.middleware.logic.presenter.home.CategoryFragmentPresenter
import com.enation.javashop.android.middleware.model.ChildCategoryShell
import com.enation.javashop.android.middleware.model.ChildCategoryViewModel
import com.enation.javashop.android.middleware.model.ParentCategoryViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.utils.base.tool.ScreenTool
import kotlinx.android.synthetic.main.category_frag_lay.*
import kotlin.collections.ArrayList

/**
 * @author LDD
 * @Date   2018/1/22 下午12:38
 * @From   com.enation.javashop.android.component.home.fragment
 * @Note   分类Fragment
 */
class CategoryFragment :BaseFragment<CategoryFragmentPresenter,CategoryFragLayBinding>(),CategoryFragmentContract.View {

    /**
     * 左侧列表数据
     */
    private val leftViewData = ArrayList<ParentCategoryViewModel>()

    /**
     * 右侧列表布局控制器
     */
    private lateinit var virtualLayoutManager :VirtualLayoutManager

    /**
     * 右侧列表视图父适配器
     */
    private lateinit var delegateAdapter :DelegateAdapter


    /** 适配器列表设置 */
    val adapterList = ArrayList<DelegateAdapter.Adapter<*>>()
    /**
     * 占位视图
     */
    private val holderView = VlayoutHolderAdapter(object :VlayoutHolderAdapter.HolderCallBack{
        override fun bindView(holder: RecyclerView.ViewHolder) {
            holder.itemView.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.dip2px(holder.itemView.context,10f)))
            holder.itemView.setBackgroundResource(R.color.javashop_color_category_rv_bg)
        }

        override fun layoutHelperProvider(): LayoutHelper {
            return LinearLayoutHelper(0,1)
        }

        override fun viewHolderProvider(parent : ViewGroup): RecyclerView.ViewHolder {
            return VlayoutHolderAdapter.PlaceHolder(View(parent.context))
        }

    })

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:46
     * @Note   提供layoutId
     * @return layoutId
     */
    override fun getLayId(): Int {
        return R.layout.category_frag_lay
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:46
     * @Note   依赖注入初始化
     */
    override fun bindDagger() {
        HomeLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:47
     * @Note   初始化页面
     */
    override fun init() {
        mViewDataBinding.categorySearchLay.setOnClickListener(OnClickListenerAntiViolence({
            push("/search/main")
        }))
        initLeft()
        initRight()
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:47
     * @Note   绑定事件
     */
    override fun bindEvent() {
        category_frag_my_iv.setOnClickListener {
            activity.to<HomeActivity>().toMember()
        }
        category_frag_scan_iv.setOnClickListener {
            push("/extra/scan")
        }
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:47
     * @Note   页面销毁时调用
     */
    override fun destory() {

    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:47
     * @Note   展示子分类
     * @param  childCategoryList 子分类数据
     */
    override fun showChildCatList(childCategoryList: ArrayList<ChildCategoryShell>) {

        if(delegateAdapter.adaptersCount > 0){
            delegateAdapter.removeAdapters(adapterList)
        }

        /** 适配器列表设置 */
        adapterList.then { self ->

            self.clear()

            /**循环设置*/
            childCategoryList.forEach { item ->

                /** 添加二级分类视图 */
                self.add(ParentCategoryAdapter(item.parentName))

                /** 初始化三级分类适配器 */
                val childAdapter = ChildCategoryAdapter(GridLayoutHelper(3).then {
                    self ->
                    /** 设置视图不自动展开 */
                    self.setAutoExpand(false)
                },item.item)

                /** 设置Item点击事件 */
                childAdapter.setOnItemClickListener { data, position ->
                    childClick(data)
                }

                self.add(childAdapter)

                /** 添加占位视图 */
                self.add(holderView)

            }

        }
        /**重置LayoutHelper*/
        virtualLayoutManager.setLayoutHelpers(ArrayList<LayoutHelper>().then {self ->
            adapterList.forEach {
                self.add(it.onCreateLayoutHelper())
            }
        })
        /** 添加适配器 */
        delegateAdapter.addAdapters(adapterList)
        /** 通知视图更新 */
        delegateAdapter.notifyDataSetChanged()
        /** 刷新视图 */
        category_right_rv.scrollToPosition(0)

        category_right_rv.scrollToPosition(0)

    }

    /**
     * 子分类点击事件
     */
    private fun childClick(data :ChildCategoryViewModel){
        push("/goods/list",{
            it.withInt("category",data.catId)
            it.withString("hint",data.name)
        })
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:48
     * @Note   展示父分类列表
     * @param  parentCategoryList  父分类数据
     */
    override fun showParentCatList(parentCategoryList: ArrayList<ParentCategoryViewModel>) {
        leftViewData.addAll(parentCategoryList)
        listViewAdapter.notifyDataSetChanged()
        presenter.loadChildCat(parentCategoryList[0].parentId)
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:49
     * @Note   逻辑错误时 展示错误信息
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:50
     * @Note   耗时操作完成时调用
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {

    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:50
     * @Note   耗时操作开始
     */
    override fun start() {

    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/22 下午2:50
     * @Note   网络状态监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {
        state.filter(onMobile = {

        },onWifi = {

        },offline = {

        })
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/29 下午8:43
     * @Note   初始化父分类
     */
    private fun initLeft(){
        mViewDataBinding.categoryLeftLv.adapter = listViewAdapter
        presenter.loadParentCat()
    }

    /**
     * @author LDD
     * @From   CategoryFragment
     * @Date   2018/1/29 下午8:44
     * @Note   初始化子分类
     */
    private fun initRight(){
        virtualLayoutManager =  VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        mViewDataBinding.categoryRightRv.layoutManager = virtualLayoutManager
        mViewDataBinding.categoryRightRv.adapter = delegateAdapter
    }

    /**
     * @Name  listViewAdapter
     * @Type  ListViewBaseAdapter<ParentCategory, CategroyLeftItemBinding>
     * @Note  父分类的适配器 单例 懒加载
     */
    private val listViewAdapter :ListViewBaseAdapter<ParentCategoryViewModel, CategroyLeftItemBinding> by lazy {
        object: ListViewBaseAdapter<ParentCategoryViewModel, CategroyLeftItemBinding>(context,R.layout.categroy_left_item,leftViewData),AbsListView.OnScrollListener{

            /**
             * @Name  firstPostion
             * @Type  int
             * @Note  屏幕上显示的第一个item坐标
             */
            private var firstPostion = 0

            /**
             * 初始化时调用
             */
            init {
                mViewDataBinding.categoryLeftLv.setOnScrollListener(this)
            }

            /**
             * @author LDD
             * @From   AbsListView.OnScrollListener
             * @Date   2018/1/30 下午1:37
             * @Note   滑动回调
             */
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                firstPostion= firstVisibleItem
            }

            /**
             * @author LDD
             * @From   AbsListView.OnScrollListener
             * @Date   2018/1/30 下午1:38
             * @Note   滑动状态改变
             */
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }

            /**
             * @author LDD
             * @From   CategoryFragment
             * @Date   2018/1/30 下午1:38
             * @Note   填充视图
             * @param  binding  databinding
             * @param  data     数据
             * @param  position 坐标
             */
            override fun fillItem(binding: CategroyLeftItemBinding, data: ParentCategoryViewModel, position: Int) {
                binding.itemData = data
            }

            /**
             * @author LDD
             * @From   CategoryFragment
             * @Date   2018/1/30 下午1:39
             * @Note   单击事件
             * @param  data 数据
             * @param  position 坐标
             */
            override fun itemClick(data: ParentCategoryViewModel, position: Int) {
                for (i in 0 until leftViewData.size){
                    if (i == position){
                        leftViewData[i].selected.set(true)
                        presenter.loadChildCat(leftViewData[i].parentId)
                    }else{
                        leftViewData[i].selected.set(false)
                    }
                }
                scroll(position)
            }

            /**
             * @author LDD
             * @From   CategoryFragment
             * @Date   2018/1/30 下午1:40
             * @Note   重新调整item大小
             * @param  convertView item
             */
            override fun reSize(convertView: View) {
                convertView.layoutParams.height = ((ScreenTool.getScreenHeight(context)-ScreenTool.dip2px(context,50f))*0.87/9).toInt()
                convertView.layoutParams.width = (ScreenTool.getScreenWidth(context)*0.23).toInt()
            }

            /**
             * @author LDD
             * @From   CategoryFragment
             * @Date   2018/1/30 下午1:40
             * @Note   滑动具体操作
             * @param  position 需要滑动的position
             */
            private fun scroll(position:Int){
                if (position < (firstPostion + 4)) {
                    category_left_lv.post {
                        category_left_lv.smoothScrollToPosition(position - 4 )
                    }
                } else {
                    category_left_lv.post {
                        category_left_lv.smoothScrollToPosition(position + 4 )
                    }
                }

            }
        }
    }
}