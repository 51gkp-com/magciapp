package com.enation.javashop.android.component.shop.weiget

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.drawable.BitmapDrawable
import android.view.MotionEvent
import android.view.View
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.databinding.ShopSearchPopLayBinding
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.animSequentialStart
import com.enation.javashop.android.lib.widget.PopWindowCompatible
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/11 上午9:26
 * @From   com.enation.javashop.android.component.shop.weiget
 * @Note   店铺检索视图
 */

class ShopSearchView :PopWindowCompatible {

    /**
     * @author LDD
     * @Date   2018/4/24 下午3:29
     * @From   ShopSearchView
     * @Note   店铺搜索ViewModel
     */
    data class SearchVM(val text :ObservableField<String>)

    companion object {

        /**
         * @author LDD
         * @From   ShopSearchView
         * @Date   2018/4/24 下午3:29
         * @Note   快速构建
         * @param  activity 调用Act
         * @param  height   高度
         */
        fun build(activity: Activity, height : Int):ShopSearchView{
            return ShopSearchView(activity,height)
        }

    }

    /**
     * @Name  showObserver
     * @Type  (()->(Unit))?
     * @Note  显示监听
     */
    private var showObserver : (()->(Unit))? = null

    /**
     * @Name  dismissObserver
     * @Type  (()->(Unit))?
     * @Note  取消监听
     */
    private var dismissObserver : (()->(Unit))? = null

    /**
     * @Name  searchObserver
     * @Type  (()->(Unit))?
     * @Note  搜索监听
     */
    private var searchObserver :((String) ->Unit)? = null

    /**
     * @Name  animIsEnd
     * @Type  Boolean
     * @Note  动画是否结束
     */
    private var animIsEnd = true

    /**
     * @Name  databinding
     * @Type  ShopSearchPopLayBinding
     * @Note  视图绑定
     */
    val databinding by lazy {
        DataBindingUtil.bind<ShopSearchPopLayBinding>(activity.layoutInflater.inflate(R.layout.shop_search_pop_lay,null))
    }

    /**
     * @Name  textObser
     * @Type  SearchVM
     * @Note  搜索VM
     */
    private val textObser = SearchVM(ObservableField(""))

    /**
     * @Name  activity
     * @Type  Activity
     * @Note  调用Activity
     */
    private val activity :Activity

    /**构造方法*/
    private constructor(activity: Activity, height : Int): super(ScreenTool.getScreenWidth(activity).toInt(), height){
        this.activity = activity
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        databinding.data = textObser
        contentView = databinding.root
        bindEvent()
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:32
     * @Note   绑定事件
     */
    fun bindEvent(){

        databinding.shopSearchMask.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                dismiss()
            }
            return@setOnTouchListener true
        }

        databinding.shopSearchCancel.setOnClickListener {
            dismiss()
        }

        databinding.shopSearchHint.setOnClickListener {
            if (textObser.text.get().isNotEmpty()){
                searchObserver?.invoke(textObser.text.get())
            }
            dismiss()
        }

        databinding.shopSearchClearTextIv.setOnClickListener {
            textObser.text.set("")
        }
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:32
     * @Note   重置数据
     */
    private fun resetData(){
        textObser.text.set("")
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:33
     * @Note   设置显示监听
     * @param  call 回调
     */
    fun setShowObserver(call :() -> Unit):ShopSearchView{
        showObserver = call
        return this
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:33
     * @Note   设置关闭监听
     * @param  call 回调
     */
    fun setDismissObserver(call :() -> Unit):ShopSearchView{
        dismissObserver = call
        return this
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:34
     * @Note   设置搜索监听
     * @param  call 回调
     */
    fun setSearchObserver(call :(String) -> Unit):ShopSearchView{
        searchObserver = call
        return this
    }

    /**
     * @author LDD
     * @From   ShopSearchView
     * @Date   2018/4/24 下午3:34
     * @Note   显示
     * @param  atView 显示在该View下方
     */
    fun show(atView : View){
        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (!animIsEnd){
            return
        }

        /**mask先隐藏掉*/
        databinding.shopSearchMask.visibility = View.GONE

        /**将该View加载到页面之上*/
        showAsDropDown(atView)

        /**执行动画 起始y点为0*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity, -(height*0.13).toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
                showObserver?.invoke()
                databinding.shopSearchMask.visibility = View.VISIBLE
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
     * @From   ShopSearchView
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
        databinding.shopSearchMask.visibility = View.GONE

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity,-(height*0.13).toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画执行完毕 设置标记 隐藏View*/
            if (state == 2){
                animIsEnd = true
                resetData()
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