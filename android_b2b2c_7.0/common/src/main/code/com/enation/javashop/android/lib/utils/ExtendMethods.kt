package com.enation.javashop.android.lib.utils

/**
 * @author  LDD
 * @Data   2017/12/22 下午4:45
 * @From   com.enation.javashop.android.lib.utils
 * @Note   扩展方法Koltin文件
 */

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.jrouter.logic.datainfo.Postcard
import com.enation.javashop.android.lib.R
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.android.lib.base.DisposableManager
import com.enation.javashop.net.engine.plugin.rxbus.RxBus
import com.enation.javashop.utils.base.tool.CommonTool
import com.enation.javashop.utils.base.tool.ScreenTool
import com.enation.javashop.utils.logger.LoggerFactory
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

/**
 * 绑定字段 指定泛型
 */
typealias ObserableString = ObservableField<String>


/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:47
 * @Note   debug日志打印
 * @param  tag 标记
 * @param  message 日志信息
 */
fun debugLog(tag: String?, message: String?) {
    val st = Throwable().stackTrace[1]
    LoggerFactory.getLogger().i("【Tag -> $tag】 >>>>>>>>>>", "Message -> $message >>>>>>>>>>>>>>>>>>>>>>>>>> 【Location ->Class:${st.className} - Method:${st.methodName} - Line:${st.lineNumber}】 ")
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:47
 * @Note   error日志打印
 * @param  tag 标记
 * @param  message 日志信息
 */
fun errorLog(tag: String, message: String) {
    val st = Throwable().stackTrace[1]
    LoggerFactory.getLogger().e("【Tag -> $tag】 >>>>>>>>>>", "Message -> $message >>>>>>>>>>>>>>>>>>>>>>>>>> 【Location ->Class:${st.className} - Method:${st.methodName} - Line:${st.lineNumber}】 ")
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:47
 * @Note   显示Toast信息
 * @param  message 需要显示的信息
 */
fun showMessage(message: String) {
    if (message == ""){
        return
    }
    val messageCallback = {
        Toast.makeText(BaseApplication.appContext, message, Toast.LENGTH_SHORT).show()
    }
    try {
        messageCallback.invoke()
    } catch (runtime: RuntimeException) {
        Looper.prepare()
        messageCallback.invoke()
    }
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:48
 * @Note   数据类型转换
 * @return 转换后的值
 */
fun <T> Any.to(): T {
    return this as T
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:49
 * @Note   通过JRouter框架检索对象，并实例化
 * @param  path 对象注册到JRouter的路径
 */
fun <T> acquireInstance(path: String): T {
    return JRouter.prepare().create(path).seek().to()
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:51
 * @Note   跳转页面Activity扩展方法
 * @param  path Activity注册到JRouter中的路径
 * @param  _block 更多操作
 * @param  requstCode 返回码
 */
fun AppCompatActivity.push(path: String, _block: ((Postcard) -> Unit)? = null, requstCode: Int = -1 , isNeedLogin :Boolean = false) {
    if (isNeedLogin){
        JRouter.prepare().create("/member/login/main").withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
            _block?.invoke(postcard)
        }.seek(this)
        return
    }
    val engine = JRouter.prepare().create(path).withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
        _block?.invoke(postcard)
    }
    if (requstCode == -1) {
        engine.seek(this)
    } else {
        engine.seek(this, requstCode)
    }
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:51
 * @Note   跳转页面Activity扩展方法
 * @param  path Activity注册到JRouter中的路径
 * @param  _block 更多操作
 * @param  requstCode 返回码
 */
fun Activity.push(path: String, _block: ((Postcard) -> Unit)? = null, requstCode: Int = -1 , isNeedLogin :Boolean = false) {
    if (isNeedLogin){
        JRouter.prepare().create("/member/login/main").withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
            _block?.invoke(postcard)
        }.seek(this)
        return
    }
    val engine = JRouter.prepare().create(path).withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
        _block?.invoke(postcard)
    }
    if (requstCode == -1) {
        engine.seek(this)
    } else {
        engine.seek(this, requstCode)
    }
}


/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:51
 * @Note   跳转页面 V4 Fragment扩展方法
 * @param  path Activity注册到JRouter中的路径
 * @param  _block 更多操作
 */
fun android.support.v4.app.Fragment.push(path: String, _block: ((Postcard) -> Unit)? = null , isNeedLogin :Boolean = false) {
    if (isNeedLogin){
        JRouter.prepare().create("/member/login/main").withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
            _block?.invoke(postcard)
        }.seek(activity)
        return
    }
    JRouter.prepare().create(path).withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
        _block?.invoke(postcard)
    }.seek(activity)
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:51
 * @Note   跳转页面 V7 Fragment扩展方法
 * @param  _block 更多操作
 * @param  path Activity注册到JRouter中的路径
 */
fun Fragment.push(path: String, _block: ((Postcard) -> Unit)? = null , isNeedLogin :Boolean = false) {
    if (isNeedLogin){
        JRouter.prepare().create("/member/login/main").withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
            _block?.invoke(postcard)
        }.seek(activity)
        return
    }
    JRouter.prepare().create(path).withTransition(R.anim.push_left_in, R.anim.push_left_out).then { postcard ->
        _block?.invoke(postcard)
    }.seek(activity)
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:53
 * @Note   Activity 退出扩展方法
 */
fun AppCompatActivity.pop() {
    finish()
    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午4:53
 * @Note   用于代码规范化 有助于代码美观 一般用于配置对象时对代码格式化
 *         示例: val linearLayout = LinearLayout(context).then {
 *                  lay ->
 *                  lay.orientation = LinearLayout.HORIZONTAL
 *                  lay.setBackgroundColor(Color.RED)
 *              }.removeAllViews()
 * @param  _block 回调
 */
inline fun <T : Any> T.then(_block: (T) -> Unit): T {
    _block.invoke(this)
    return this
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:04
 * @Note   用于代码格式化 与then不同的是 该方法不继续传递self 一般用于格式化对象初始化代码
 *         示例: val view = View(context).more {
 *                  self ->
 *                  self.setBackgroundColor(Color.BLUE)
 *                  self.setFadingEdgeLength(1)
 *                  self.setOnClickListener{}
 *              }
 * @param  _block 回调
 */
inline fun <T : Any> T.more(_block: (T) -> Unit) {
    _block.invoke(this)
}


/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:10
 * @Note   获取事件序列中心
 * @return RxBus事件中心
 */
fun getEventCenter(): RxBus {
    return RxBus.getDefault()
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:13
 * @Note   当变量为空时执行
 * @param  _block 回调
 */
inline fun Any?.haventDo(_block: () -> Unit) {
    if (this == null) {
        _block.invoke()
        return
    } else if (this is String) {
        if (this == "") {
            _block.invoke()
        }
    }
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:15
 * @Note   当一个序列 都为空时 执行该回调
 * @param  array 需要判断空的序列
 * @param  _block 回调
 */
inline fun haventDo(vararg array: Any?, _block: () -> Unit) {
    var has = false
    for (any in array) {
        if (any != null) {
            has = true
            if (any is String) {
                if (any == "") {
                    has = false
                }
            }
        } else {
            has = false
        }
    }
    if (!has) {
        _block.invoke()
    }
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:13
 * @Note   当变量不为空时执行
 * @param  _block 回调
 */
inline fun Any?.haveDo(_block: () -> Unit) {
    if (this != null) {
        if (this is String) {
            if (this == "") {
                return
            }
        }
        _block.invoke()
    }
}

/**
 * @author  LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Data   2017/12/22 下午5:15
 * @Note   当一个序列 都不为空时 执行该回调
 * @param  array 需要判断空的序列
 * @param  _block 回调
 */
inline fun haveDo(vararg array: Any?, _block: () -> Unit) {
    var has = true
    for (any in array) {
        if (any == null) {
            has = false
        } else if (any is String) {
            if (any == "") {
                has = false
            }
        }
    }
    if (has) {
        _block.invoke()
    }
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/1/19 下午3:46
 * @Note   通过context获取颜色
 * @param  rid  颜色索引ID
 */
fun Context.getColorCompatible(rid: Int): Int {
    return ContextCompat.getColor(this, rid)
}

/**
 * @author LDD
 * @Date   2018/1/19 下午3:51
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Note   非入侵式，setOnScrollListener兼容低版本实现
 * @param  call 滑动回调
 */
inline fun View.setOnScrollObserver(crossinline call: (scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) -> (Unit)) {
    var oldScrollX = 0
    var oldScrollY = 0
    this.viewTreeObserver.addOnScrollChangedListener {
        call.invoke(scrollX, scrollY, oldScrollX, oldScrollY)
        oldScrollX = scrollX
        oldScrollY = scrollY
    }
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/1/29 下午4:00
 * @Note   快速判断语法糖
 * @param  trueDo  为true时调用
 * @param  falseDo 为false时调用
 */
fun Boolean.judge(trueDo: (() -> (Unit))? = null, falseDo: (() -> (Unit))? = null) {
    if (this) {
        trueDo?.invoke()
    } else {
        falseDo?.invoke()
    }
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/1/31 下午2:25
 * @Note   快速判断语法糖
 * @param  trueValue  为true时抛出
 * @param  falseValue 为false时抛出
 */
fun <T> Boolean.judge(trueValue: T, falseValue: T): T {
    return if (this) {
        trueValue
    } else {
        falseValue
    }
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/12 下午4:24
 * @Note   快速重新定义LayoutParams重新布局
 * @param  layout 重新布局回调
 */
inline fun <T : ViewGroup.LayoutParams> View.reLayout(layout: ((params: T) -> (Unit))) {
    var params = (this.layoutParams as T)
    layout(params)
    this.layoutParams = params
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/13 上午11:09
 * @Note   顺序执行补间动画 且可拦截
 * @param  animList 动画列表
 * @param  interceptor 拦截器
 */
fun View.animSequentialStart(animList: ArrayList<Animation>, interceptor: ((index: Int, state: Int) -> (Boolean))? = null) {

    var index = 0

    var listener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
            val flag = interceptor?.invoke(index, 1)
            flag?.judge(trueDo = {
                animation?.cancel()
            })
        }

        override fun onAnimationEnd(animation: Animation?) {
            if (index != animList.size - 1) {
                animList[index].cancel()
            }
            val flag = interceptor?.invoke(index, 2)
            index += 1
            flag?.judge(trueDo = {
                animation?.cancel()
            }, falseDo = {
                if (index < animList.size) {
                    animList[index].setAnimationListener(this)
                    startAnimation(animList[index])
                }
            })
        }

        override fun onAnimationStart(animation: Animation?) {
            val flag = interceptor?.invoke(index, 3)
            flag?.judge(trueDo = {
                animation?.cancel()
            })
        }
    }
    animList[index].setAnimationListener(listener)
    startAnimation(animList[index])
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/15 下午3:57
 * @Note   根据标记查找Adapter
 * @param  tag 标记
 */
fun <T : BaseDelegateAdapter<*, *>> ArrayList<DelegateAdapter.Adapter<*>>.getAdapterByTag(tag: Any): T? {
    this.forEach { item ->
        if (item is BaseDelegateAdapter<*, *>) {
            if (item.tag == tag) {
                return item as T
            }
        }
    }
    return null
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/15 下午3:58
 * @Note   根据标记移除Adapter
 * @param  tag 标记
 */
fun <T : BaseDelegateAdapter<*, *>> ArrayList<DelegateAdapter.Adapter<*>>.removeAdapterByTag(tag: Any) {
    remove(getAdapterByTag<T>(tag) as DelegateAdapter.Adapter<*>)
}


/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/15 下午4:25
 * @Note   Vlayout获取屏幕上可见第一个Item的位置
 */
fun VirtualLayoutManager.getScollYDistance(): Int {
    val position = findFirstVisibleItemPosition()
    val firstVisiableChildView = findViewByPosition(position)
    val itemHeight = firstVisiableChildView?.height
    return position * (itemHeight ?: 0) - (firstVisiableChildView?.top ?: 0)
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/3/21 上午9:55
 * @Note   转换弱引用
 */
fun <T : Any> T.weak(): WeakReference<T> {
    return WeakReference(this)
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/4/12 上午8:56
 * @Note   加入管理器
 * @param  manager 管理器
 */
fun Disposable.joinManager(manager: DisposableManager) {
    manager.addDisposable(this)
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/4/19 下午2:50
 * @Note   dp转px
 */
fun Int.dpToPx(): Int {
    return ScreenTool.dip2px(BaseApplication.appContext, this.toFloat())
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/4/19 下午2:50
 * @Note   px转dp
 */
fun Int.pxToDp(): Int {
    return ScreenTool.px2dip(BaseApplication.appContext, this.toFloat())
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/4/19 下午2:50
 * @Note   dp转px
 */
fun Double.dpToPx(): Int {
    return ScreenTool.dip2px(BaseApplication.appContext, this.toFloat())
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/4/19 下午2:50
 * @Note   px转dp
 */
fun Double.pxToDp(): Int {
    return ScreenTool.px2dip(BaseApplication.appContext, this.toFloat())
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/5/29 上午10:01
 * @Note   获取对象
 * @param  key
 * @param  cls class对象
 */
fun <T> Intent.getObjectForGson(key: String, cls: Class<T>): T? {
    return if (hasExtra(key)) {
        val json = getStringExtra(key)
        try {
            JsonTranforHelper.toObject(json, cls)
        } catch (e: Exception) {
            null
        }
    } else {
        null
    }
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/5/29 上午10:03
 * @Note   设置对象根据Gson
 * @param  key 索引
 * @param  value 值
 */
fun Intent.setObjectForGson(key: String, value: Any) {
    putExtra(key, JsonTranforHelper.toJson(value))

}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/5/29 下午4:13
 * @Note   快速转换databinding字段
 */
fun <T> T.bindingParams(): ObservableField<T> {
    return ObservableField(this)
}

/**
 * @author LDD
 * @From   com.enation.javashop.android.lib.utils ExtendMethods.kt
 * @Date   2018/8/13 上午10:13
 * @Note   以流的方式获取Json的字符串
 */
fun ResponseBody.getJsonString() : String{
    source().request(Long.MAX_VALUE)
    val buffer = source().buffer()
    return buffer.clone().readString(Charset.forName("UTF-8"))
}

fun ResponseBody.toJsonObject() :JSONObject{
    val jsonString = getJsonString()
    return if (jsonString.isEmpty()){
        JSONObject()
    }else{
        JSONObject(jsonString)
    }
}

fun ResponseBody.toJsonArray() :JSONArray{
    val jsonString = getJsonString()
    return if (jsonString.isEmpty()){
        JSONArray()
    }else{
        JSONArray(jsonString)
    }
}


/** 参数获取扩展方法 防止空值 */

fun JSONObject.valueString(key: String) :String{
    return if (has(key) &&  !isNull(key)){
        getString(key)
    }else{
        ""
    }}

fun JSONObject.valueInt(key :String) :Int{
    return if (has(key) &&  !isNull(key)){
        getInt(key)
    }else{
        -1
    }}

fun JSONObject.valueBool(key: String) :Boolean{
    return if (has(key) &&  !isNull(key)){
        getBoolean(key)
    }else{
        false
    }}

fun JSONObject.valueDouble(key: String) :Double{
    return if (has(key) &&  !isNull(key)){
        getDouble(key)
    }else{
        -1.0
    }
}

fun JSONObject.valueDate(key:String) :String{
    return if(has(key) && !isNull(key)){
        CommonTool.toString(Date(valueLong(key)*1000),null)
    }else{
        ""
    }
}

fun JSONObject.valueLong(key: String) :Long{
    return if (has(key) &&  !isNull(key)){
        getLong(key)
    }else{
        -1
    }
}

fun JSONObject.valueJsonObject(key :String) :JSONObject{
    return if (has(key) &&  !isNull(key)){
        getJSONObject(key)
    }else{
        JSONObject()
    }
}

fun JSONObject.valueJsonArray(key :String) :JSONArray{
    return if (has(key) &&  !isNull(key)){
        getJSONArray(key)
    }else{
        JSONArray()
    }
}

fun JSONArray.arrayObjects() : ArrayList<JSONObject>{

    var result = ArrayList<JSONObject>()

    for (i in 0..(this.length() - 1)){
        result.add(getJSONObject(i))
    }

    return  result

}

/**=================================================*/

fun View.visable(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.invisable(){
    this.visibility = View.INVISIBLE
}
