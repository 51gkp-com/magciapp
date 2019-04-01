package com.enation.javashop.android.lib.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/5/10 下午5:10
 * @From   com.enation.javashop.android.lib.widget
 * @Note   验证码对话框
 */
class VcodeDialog  {

    /**
     * @Name  context
     * @Type  Context
     * @Note  上下文
     */
    private val context :Context

    /**
     * @author LDD
     * @Date   2018/5/10 下午5:13
     * @From   VcodeDialog
     * @Note   伴生对象
     */
    companion object {

        /**
         * @author LDD
         * @From   VcodeDialog
         * @Date   2018/5/10 下午5:13
         * @Note   静态构建
         * @param  context 上下文
         */
        fun build(context: Context):VcodeDialog{
            return VcodeDialog(context)
        }

    }
    
    /**
     * @author LDD
     * @From   VcodeDialog
     * @Date   2018/5/10 下午5:12
     * @Note   构造
     * @param  context 上下文
     */
    private constructor(context: Context) {
        this.context = context
    }

    /**
     * @author LDD
     * @From   VcodeDialog
     * @Date   2018/5/10 下午5:12
     * @Note   构建
     * @param  call 验证码回调
     * @param  imageLoader 加载验证码回调
     */
    fun config(call :(String) ->Unit,imageLoader :(iv : ImageView) ->Unit ,cancleCall :(()->Unit)? = null) :Dialog{
        val contentView = LayoutInflater.from(context).inflate(R.layout.vcode_dialog_lay, null)
        val dialog = Dialog(context, R.style.Dialog)
        dialog.setContentView(contentView)
        contentView.reLayout<ViewGroup.LayoutParams> {
            params ->
            params.width = ScreenTool.getScreenWidth(context).toInt()
            params.height = ScreenTool.getScreenHeight(context).toInt()
        }
        val yesTv = contentView.findViewById<View>(R.id.vcode_dialog_confrim)
        val noTv = contentView.findViewById<View>(R.id.vcode_dialog_cancel)
        val image = contentView.findViewById<ImageView>(R.id.vcode_dialog_iv)
        val et = contentView.findViewById<EditText>(R.id.vcode_dialog_et)

        imageLoader.invoke(image)

        image.setOnClickListener {
            imageLoader.invoke(image)
        }

        yesTv.setOnClickListener {
            val text = et.text.toString().trim()
            if (text == "") {
                showMessage("请输入验证码！")
                return@setOnClickListener
            }
            dialog.dismiss()
            call.invoke(text)
        }
        noTv.setOnClickListener {
            dialog.dismiss()
            cancleCall?.invoke()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog
    }



}