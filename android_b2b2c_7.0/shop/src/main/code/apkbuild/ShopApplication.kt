//package apkbuild
//
//import com.enation.javashop.android.jrouter.JRouter
//import com.enation.javashop.android.lib.base.BaseApplication
//import com.enation.javashop.net.engine.config.NetEngineConfig
//import com.enation.javashop.net.engine.plugin.exception.RestfulExceptionInterceptor
//import com.enation.javashop.utils.base.config.BaseConfig
//import com.squareup.leakcanary.LeakCanary
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import io.reactivex.functions.Function3
//
///**
// * 单Moudle编译 Application
// */
//class ShopApplication : BaseApplication() {
//    /**
//     * @author LDD
//     * @From   Application
//     * @Date   2018/1/11 下午12:50
//     * @Note   应用启动时调用
//     */
//    override fun onCreate() {
//        super.onCreate()
//        Observable.zip(initRouter().subscribeOn(Schedulers.newThread()),
//                initLeaks().subscribeOn(Schedulers.newThread()),
//                initFrame().subscribeOn(Schedulers.newThread()),
//                Function3<String,String,String,String> { _ , _ , _ -> return@Function3 "" }).
//                observeOn(AndroidSchedulers.mainThread()).subscribe({})
//    }
//
//    /**
//     * @author  LDD
//     * @From    Application
//     * @Date   2018/1/11 下午12:50
//     * @Note   初始化路由
//     * @return rx观察者
//     */
//    private fun initRouter(): Observable<String> {
//        return Observable.create {
//            JRouter.init(this)
//            JRouter.openDebug()
//            JRouter.openLog()
//            JRouter.prepare().create("/welcome/launch").seek()
//        }
//    }
//
//    /**
//     * @author  LDD
//     * @From    Application
//     * @Date   2018/1/11 下午12:50
//     * @Note   初始化内存检测器
//     * @return rx观察者
//     */
//    private fun initLeaks(): Observable<String> {
//        return Observable.create {
//            LeakCanary.install(this)
//        }
//    }
//
//    /**
//     * @author  LDD
//     * @From    Application
//     * @Date   2018/1/11 下午12:50
//     * @Note   初始化内部框架
//     * @return rx观察者
//     */
//    private fun initFrame(): Observable<String> {
//        return Observable.create {
//            if (android.os.Build.VERSION.SDK_INT> android.os.Build.VERSION_CODES.LOLLIPOP){
//                BaseConfig.getInstance().addActivity("HomeActivity")
//            }else{
//                BaseConfig.getInstance().closeScrollBack()
//            }
//            NetEngineConfig.init(baseContext)
//                    .openLogger()
//                    .addNetInterceptor(RestfulExceptionInterceptor())
//        }
//    }
//}