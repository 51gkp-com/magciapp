package com.enation.javashop.android.component.setting.activity

import android.graphics.Color
import android.os.Bundle
import com.enation.javashop.android.component.setting.R
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.EcodeHelper
import com.enation.javashop.android.lib.utils.joinManager
import com.enation.javashop.android.middleware.bind.DataBindingHelper
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.android.synthetic.main.about_act_lay.*
/**
 * @author LDD
 * @Date   2018/3/9 上午9:46
 * @From   com.enation.javashop.android.component.setting.activity
 * @Note   关于页面
 */
@Router(path = "/setting/about")
class AboutActivity : BaseToolActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_act_lay)
        /**沉浸式*/
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        DataBindingHelper.topbarAutoHeight(about_topbar,true)
        /**设置返回按钮*/
        about_topbar.setLeftClickListener {
            toolBack()
        }
        Thread{
            val code = EcodeHelper().createQRImage("https://51gkp-static.oss-cn-beijing.aliyuncs.com/package/app-releaseV1.4.apk",200,null)
            runOnUiThread {
                about_ercode_iv.setImageBitmap(code)
            }
        }.start()
    }
}