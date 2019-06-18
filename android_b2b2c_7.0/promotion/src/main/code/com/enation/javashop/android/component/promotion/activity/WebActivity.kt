package com.enation.javashop.android.component.promotion.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.WebLayBinding
import com.enation.javashop.android.component.promotion.launch.PromotionLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.logic.contract.promotion.WebContract
import com.enation.javashop.android.middleware.logic.presenter.promotion.WebPresenter
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.web_lay.*

@Router(path = "/common/web")
class WebActivity : BaseActivity<WebPresenter, WebLayBinding>(), WebContract.View  {

    @Autowired(name = "title",required = false)
    @JvmField var title :String = "网页"

    @Autowired(name = "url",required = false)
    @JvmField var url :String = ""

    override fun getLayId(): Int {
        return R.layout.web_lay
    }

    override fun bindDagger() {
        PromotionLaunch.component.inject(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        web_topbar.setTitleText(title).setLeftClickListener { pop() }
        webview.settings.javaScriptEnabled = true

        webview.webViewClient = object : WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.url.toString())
                } else {
                    view.loadUrl(request.toString())
                }
                return true
            }
        }



        webview.loadUrl(url)
    }

    override fun bindEvent() {
        web_topbar.setLeftClickListener {
            pop()
        }
    }

    override fun onError(message: String, type: Int) {

    }

    override fun complete(message: String, type: Int) {

    }

    override fun start() {

    }

    override fun networkMonitor(state: NetState) {

    }

    override fun destory() {

    }
}