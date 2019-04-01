package com.enation.javashop.android.component.goods.weiget

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.drawable.BitmapDrawable
import android.support.constraint.ConstraintLayout
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterFirstDialogBinding
import com.enation.javashop.android.component.goods.databinding.GoodsSearchFilterFirstItemBinding
import com.enation.javashop.android.lib.adapter.ListViewBaseAdapter
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.animSequentialStart
import com.enation.javashop.android.lib.utils.judge
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.lib.widget.PopWindowCompatible
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/3/15 下午5:26
 * @From   com.enation.javashop.android.component.goods.weiget
 * @Note   综合按钮 更多操作
 */
class GoodsFirstMoreView :PopWindowCompatible{

    /**
     * @Name  activity
     * @Type  Activity
     * @Note  页面
     */
    private var activity :Activity

    /**
     * @Name  data
     * @Type  ArrayList<GoodsFirstMoreData>
     * @Note  数据
     */
    private var data :ArrayList<GoodsFirstMoreData>  = ArrayList()

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/4/4 上午10:45
     * @Note   数据绑定
     */
    private lateinit var binding :GoodsSearchFilterFirstDialogBinding

    /**
     * @Name  adapter
     * @Type  ListViewBaseAdapter<GoodsFirstMoreData,GoodsSearchFilterFirstItemBinding>
     * @Note  列表视图适配器
     */
    private lateinit var adapter :ListViewBaseAdapter<GoodsFirstMoreData,GoodsSearchFilterFirstItemBinding>

    /**
     * @Name  confirmObserver
     * @Type  ((GoodsFilterValue?)->Unit)?
     * @Note  确定监听
     */
    private var confirmObserver :((GoodsFirstMoreData)->Unit)? = null

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

    /**构造方法*/
    constructor(activity: Activity,height : Int): super(ScreenTool.getScreenWidth(activity).toInt(), height){
        this.activity = activity
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        createUI()
    }

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/4/4 上午10:46
     * @Note   创建UI
     */
    private fun createUI(){
        contentView = activity.layoutInflater.inflate(R.layout.goods_search_filter_first_dialog,null)
        binding = DataBindingUtil.bind(contentView)
        binding.goodsSearchFilterFirstMask.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN ){
                dismiss()
            }
            return@setOnTouchListener true
        }
        adapter = object : ListViewBaseAdapter<GoodsFirstMoreData, GoodsSearchFilterFirstItemBinding>(activity,R.layout.goods_search_filter_first_item,data) {
            override fun fillItem(binding: GoodsSearchFilterFirstItemBinding, data: GoodsFirstMoreData, position: Int) {
                binding.data = data
            }

            override fun itemClick(data: GoodsFirstMoreData, position: Int) {
                this@GoodsFirstMoreView.data.forEach {
                    item ->
                    (item == data).judge(trueDo = {
                        item.selected = true
                        item.selectedObservable.set(true)
                        confirmObserver?.invoke(item)
                    },falseDo = {
                        item.selected = false
                        item.selectedObservable.set(false)
                    })
                    dismiss()
                }
            }
        }
        binding.goodsSearchFilterFirstLv.adapter = adapter
    }

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/4/4 上午10:47
     * @Note   设置数据
     */
    fun setData(data :ArrayList<GoodsFirstMoreData>):GoodsFirstMoreView{
        this.data = data
        adapter.datas = data
        adapter.notifyDataSetChanged()
        binding.goodsSearchFilterFirstLv.reLayout<ConstraintLayout.LayoutParams> {
            params ->
            params.height = (data.size >= 5).judge((ScreenTool.getScreenWidth(activity)/2).toInt(),((ScreenTool.getScreenWidth(activity)/10)*data.size).toInt())
        }
        return this
    }

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/3/15 下午3:28
     * @Note   设置取消事件
     * @param  obserable 取消响应事件
     */
    fun setOnDismissObserable(obserable:(()->(Unit))) :GoodsFirstMoreView{
        dismissObserver = obserable
        return this
    }

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/3/15 下午3:29
     * @Note   设置确定监听
     * @param  obserable 监听事件
     */
    fun setConfirmObserable(obserable: (GoodsFirstMoreData?) -> Unit) :GoodsFirstMoreView{
        this.confirmObserver = obserable
        return this
    }

    fun show(atView :View){
        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (data.size == 0 || !animIsEnd){
            return
        }

        /**mask先隐藏掉*/
        binding.goodsSearchFilterFirstMask.visibility = View.GONE

        /**将该View加载到页面之上*/
        showAsDropDown(atView)

        /**执行动画 起始y点为 -View.height 减去 Recly.height -button.height  */
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity, -binding.goodsSearchFilterFirstLv.layoutParams.height.toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
                binding.goodsSearchFilterFirstMask.visibility = View.VISIBLE
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
     * @From   GoodsFirstMoreView
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
        binding.goodsSearchFilterFirstMask.visibility = View.GONE

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity,-binding.goodsSearchFilterFirstLv.layoutParams.height.toFloat(),300)),interceptor = {
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

    /**
     * @author LDD
     * @Date   2018/4/4 上午10:37
     * @From   GoodsFirstMoreData
     * @Note   商品数据监听
     */
    data class  GoodsFirstMoreData(val name:String,val value:Int ,var selected :Boolean){
        var selectedObservable = ObservableField(selected)
    }
}