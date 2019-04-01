package com.enation.javashop.android.lib.widget

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
import com.enation.javashop.android.lib.databinding.PopCommonLayBinding
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/19 下午12:38
 * @From   com.enation.javashop.android.lib.widget
 * @Note   通用列表对话框
 */
class PopCommonView : PopWindowCompatible{

    /**
     * @Name  activity
     * @Type  Activity
     * @Note  页面
     */
    private var activity : Activity

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/4/4 上午10:45
     * @Note   数据绑定
     */
    private lateinit var binding : PopCommonLayBinding

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
     * @Name  confirmObserver
     * @Type  ((GoodsFilterValue?)->Unit)?
     * @Note  确定监听
     */
    private var confirmObserver :(()->Unit)? = null

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
     * @Date   2018/4/19 下午3:20
     * @From   PopCommonView
     * @Note   伴生对象
     */
    companion object {

        fun getHeight() :Int{
            var dis  = DisplayMetrics()
            JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getRealMetrics(dis)
            return dis.heightPixels - ScreenTool.getVirtualBarHeigh(JavaShopActivityTask.instance.peekTopActivity() as AppCompatActivity?)
        }

        /**
         * @author LDD
         * @From   PopCommonView
         * @Date   2018/4/19 下午3:21
         * @Note   静态构建
         * @param  activity 页面
         */
        fun build(activity: Activity) :PopCommonView{
            return PopCommonView(activity)
        }
    }

    /**构造方法*/
    private constructor(activity: Activity): super(ScreenTool.getScreenWidth(activity).toInt(), (PopCommonView.getHeight() - AppTool.SystemUI.getStatusBarHeight())){
        this.activity = activity
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        createUI()
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:21
     * @Note   构建UI
     */
    private fun createUI(){
        contentView = activity.layoutInflater.inflate(R.layout.pop_common_lay,null)
        binding = DataBindingUtil.bind(contentView)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)
        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        binding.popCommonRv.layoutManager = virtualLayoutManager
        binding.popCommonRv.adapter = delegateAdapter
        binding.popCommonContentBg.setOnTouchListener{ _ , _ -> true }
        binding.popCommonMask.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN ){
                dismiss()
            }
            return@setOnTouchListener true
        }
        binding.popCommonBackIv.setOnClickListener(OnClickListenerAntiViolence({
            dismiss()
        }))
        binding.popCommonComfrimTv.setOnClickListener(OnClickListenerAntiViolence({
            confirmObserver?.invoke()
            dismiss()
        }))
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:22
     * @Note   设置标题
     * @param  title 标题
     */
    fun setTitle(title:String):PopCommonView{
        binding.popCommonTitleTv.text = title
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置标题文字颜色
     * @param  colorInt 颜色
     */
    fun setTitleTextColor(colorInt :Int):PopCommonView{
        binding.popCommonTitleTv.setTextColor(colorInt)
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置标题背景颜色
     * @param  colorInt 颜色
     */
    fun setTitleBackgroudColor(colorInt :Int):PopCommonView{
        binding.popCommonTitleTv.setBackgroundColor(colorInt)
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置确认文字颜色
     * @param  colorInt 颜色
     */
    fun setConfirmTextColor(colorInt :Int):PopCommonView{
        binding.popCommonComfrimTv.setTextColor(colorInt)
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置确认按钮显示
     * @param  visable 是否显示
     */
    fun setConfirmVisable(visable :Boolean):PopCommonView{
        binding.popCommonComfrimTv.visibility = visable.judge(View.VISIBLE,View.GONE)
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置确认背景颜色
     * @param  colorInt 颜色
     */
    fun setConfirmBackgroundColor(colorInt :Int):PopCommonView{
        binding.popCommonComfrimTv.setBackgroundColor(colorInt)
        return this
    }

    /**
     * @author LDD
     * @Date   2018/4/19 下午4:04
     * @From   PopCommonView
     * @Note   设置背景颜色
     * @param  colorInt 颜色
     */
    fun setBackgroundColor(colorInt: Int):PopCommonView{
        binding.popCommonContentBg.setBackgroundColor(colorInt)
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:22
     * @Note   是否显示确认Button
     * @param  isVisable 是否显示
     */
    fun confirmButtonVisable(isVisable :Boolean):PopCommonView{
        binding.popCommonComfrimTv.visibility = isVisable.judge(View.VISIBLE,View.GONE)
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:23
     * @Note   设置确认提示
     * @param  text 提示
     */
    fun setConfrimTitle(text :String):PopCommonView{
        binding.popCommonComfrimTv.visibility = View.VISIBLE
        binding.popCommonComfrimTv.text = text
        return this
    }


    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午4:13
     * @Note   设置确定事件
     * @param  call 回调
     */
    fun setConfirmListener(call : ()->Unit):PopCommonView{
        confirmObserver  = call
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午4:13
     * @Note   设置取消事件
     * @param  call 回调
     */
    fun setDismissListener(call : () ->Unit):PopCommonView{
        dismissObserver = call
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:23
     * @Note   添加适配器
     * @param  adapters 适配器列表
     */
    fun setAdapters(vararg adapters : DelegateAdapter.Adapter<*>):PopCommonView{
        delegateAdapter.addAdapters(adapters.asList())
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:23
     * @Note   添加适配器
     * @param  adapters 适配器列表
     */
    fun setAdapters(adapters: List<DelegateAdapter.Adapter<*>>): PopCommonView {
        delegateAdapter.addAdapters(adapters)
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:24
     * @Note   设置适配器
     * @param   adapters 适配器
     */
    fun setAdapters(adapters: ArrayList<DelegateAdapter.Adapter<*>>):PopCommonView{
        delegateAdapter.addAdapters(adapters)
        return this
    }

    /**
     * @author LDD
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:25
     * @Note   显示与该View之下
     * @param  atView view
     */
    fun show(atView : View){
        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (delegateAdapter.adaptersCount == 0 || !animIsEnd){
            return
        }

        /**mask先隐藏掉*/
        binding.popCommonMask.visibility = View.GONE

        /**将该View加载到页面之上*/
        showAsDropDown(atView)

        /**执行动画 起始y点为 -View.height 减去 Recly.height -button.height  */
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity, height.toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
                binding.popCommonMask.visibility = View.VISIBLE
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
     * @From   PopCommonView
     * @Date   2018/4/19 下午3:26
     * @Note   销毁退出
     */
    override fun dismiss() {

        /**动画未执行完毕 不执行下方代码*/
        if (!animIsEnd){
            return
        }

        /**延迟执行 优化操作体验*/
        AppTool.Time.delay(250) {
            dismissObserver?.invoke()
        }

        /**隐藏Mask*/
        binding.popCommonMask.visibility = View.GONE

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity, height.toFloat(),300)),interceptor = {
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