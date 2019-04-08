package com.enation.javashop.android.lib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.enation.javashop.utils.base.tool.ScreenTool
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

/**
 * @author  LDD
 * @Date   2018/1/10 下午5:46
 * @From   com.enation.javashop.android.lib.utils
 * @Note   App通用工具类
 */
object AppTool {

    /**
     * @author LDD
     * @Date   2018/3/15 上午11:03
     * @From   AppTool
     * @Note   动画工具类
     */
    object Anim {

        /**
         * @author LDD
         * @From   Anim
         * @Date   2018/3/15 上午11:04
         * @Note   构建popwindow进入动画
         * @param  context 上下文
         * @param  fromYDelta 开始y点
         * @param  duration 动画时间
         */
        @JvmStatic
        fun createPopInAnimation(context: Context, fromYDelta: Float, duration: Long): Animation {
            val set = AnimationSet(context, null)
            set.setFillAfter(true)
            val animation = TranslateAnimation(0f, 0f, fromYDelta, 0f)
            animation.setDuration(duration)
            set.addAnimation(animation)
            val alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation.setDuration(duration)
            set.addAnimation(alphaAnimation)
            return set
        }

        /**
         * @author LDD
         * @From   Anim
         * @Date   2018/3/15 上午11:04
         * @Note   构建popwindow进入动画
         * @param  context 上下文
         * @param  toYDelta 结束y点
         * @param  duration 动画时间
         */
        @JvmStatic
        fun createPopOutAnimation(context: Context, toYDelta: Float, duration: Long): Animation {
            val set = AnimationSet(context, null)
            set.setFillAfter(true)
            val animation = TranslateAnimation(0f, 0f, 0f, toYDelta)
            animation.duration = duration
            set.addAnimation(animation)
            val alphaAnimation = AlphaAnimation(1f, 0f)
            alphaAnimation.duration = duration
            set.addAnimation(alphaAnimation)
            return set
        }
    }

    /**
     * @author  LDD
     * @Date   2018/1/10 下午5:46
     * @From   com.enation.javashop.android.lib.utils.AppTool
     * @Note   网络工具类
     */
    object Net {

        /**
         * @author LDD
         * @From   Net
         * @Date   2018/1/16 下午1:07
         * @Note   验证网速 并toast
         * @param  bits 网速
         */
        @JvmStatic
        fun verifyNetSpeed(bits: Double) {
            if (bits < 100) {
                showMessage("当前网络质量差")
            }
        }

        /**
         * @author LDD
         * @From   Net
         * @Date   2018/1/16 下午1:07
         * @Note   验证网速 网络不好时执行回调  由使用者决定是否显示Toast信息
         * @param  bits 网速
         */
        @JvmStatic
        fun verifyNetSpeed(bits: Double, callBack: (() -> (Unit)) -> (Unit)) {
            val toastCallBack = {
                showMessage("当前网络质量差")
            }
            if (bits < 100) {
                callBack.invoke(toastCallBack)
            }
        }
    }

    /**
     * @author  LDD
     * @Date   2018/1/10 下午5:46
     * @From   com.enation.javashop.android.lib.utils.AppTool
     * @Note   RxJava工具类
     */
    object Rx {

    }

    /**
     * @author LDD
     * @Date   2018/5/18 上午11:27
     * @From   com.enation.javashop.android.lib.utils.AppTool
     * @Note   文件工具类
     */
    object File {

        /**
         * @author LDD
         * @From   File
         * @Date   2018/5/18 上午11:33
         * @Note   读取Assets文件夹下的配置信息
         * @param  context  上下文
         * @param  fileName 文件名
         */
        @JvmStatic
        fun readAssetsText(context: Context, fileName: String): String {
            try {
                val ins = context.assets.open(fileName)
                val size = ins.available()
                var buffer = ByteArray(size)
                ins.read(buffer)
                ins.close()
                var text = String(buffer, Charset.forName("utf-8"))
                return text
            } catch (e :IOException) {
                return "读取错误，请检查文件名"
            }
        }

    }

    /**
     * @author LDD
     * @Date   2018/3/15 下午3:07
     * @From   AppTool
     * @Note   关于时间的操作
     */
    object Time {
        private val handler by lazy { Handler() }

        /**
         * @author LDD
         * @From   com.enation.javashop.android.lib.utils.AppTool.Rx
         * @Date   2018/1/10 下午5:41
         * @Note   Observable扩展方法 延时执行
         * @param  milliSeconds 延时毫秒数
         * @param  handlerProcessor     处理回调
         */
        @JvmStatic
        fun delay(milliSeconds: Long, handlerProcessor: () -> (Unit)) {
            handler.postDelayed(handlerProcessor, milliSeconds)
        }
    }

    object Calculation{

        /**
         * @author LDD
         * @From   Calculation
         * @Date   2018/5/21 下午3:21
         * @Note   获取百分比
         * @param  total 总数
         * @param  current 当前
         */
        @JvmStatic
        fun getPercentage(total: Int, current: Int): Int {
            val numberFormat = NumberFormat.getInstance()
            // 设置精确到小数点后2位
            numberFormat.maximumFractionDigits = 0
            val result = numberFormat.format(current.toFloat() / total.toFloat() * 100)
            return Integer.valueOf(result)!!
        }

    }


    /**
     * @author LDD
     * @Date   2018/1/11 上午10:31
     * @From   com.enation.javashop.android.lib.utils.AppTool
     * @Note   关于设置
     */
    object Setting {

        /**
         * @author  LDD
         * @From    com.enation.javashop.android.lib.utils.AppTool.Setting
         * @Date    2018/1/11 上午10:31
         * @Note    跳转网络设置
         * @param   activity 调用页面
         */
        @JvmStatic
        fun systemNetSetting(activity: AppCompatActivity) {
            activity.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }

