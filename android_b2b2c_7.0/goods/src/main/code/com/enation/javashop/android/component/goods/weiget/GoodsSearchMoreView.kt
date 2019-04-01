package com.enation.javashop.android.component.goods.weiget

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.support.constraint.ConstraintLayout
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.adapter.GoodsMoreActionAdapter
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchMoreActionDialogBinding
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopWindowCompatible
import com.enation.javashop.android.middleware.model.GoodsFilterValue
import com.enation.javashop.android.middleware.model.GoodsFilterViewModel
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/3/15 下午3:15
 * @From   com.enation.javashop.android.component.goods.weiget
 * @Note   更多筛选弹出框
 */
class GoodsSearchMoreView : PopWindowCompatible {

    /**
     * @Name  activity
     * @Type  Activity
     * @Note  调用页面上下文
     */
    private var activity: Activity

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private var delegateAdapter: DelegateAdapter

    /**
     * @Name  adapter
     * @Type  GoodsMoreActionAdapter
     * @Note  筛选列表适配器
     */
    private var adapter = GoodsMoreActionAdapter(GoodsFilterViewModel("",null,"", arrayListOf()))

    /**
     * @Name  binding
     * @Type  GoodsSearchMoreActionDialogBinding
     * @Note  视图对应Databinding
     */
    private var binding : GoodsSearchMoreActionDialogBinding

    /**
     * @Name  data
     * @Type  GoodsFilterViewModel
     * @Note  数据
     */
    private lateinit var data :GoodsFilterViewModel

    /**
     * @Name  confirmObserver
     * @Type  ((GoodsFilterValue?)->Unit)?
     * @Note  确定监听
     */
    private var confirmObserver :((GoodsFilterValue?)->Unit)? = null

    /**
     * @Name  dismissObserver
     * @Type  (()->(Unit))?
     * @Note  取消监听
     */
    private var dismissObserver : (()->(Unit))? = null

    /**
     * @Name  animIsEnd
     * @Type  Boolean
     * @Note  动画是否结束
     */
    private var animIsEnd = true

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:18
     * @Note   构造方法
     * @param  activity 调用页面
     * @param  height view高度
     */
    constructor(activity: Activity, height: Int): super(ScreenTool.getScreenWidth(activity).toInt(), height) {

        /**Pop初始化*/
        this.activity = activity
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())

        /**初始化适配器以及View*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        contentView = activity.layoutInflater.inflate(R.layout.goods_search_more_action_dialog,null)
        binding = DataBindingUtil.bind(contentView)
        binding.goodsSearchMoreActionDialogRv.adapter = delegateAdapter
        binding.goodsSearchMoreActionDialogRv.layoutManager = virtualLayoutManager
        delegateAdapter.addAdapter(adapter)
        /**绑定事件*/
        bindEvnet()
    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:27
     * @Note   绑定事件
     */
    fun bindEvnet(){

        /**设置点击Mask退出*/
        binding.root.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                adapter.resetData()
                confirmObserver?.invoke(data.selectedFilterValue)
                dismiss()
            }
            return@setOnTouchListener true
        }

        /**绑定重置事件*/
        binding.goodsSearchMoreActionDialogReset.setOnClickListener {
            data.selectedFilterValue = null
            data.selectedValueObser.set(null)
            data.valueList.forEach {
                item ->
                item.selectedObservable.set(false)
            }
        }

        /**绑定确定事件*/
        binding.goodsSearchMoreActionDialogConfirm.setOnClickListener {
            adapter.injectData()
            confirmObserver?.invoke(data.selectedFilterValue)
            dismiss()
        }

    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:28
     * @Note   设置取消事件
     * @param  obserable 取消响应事件
     */
    fun setOnDismissObserable(obserable:(()->(Unit))) :GoodsSearchMoreView{
        dismissObserver = obserable
        return this
    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:29
     * @Note   设置数据源
     * @param  data 数据源
     */
    fun setData(data :GoodsFilterViewModel):GoodsSearchMoreView{
        this.data = data

        /**重新设置列表视图高度*/
        binding.goodsSearchMoreActionDialogRv.reLayout<ConstraintLayout.LayoutParams> {
            params ->
            params.height = (ScreenTool.getScreenWidth(activity)/8*((data.valueList.size >10).judge(5,(data.valueList.size+1)/2))).toInt()
        }
        adapter.data = data

        /**刷新列表视图*/
        adapter.notifyDataSetChanged()
        return this
    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:29
     * @Note   设置确定监听
     * @param  obserable 监听事件
     */
    fun setConfirmObserable(obserable: (GoodsFilterValue?) -> Unit) :GoodsSearchMoreView{
        this.confirmObserver = obserable
        return this
    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:29
     * @Note   显示
     * @param  atView 显示在那个view下方
     */
    fun show(atView:View){

        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (data.valueList.size == 0 || !animIsEnd){
           return
        }

        /**mask先隐藏掉*/
        binding.goodsSearchMoreActionDialogMask.visibility = View.GONE

        /**将该View加载到页面之上*/
        showAsDropDown(atView)

        /**执行动画 起始y点为 -View.height 减去 Recly.height -button.height  */
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity,-(height - binding.goodsSearchMoreActionDialogRv.height - ScreenTool.getScreenWidth(activity)/7),350)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
                binding.goodsSearchMoreActionDialogMask.visibility = View.VISIBLE
            }

            /**动画开始 重置动画标记*/
            if (state == 3){
                animIsEnd = false
            }

            /**当返回true时 代表拦截当前动画 不再往下执行*/
            return@animSequentialStart false
        })
    }

    /**
     * @author LDD
     * @From   GoodsSearchMoreView
     * @Date   2018/3/15 下午3:36
     * @Note   重写dismiss事件
     */
    override fun dismiss() {

        /**动画未执行完毕 不执行下方代码*/
        if (!animIsEnd){
            return
        }

        /**延迟执行 优化操作体验*/
        AppTool.Time.delay(250,{
            dismissObserver?.invoke()
        })

        /**隐藏Mask*/
        binding.goodsSearchMoreActionDialogMask.visibility = View.GONE

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity,-height.toFloat(),500)),interceptor = {
            index: Int, state: Int ->

            /**动画执行完毕 设置标记 隐藏View*/
            if (state == 2){
                animIsEnd = true
                super.dismiss()
            }

            /**动画开始执行 还原标记*/
            if (state == 3){
                animIsEnd = false
            }

            /**当返回true时 代表拦截当前动画 不再往下执行*/
            return@animSequentialStart false
        })
    }
}