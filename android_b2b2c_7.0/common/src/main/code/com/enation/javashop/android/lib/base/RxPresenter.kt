package com.enation.javashop.android.lib.base

import com.enation.javashop.android.lib.utils.errorLog
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.lib.utils.to
import com.enation.javashop.android.lib.vo.NetStateEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author  LDD
 * @Data   2017/12/26 下午12:22
 * @From   com.enation.javashop.android.lib.base
 * @Note   Presenter基类
 */
abstract class RxPresenter<out ViewType : BaseContract.BaseView> : DisposableManager() , BaseContract.BasePresenter {

     /**
      * @Name  mView
      * @Type  T : BaseContract.BaseView
      * @Note  View接口
      */
     private var mView: ViewType? = null


    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.RxPresenter
     * @Data   2017/12/26 下午12:31
     * @Note   注入View接口和API 注册网络状态Event
     * @param  view View接口
     * @param  api  API
     */
    override fun attachView(view: Any) {
        bindDagger()
        /**注入View接口*/
        try {
            this.mView = view.to()
        }catch (e :Exception){
            errorLog("Interface Not Implement","Fragment/Activity 没有实现对应BaseView接口")
        }
        /**注册网络状态事件*/
        var disposable = getEventCenter().register(NetStateEvent::class.java,{
            result ->
            this.mView!!.networkMonitor(result.state)
        })
        addDisposable(disposable)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.RxPresenter
     * @Data   2017/12/26 下午12:35
     * @Note   销毁操作
     */
    override fun detachView() {
        this.mView = null
        unDisposable()
    }

    /**
     * @author LDD
     * @Date   2018/1/19 下午6:16
     * @From   com.enation.javashop.android.lib.base.RxPresenter
     * @Note   依赖注入初始化
     */
    abstract fun bindDagger()

    /**
     * @author LDD
     * @From   RxPresenter
     * @Date   2018/1/19 下午6:16
     * @Note   View接口提供者
     * @return View接口
     */
    fun providerView():ViewType{
        return mView!!
    }

}