        /**
         * @author  LDD
         * @From    com.enation.javashop.android.lib.utils.AppTool.Setting
         * @Date    2018/1/11 上午10:31
         * @Note    跳转WIFI设置
         * @param   activity 调用页面
         */
        @JvmStatic
        fun systemWifiSetting(activity: AppCompatActivity) {
            activity.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        /**
         * @author  LDD
         * @From    com.enation.javashop.android.lib.utils.AppTool.Setting
         * @Date    2018/1/11 上午10:31
         * @Note    跳转Mobile设置
         * @param   activity 调用页面
         */
        @JvmStatic
        fun systemMobileSetting(activity: AppCompatActivity) {
            activity.startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
        }
    }

    /**
     * @author LDD
     * @Date   2018/4/2 下午3:12
     * @From   AppTool
     * @Note   图片工具类
     */
    object Image {

        /**
         * @author LDD
         * @From   Image
         * @Date   2018/4/2 下午3:23
         * @Note   url获取Bitmap
         * @param  url 图片URL
         */
        @JvmStatic
        fun urlToBitmap(url: String): Observable<Bitmap> {
            return Observable.create { subscriber ->
                var fileUrl: URL? = null
                val bitmap: Bitmap

                try {
                    fileUrl = URL(url)
                } catch (e: MalformedURLException) {
                    subscriber.onError(e)
                }


                try {
                    val conn = fileUrl!!.openConnection() as HttpURLConnection
                    conn.doInput = true
                    conn.connect()
                    val inStream = conn.inputStream
                    bitmap = BitmapFactory.decodeStream(inStream)
                    inStream.close()
                    subscriber.onNext(bitmap)
                } catch (e: IOException) {
                    subscriber.onError(e)
                }
            }
        }
    }

    /**
     * @author LDD
     * @Date   2018/4/2 下午3:12
     * @From   AppTool
     * @Note   系统UI工具类
     */
    object SystemUI {

        private var statusBarHeight : Int = 0

        @JvmStatic fun getStatusBarHeight() : Int{
            return statusBarHeight
        }

        @JvmStatic fun initStatusBarHeight(context: Context){
            val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId)
            }
        }


        /**
         * @author  LDD
         * @From   SystemUI
         * @Date   2018/1/12 下午4:09
         * @Note   设置状态栏透明  4.4.2及以下 不可设置导航栏变色
         * @param  activity 需要状态栏透明的页面
         * @param  bottomColor  导航栏变色
         */
        @JvmStatic
        fun ImmersiveWithBottomBarColor(activity: Activity, bottomColor: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                    var window = activity.window
                    var decorView = window.decorView
                    //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                    var option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    decorView.systemUiVisibility = option
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                    //导航栏颜色也可以正常设置
                    window.navigationBarColor = bottomColor
                } else {
                    var window = activity.window
                    var attributes = window.attributes
                    var flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    attributes.flags = flagTranslucentStatus
                    window.attributes = attributes
                }
            }
        }

        /**
         * @author LDD
         * @From   SystemUI
         * @Date   2018/1/15 下午2:07
         * @Note   设置当页面有drawlayout时，让drawlayout也实现变色
         * @param  activity  需要设置的也米娜
         * @param  rid       当页内容视图
         */
        @JvmStatic
        fun KitkatNavigationViewImmersive(activity: Activity, rid: Int) {
            //要在内容布局增加状态栏，否则会盖在侧滑菜单上
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content) as ViewGroup
            //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
            val parentView = rootView.getChildAt(0)
            val linearLayout = LinearLayout(activity)
            linearLayout.orientation = LinearLayout.VERTICAL
            //侧滑菜单
            val drawer = parentView as DrawerLayout
            //内容视图
            val content = activity.findViewById<View>(rid)
            //将内容视图从 DrawerLayout 中移除
            drawer.removeView(content)
            //添加内容视图
            linearLayout.addView(content, content.layoutParams)
            //将带有占位状态栏的新的内容视图设置给 DrawerLayout
            drawer.addView(linearLayout, 0)

            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.fitsSystemWindows = true
                //布局预留状态栏高度的 padding
                if (parentView is DrawerLayout) {
                    //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                    parentView.clipToPadding = false
                }
            }
        }


        /**
         * @author LDD
         * @From   SystemUI
         * @Date   2018/1/29 下午8:28
         * @Note   显示或隐藏导航栏
         * @param  activity 需要操作的activity
         * @param  isShow   显示还是隐藏
         */
        @JvmStatic
        fun showNavigationBar(activity: Activity, isShow: Boolean) {
            if (isShow) {
                activity.window.decorView.setOnSystemUiVisibilityChangeListener(null)
                activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            } else {
                activity.window.decorView.setOnSystemUiVisibilityChangeListener {
                    activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
                }
                activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        }

        /**
         * @author LDD
         * @From   SystemUI
         * @Date   2018/4/17 下午1:44
         * @Note   dp转px
         * @param  dp dp大小
         */
        @JvmStatic
        fun dpToPx(dp: Float): Int {
            return ScreenTool.dip2px(BaseApplication.appContext, dp)
        }

        @JvmStatic
        fun getNavigationBarHeight(activity: Activity): Int {
            val resources = activity.getResources()
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val height = resources.getDimensionPixelSize(resourceId)
            return height
        }

        /**
         * @author LDD
         * @From   SystemUI
         * @Date   2018/5/15 下午4:47
         * @Note   关闭软键盘
         * @param  context 页面
         */
        @JvmStatic
        fun hideKeyBoard(context: Activity) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive && context.currentFocus != null) {
                if (context.currentFocus.windowToken != null) {
                    imm.hideSoftInputFromWindow(context.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }
    }
}