package com.enation.javashop.android.component.member.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberInfoEditFragLayBinding
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.lib.utils.weak
import com.enation.javashop.utils.base.tool.ScreenTool
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/5/15 下午2:06
 * @From   com.enation.javashop.android.component.member.fragment
 * @Note   会员信息修改页面
 */
class MemberInfoEditFragment :Fragment {

    /**
     * @Name  layoutId
     * @Type  Int
     * @Note  布局ID
     */
    private  var layoutId = -1

    /**
     * @Name  contentText
     * @Type  String
     * @Note  内容文字
     */
    private var contentText  = ""


    /**
     * @Name  callBack
     * @Type  Block
     * @Note  回调
     */
    private var callBack :((String) ->Unit)? = null

    /**
     * @Name  parentActivity
     * @Type  AppCompatActivity
     * @Note  父页面
     */
    private lateinit var parentActivity:WeakReference<AppCompatActivity>
    
    /**
     * @Name  mViewDataBinding
     * @Type  MemberInfoEditFragLayBinding
     * @Note  VDB
     */
    private var mViewDataBinding: MemberInfoEditFragLayBinding? = null

    constructor() : super()

    /**伴生对象*/
    companion object {

        /**
         * @author LDD
         * @From   MemberInfoEditFragment
         * @Date   2018/5/15 下午2:52
         * @Note   构建
         */
        fun build(activity: AppCompatActivity, @IdRes layoutId :Int): MemberInfoEditFragment {
            return MemberInfoEditFragment().then {
                self ->
                self.parentActivity = activity.weak()
                self.layoutId = layoutId
            }
        }
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午2:21
     * @Note   视图创建
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**初始化根视图及DataBinding*/
        if (mViewDataBinding == null) {
            mViewDataBinding = DataBindingUtil.bind(inflater.inflate(R.layout.member_info_edit_frag_lay, null))
            mViewDataBinding?.root?.layoutParams = ViewGroup.LayoutParams(ScreenTool.getScreenWidth(activity).toInt(),ScreenTool.getScreenHeight(activity).toInt())
            mViewDataBinding?.root?.setOnTouchListener { _ , _ -> true }
        }
        bindEvent()
        return mViewDataBinding?.root
    }


    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午3:53
     * @Note   绑定
     */
    private fun bindEvent(){
        mViewDataBinding?.memberInfoEditFragTopbar?.setLeftClickListener {
            pop()
        }?.setRightClickListener {
            var text = mViewDataBinding?.memberInfoEditFragEt?.text.toString().trim()
            if (text.isNotEmpty()){
                callBack?.invoke(text)
                pop()
            }else{
                showMessage("请完善信息")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        renderUI()
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午2:22
     * @Note   渲染UI
     */
    private fun renderUI(){
        mViewDataBinding?.memberInfoEditFragEt?.setText(contentText)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午2:22
     * @Note   绑定事件
     */
    fun setEvent(confrimCall : (String)->Unit):MemberInfoEditFragment{
        callBack = confrimCall
        return this
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午3:20
     * @Note   设置内容
     * @param  contentText 内容
     */
    fun setContentText(contentText :String):MemberInfoEditFragment{
        this.contentText = contentText
        return this
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午2:49
     * @Note   显示
     * @param  layoutId 父布局ID
     */
    fun show(){
        parentActivity.get()?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)?.add(layoutId, this)?.addToBackStack(null)?.commit()
    }

    /**
     * @author LDD
     * @From   MemberInfoEditFragment
     * @Date   2018/5/15 下午2:49
     * @Note   销毁
     */
    private fun pop(){
        val act = parentActivity.get()
        if (act!=null){
            val pop = {
                act.supportFragmentManager?.popBackStack()
            }
            val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(imm.isActive &&act.currentFocus !=null){
                if (act.currentFocus.windowToken !=null) {
                    imm.hideSoftInputFromWindow(act.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    AppTool.Time.delay(500) {
                        pop.invoke()
                    }
                }else{
                    pop.invoke()
                }
            }else{
                pop.invoke()
            }
        }
    }


}