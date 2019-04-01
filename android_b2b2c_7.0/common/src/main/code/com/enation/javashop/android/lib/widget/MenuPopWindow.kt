package com.enation.javashop.android.lib.widget

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.adapter.ListViewBaseAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.databinding.MenuItemLayBinding
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.animSequentialStart
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.lib.vo.MenuVo
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/4/12 下午1:57
 * @From   com.enation.javashop.android.lib.widget
 * @Note   点击菜单视图
 */
class MenuPopWindow constructor(val activity: Activity,val data :ArrayList<MenuVo>) :PopWindowCompatible((ScreenTool.getScreenWidth(BaseApplication.appContext)/3).toInt(), (if(data.size>5) 5 else data.size) * (ScreenTool.getScreenWidth(BaseApplication.appContext)/3/3.5).toInt()){



    /**
     * @Name  animIsEnd
     * @Type  Boolean
     * @Note  动画是否结束
     */
    private var animIsEnd = true

    /**
     * @Name  clickCallBack
     * @Type  block
     * @Note  点击回调
     */
    private var clickCallBack :((MenuVo) ->Unit)? = null

    /**类初始化操作*/
    init {
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        createView()
    }

    companion object {
        /**
         * @author LDD
         * @From   MenuPopWindow
         * @Date   2018/4/13 上午9:43
         * @Note   静态构建
         * @param  activity 调用上下文
         * @param  data     数据
         */
        fun build(activity: Activity,data :ArrayList<MenuVo>):MenuPopWindow{
            return MenuPopWindow(activity,data)
        }
    }

    /**
     * @author LDD
     * @From   MenuPopWindow
     * @Date   2018/4/13 上午9:28
     * @Note   创建视图
     */
    fun createView(){

        /**声明父视图*/
        var parent = RelativeLayout(activity)
        /**设置父视图为PopWindow的内容视图*/
        contentView = parent
        /**初始化背景视图*/
        val backView = DialogBackGroundView(activity)
        /**添加进父视图*/
        parent.addView(backView)
        /**设置为父视图的大小*/
        backView.reLayout<RelativeLayout.LayoutParams> {
            params ->
            params.height = height
            params.width = width
        }
        /**初始化ListView*/
        val listView = ListView(activity)
        /**隐藏滑动条*/
        listView.isVerticalScrollBarEnabled = false
        /**添加父视图*/
        parent.addView(listView)
        /**设置列表视图大小*/
        listView.reLayout<RelativeLayout.LayoutParams> {
            params ->
            params.height = (height*0.95).toInt()
            params.width = width
            params.setMargins(0,(height*0.05).toInt(),0,(height*0.05).toInt())
        }
        /**设置适配器*/
        listView.adapter = object : ListViewBaseAdapter<MenuVo,MenuItemLayBinding>(activity, R.layout.menu_item_lay,data){

            override fun fillItem(binding: MenuItemLayBinding, data: MenuVo, position: Int) {
                binding.menuItemIv.setImageResource(data.resId)
                binding.data = data
            }

            override fun itemClick(data: MenuVo, position: Int) {
                dismiss({
                    clickCallBack?.invoke(data)
                })
            }
        }
    }

    /**
     * @author LDD
     * @From   MenuPopWindow
     * @Date   2018/4/13 上午9:41
     * @Note   设置点击事件
     * @param  call 点击回调
     */
    fun setItemCallBack(call : (MenuVo) -> Unit):MenuPopWindow{
        this.clickCallBack = call
        return this
    }

    /**
     * @author LDD
     * @From   MenuPopWindow
     * @Date   2018/3/15 下午3:29
     * @Note   显示
     * @param  atView 显示在那个view下方
     */
    fun show(atView:View){

        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (data.size == 0 || !animIsEnd){
            return
        }


        /**将该View加载到页面之上*/

        var baseLine = ScreenTool.getScreenWidth(BaseApplication.appContext) - width
        var offset = -atView.width/3
        var location = IntArray(2)
        atView.getLocationOnScreen(location)
        if (location[0] > baseLine){
            offset -= (location[0] - baseLine).toInt()
        }

        showAsDropDown(atView,offset,-atView.height/3)

        /**执行动画 起始y点为 -View.height 减去 Recly.height -button.height  */
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity, (-height).toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
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
     * @From   MenuPopWindow
     * @Date   2018/3/15 下午3:36
     * @Note   重写dismiss事件
     */
    fun dismiss(complete :(()->Unit)? = null) {

        /**动画未执行完毕 不执行下方代码*/
        if (!animIsEnd){
            return
        }

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity,-height.toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画执行完毕 设置标记 隐藏View*/
            if (state == 2){
                animIsEnd = true
                complete?.invoke()
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