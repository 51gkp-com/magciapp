package com.enation.javashop.android.lib.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.utils.base.tool.BaseInterface
import com.enation.javashop.utils.base.tool.BaseToolActivity
import javax.inject.Inject

/**
 * @author  LDD
 * @Data   2017/12/26 上午11:26
 * @From   com.enation.javashop.android.lib.base
 * @Note   Fragment基类
 */
  abstract class BaseFragment<PresenterType : BaseContract.BasePresenter, DataBindingType : ViewDataBinding> : Fragment(),BaseControl {

    /**
     * @Name  layout
     * @Type  android.view.View
     * @Note  View根视图
     */
    private var layout: View? = null

    /**
     * @Name  activity
     * @Type  com.enation.javashop.android.lib.base.BaseActivity
     * @Note  Fragment宿主Activity
     */
    protected lateinit var activity: BaseToolActivity

    /**
     * @Name  mViewDataBinding
     * @Type  T2 : ViewDataBinding
     * @Note  DataBinding对象
     */
    protected lateinit var mViewDataBinding: DataBindingType

    /**
     * @Name  lifecycleCalls
     * @Type  ArrayList<((state :Int) ->Unit)>
     * @Note  回调集合
     */
    private val lifecycleCalls by lazy { ArrayList<((state :Int) ->Unit)>() }

    /**
     * @Name  presenter
     * @Type  T : BaseContract.BasePresenter
     * @Note  Fargment的Presenter Dagger自动注入
     */
    @Inject
    protected lateinit var presenter: PresenterType

    /**
     * @Name  disposableManager
     * @Type  DisposableManager
     * @Note  Rx索引管理
     */
    protected val disposableManager by lazy { DisposableManager() }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:38
     * @Note   Fragment创建时调用
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**初始化根视图及DataBinding*/
        if (layout == null) {
            layout = inflater.inflate(getLayId(), null)
            mViewDataBinding = DataBindingUtil.bind(layout)
        }

        /**初始化宿主Activity*/
        activity = getActivity() as BaseToolActivity
        return layout
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:43
     * @Note   在Fragment视图创建完毕后调用
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_CREATE)

        /**绑定Dagger*/
        bindDagger()

        /**绑定视图*/
        attachView()

        /**执行其他初始化操作*/
        init()

        /**初始化响应事件*/
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
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:45
     * @Note   在Fragment销毁时调用
     */
    override fun onDestroyView() {

        /**调用父类方法*/
        super.onDestroyView()

        /**Presenter解除绑定*/
        detachView()

        /**ViewBinding解除绑定*/
        mViewDataBinding.unbind()

        /**执行其他销毁操作*/
        destory()

        /**解除RX引用*/
        disposableManager.unDisposable()

        /**执行生命周期监听*/
        lifeCycleDo(LIFE_CYCLE_DESTORY)

        /**清除生命周期监听引用*/
        removeAllCallBack()

        errorLog("FragmentDestory","页面销毁======>${javaClass.name}")

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
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:47
     * @Note   获取根视图LayoutId
     * @return LayoutId
     */
    protected abstract fun getLayId(): Int

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:47
     * @Note   绑定Dagger
     */
    protected abstract fun bindDagger()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:50
     * @Note   初始化其他操作
     */
    protected abstract fun init()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:50
     * @Note   绑定事件
     */
    protected abstract fun bindEvent()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:50
     * @Note   其他销毁操作
     */
    protected abstract fun destory()

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:52
     * @Note   销毁View操作
     */
    protected open fun attachView() {
        presenter.attachView(this)
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
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.BaseFragment
     * @Data   2017/12/26 上午11:57
     * @Note   获取工具对象
     * @return 工具对象
     */
    protected fun getUtils(): BaseInterface {

        return activity
    }
}