package com.enation.javashop.android.component.extra.activity

import android.app.PendingIntent.getActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Message
import android.util.Log
import com.enation.javashop.android.component.extra.R
import com.enation.javashop.android.component.extra.databinding.ScanActLayBinding
import com.enation.javashop.android.component.extra.launch.ExtraLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.base.GalleryActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.logic.contract.extra.ScanContract
import com.enation.javashop.android.middleware.logic.presenter.extra.ScanPresenter
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.photoutils.model.TResult
import com.enation.javashop.photoutils.uitl.RxGetPhotoUtils
import com.uuzuche.lib_zxing.activity.CaptureFragment
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.camera.CameraManager
import kotlinx.android.synthetic.main.scan_act_lay.*

/**
 * 扫一扫
 */
@Router(path = "/extra/scan")
class ScanActivity :GalleryActivity<ScanPresenter,ScanActLayBinding>(),ScanContract.View,CodeUtils.AnalyzeCallback {

    private lateinit var captureFragment : CaptureFragment

    private var lightOpen = false

    override fun getLayId(): Int {
        return R.layout.scan_act_lay
    }

    override fun bindDagger() {
        ExtraLaunch.component.inject(this)
    }

    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        captureFragment = CaptureFragment()
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera)
        captureFragment.analyzeCallback = this
        supportFragmentManager.beginTransaction().replace(R.id.scan_frame, captureFragment).commit()
        scan_topbar.setTopHolderVisibility(false)
                .setLineBgColor(Color.TRANSPARENT)
    }

    override fun bindEvent() {
        scan_topbar.setLeftClickListener {
            pop()
        }
        light.setOnClickListener {
            if(lightOpen){
                light.setImageResource(R.drawable.javashop_icon_light_close)
            }else{
                light.setImageResource(R.drawable.javashop_icon_light_open)
            }
            CodeUtils.isLightEnable(!lightOpen)
            lightOpen = !lightOpen
        }
        album.setOnClickListener {
            RxGetPhotoUtils.init(this).configCompress(true,true,true,102400,800,800).getPhotoFromGallery(false)
        }
    }

    override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
        if (result.contains("goods/") && result.contains("su=")){
            push("/goods/detail",{
                it.withInt("goodsId", RegularHelper().getMiddleString(result,"goods/","\\?su=")[0].toInt())
            })
        }
        val obtain = Message.obtain()
        obtain.what = R.id.restart_preview
        captureFragment.handler.sendMessageDelayed(obtain, 500)
    }

    override fun onAnalyzeFailed() {

    }

    override fun takeSuccess(result: TResult) {
        val filePath = result.image.compressPath
        CodeUtils.analyzeBitmap(filePath,this)
    }

    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun start() {
        showDialog()
    }

    override fun networkMonitor(state: NetState) {

    }

    override fun destory() {

    }
}