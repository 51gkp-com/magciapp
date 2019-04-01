package com.enation.javashop.android.lib.base

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.enation.javashop.android.jrouter.JRouter
import com.enation.javashop.android.lib.utils.debugLog
import com.enation.javashop.photoutils.app.TakePhoto
import com.enation.javashop.photoutils.app.TakePhotoImpl
import com.enation.javashop.photoutils.model.InvokeParam
import com.enation.javashop.photoutils.model.TContextWrap
import com.enation.javashop.photoutils.model.TResult
import com.enation.javashop.photoutils.permission.InvokeListener
import com.enation.javashop.photoutils.permission.PermissionManager
import com.enation.javashop.photoutils.permission.TakePhotoInvocationHandler
import com.enation.javashop.photoutils.uitl.TakePhotoinf
import com.enation.javashop.utils.base.tool.BaseToolActivity
import java.lang.reflect.Field
import javax.inject.Inject

/**
 * @author  LDD
 * @Data   2017/12/26 下午12:07
 * @From   com.enation.javashop.android.lib.base
 * @Note   带有相册功能的Activity基类
 */
 abstract class GalleryActivity<PresenterType : BaseContract.BasePresenter, DataBindingType : ViewDataBinding>: BaseToolActivity(),BaseControl , TakePhoto.TakeResultListener, InvokeListener, TakePhotoinf {


    /**
     * @Name  takePhoto
     * @Type  com.enation.javashop.photoutils.app.TakePhoto
     * @Note  TakePhoto对象
     */
    private  var takePhoto : TakePhoto? = null

    /**
     * @Name  invokeparam
     * @Type  com.enation.javashop.photoutils.model.InvokeParam
     * @Note  invokeparam
     */
    private  var invokeparam: InvokeParam? = null

    /**
     * @Name  presenter
     * @Type  T : BaseContract.BasePresenter
     * @Note  Activity中Presenter Dagger自动注入
     */
    @Inject
    protected lateinit var presenter: PresenterType

    /**
     * @Name  presenter
     * @Type  T2 : ViewDataBinding
     * @Note  DataBinding对象
     */
    protected lateinit var mViewBinding: DataBindingType

    /**
     * @Name  lifecycleCalls
     * @Type  ArrayList<((state :Int) ->Unit)>
     * @Note  回调集合
     */
    private val lifecycleCalls by lazy { ArrayList<((state :Int) ->Unit)>() }

    /**
     * @Name  disposableManager
     * @Type  DisposableManager
     * @Note  Rx索引管理
     */
    protected val disposableManager by lazy { DisposableManager() }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:30
     * @Note   Activity创建时进行相关的配置
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        getTakePhoto()!!.onCreate(savedInstanceState)
        try {
            JRouter.prepare().inject(this)
        }catch (e :Exception){
            debugLog("Init","首页初始化完毕")
        }
        /**父类初始化*/
        super.onCreate(savedInstanceState)
        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_CREATE)
        /**创建根视图*/
        val rootView = layoutInflater.inflate(getLayId(), null, false)
        /**初始化Databinding对象*/
        mViewBinding = DataBindingUtil.bind(rootView)
        /**设置根视图到Activity*/
        setContentView(rootView)
        /**执行抽象方法初始化Dagger相应操作*/
        bindDagger()
        /**Presenter绑定View*/
        attachView()
        /**执行初始化操作*/
        init()
        /**执行绑定event操作*/
        bindEvent()
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/3/29 上午11:46
     * @Note   执行生命周期监听 恢复
     */
    override fun onResume() {
        super.onResume()
        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_RESUME)
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/3/29 上午11:46
     * @Note   执行生命周期监听 暂停
     */
    override fun onPause() {
        super.onPause()
        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_PAUSE)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:34
     * @Note   Activity销毁回调
     */
    override fun onDestroy() {
        super.onDestroy()
        /**初始化相机*/
        takePhoto = null
        /**Presenter解除绑定*/
        detachView()
        /**DataBinding解除绑定*/
        mViewBinding.unbind()
        /**执行抽象方法destory()*/
        destory()
        /**解除RX引用*/
        disposableManager.unDisposable()
        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_DESTORY)
        /**清除声明周期监听引用*/
        removeAllCallBack()
        /**处理android4.4.2 底层内存泄漏*/
        fixInputMethodManagerLeak(activity)
        debugLog("PageDestory","页面销毁======>$localClassName")
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:39
     * @Note   获取Activity_LayoutId
     */
    protected abstract fun getLayId(): Int

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:44
     * @Note   执行绑定Dagger操作
     */
    protected abstract fun bindDagger()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:45
     * @Note   执行初始化操作
     */
    protected abstract fun init()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:45
     * @Note   执行绑定事件操作
     */
    protected abstract fun bindEvent()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:46
     * @Note   执行销毁相关操作
     */
    protected abstract fun destory()

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/1/19 下午6:03
     * @Note   绑定View接口
     */
    protected open fun attachView() {
        presenter.attachView(this )
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/5/10 下午3:17
     * @Note   解绑View接口
     */
    protected open fun detachView(){
        presenter.detachView()
    }



    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/3/29 上午11:37
     * @Note   添加callBack
     * @param  listener 监听回调
     */
    override fun addLifeCycleListener(listener: (state: Int) -> Unit) {
        lifecycleCalls.add(listener)
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/3/29 上午11:43
     * @Note   清除生命周期监听
     */
    private fun removeAllCallBack(){
        lifecycleCalls.clear()
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/3/29 上午11:44
     * @Note   在各个生命周期监听执行
     * @param  state 生命周期状态
     */
    private fun lifeCycleDo(state :Int){
        lifecycleCalls.forEach {
            item ->
            item.invoke(state)
        }
    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/5/27 下午1:22
     * @Note   页面返回数据
     * @param  resultCode 返回码
     * @param  data       数据
     */
    open fun resultHandle(resultCode: Int,data: Intent?){

    }

    /**
     * @author LDD
     * @From   BaseActivity
     * @Date   2018/5/27 下午1:22
     * @Note   页面返回数据回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getTakePhoto()!!.onActivityResult(requestCode, resultCode, data)
        resultHandle(resultCode,data)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseActivity
     * @Data   2017/12/26 上午9:50
     * @Note   处理4.4.2 Android底层内存泄漏
     * @param  destContext 上下文
     */
    private fun fixInputMethodManagerLeak(destContext: Context?) {
        if (destContext == null) {
            return
        }
        val imm = destContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var f: Field?
        var obj_get: Any?
        arr.indices
                .asSequence()
                .map { arr[it] }
                .forEach {
                    try {
                        f = imm.javaClass.getDeclaredField(it)
                        if (f!!.isAccessible === false) {
                            f!!.isAccessible = true
                        }
                        obj_get = f!!.get(imm)
                        if (obj_get != null && obj_get is View) {
                            val v_get = obj_get as View?
                            if (v_get!!.getContext() === destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                                f!!.set(imm, null) // 置空，破坏掉path to gc节点
                            }
                        }
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryActivity
     * @Data   2017/12/26 下午12:12
     * @Note   保存参数，防止丢失
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        getTakePhoto()!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryActivity
     * @Data   2017/12/26 下午12:12
     * @Note   获取相机返回数据
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeparam, this)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryActivity
     * @Data   2017/12/26 下午12:15
     * @Note   鉴权
     */
    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.getMethod())
        if (PermissionManager.TPermissionType.WAIT == type) {
            this.invokeparam = invokeParam!!
        }
        return type
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryActivity
     * @Data   2017/12/26 下午12:15
     * @Note   获取TakePhoto实例
     */
    override fun getTakePhoto(): TakePhoto? {
        if (takePhoto == null) {
            takePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
        }
        return takePhoto
    }

    /**
     * 实现获取照片成功接口 子类自由复写
     */
    override fun takeSuccess(result: TResult) {}

    /**
     * 实现获取照片取消接口 子类自由复写
     */
    override fun takeCancel() {}

    /**
     * 实现获取照片失败接口 子类自由复写
     */
    override fun takeFail(result: TResult?, msg: String?) {}
}