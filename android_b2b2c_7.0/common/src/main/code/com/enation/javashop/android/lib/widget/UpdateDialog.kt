package com.enation.javashop.android.lib.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.utils.reLayout
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * @author LDD
 * @Date   2018/5/10 下午5:10
 * @From   com.enation.javashop.android.lib.widget
 * @Note   应用更新对话框
 */
class UpdateDialog(val activity: Activity): Dialog(activity, R.style.Dialog)  {

    private var listener: CommonTool.DialogInterface? = null
    private lateinit var content: TextView

    /**
     * @author LDD
     * @From   VcodeDialog
     * @Date   2018/5/10 下午5:12
     * @Note   构造
     * @param  context 上下文
     */
    constructor(ctx: Activity, listener: CommonTool.DialogInterface?): this(ctx) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = LayoutInflater.from(context).inflate(R.layout.update_dialog_lay, null)
        setContentView(contentView)
        contentView.reLayout<ViewGroup.LayoutParams> {
            params ->
            params.width = ScreenTool.getScreenWidth(context).toInt()
            params.height = ScreenTool.getScreenHeight(context).toInt()
        }
        val yesTv = contentView.findViewById<View>(R.id.update_dialog_confrim)
        val noTv = contentView.findViewById<View>(R.id.update_dialog_cancel)
        val close = contentView.findViewById<ImageView>(R.id.update_dialog_close)
        content = contentView.findViewById<TextView>(R.id.update_dialog_content)

        close.setOnClickListener {
            dismiss()
            listener?.no()
        }

        yesTv.setOnClickListener {
            content.text = "正在下载……"
            yesTv.visibility = View.INVISIBLE
            noTv.visibility = View.INVISIBLE

            listener?.yes()
        }
        noTv.setOnClickListener {
            dismiss()
            listener?.no()
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)

    }


    fun update(progress: Int){
        content.text = "测试内容"
    }


}