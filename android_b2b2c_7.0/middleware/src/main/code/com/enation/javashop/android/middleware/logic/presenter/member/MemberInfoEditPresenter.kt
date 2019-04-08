package com.enation.javashop.android.middleware.logic.presenter.member

import com.enation.javashop.android.lib.base.RxPresenter
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.vo.NetStateEvent
import com.enation.javashop.android.middleware.api.BaseApi
import com.enation.javashop.android.middleware.api.MemberApi
import com.enation.javashop.android.middleware.di.MiddlewareDaggerComponent
import com.enation.javashop.android.middleware.logic.contract.member.MemberInfoEditContract
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.net.engine.plugin.connection.ConnectionQuality
import com.enation.javashop.net.engine.plugin.exception.ExceptionHandle
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.File
import javax.inject.Inject

/**
 * @author LDD
 * @Date   2018/5/15 下午3:02
 * @From   com.enation.javashop.android.middleware.logic.presenter.member
 * @Note   会员信息修改页面
 */
class MemberInfoEditPresenter @Inject constructor() :RxPresenter<MemberInfoEditContract.View>() ,MemberInfoEditContract.Presenter {

    /**
     * @Name  Edit
     * @Type  Int
     * @Note  标示为修改信息类型
     */
    val Edit = 1

    /**
     * @Name  memberApi
     * @Type  MemberApi
     * @Note  Api
     */
    @Inject
    protected lateinit var memberApi: MemberApi

    /**
     * @Name  baseApi
     * @Type  BaseApi
     * @Note  Api
     */
    @Inject
    protected lateinit var baseApi: BaseApi

    /**
     * @Name  observer
     * @Type  ConnectionObserver
     * @Note  数据监听者
     */
    private val observer = object : ConnectionObserver<Any>(){
        override fun onStartWithConnection() {
            providerView().start()
        }

        override fun onNextWithConnection(result: Any, connectionQuality: ConnectionQuality) {
            providerView().complete()
            when (result){
                is MemberViewModel -> {
                    providerView().renderMemberUi(result)
                }
                is ArrayList<*> ->{
                    providerView().renderRegion(result as ArrayList<RegionViewModel>)
                }
                is String ->{
                    providerView().refreshUserface(result)
                }
            }
        }

        override fun onErrorWithConnection(error: ExceptionHandle.ResponeThrowable, connectionQuality: ConnectionQuality) {
            providerView().onError(error.customMessage)
        }

        override fun attachSubscribe(var1: Disposable) {
            addDisposable(var1)
        }

        override fun onNoneNet() {
            getEventCenter().post(NetStateEvent(NetState.NONE))
        }
    }

    /**
     * @author LDD
     * @From   MemberInfoEditPresenter
     * @Date   2018/5/15 下午3:04
     * @Note   加载会员信息
     */
    override fun loadMember() {

        memberApi.memberInfo()
                .map { return@map MemberViewModel.map(it) }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditPresenter
     * @Date   2018/5/15 下午3:04
     * @Note   修改会员信息
     * @param  member 会员信息
     */
    override fun editMember(member: MemberViewModel) {
        var regionId :Int? = member.getRegionId()
        if (regionId == null || regionId <= 0){
            regionId = null
        }
        memberApi.editMemberInfo(member.nikename!!,regionId,member.sex,member.birthday!!,member.address!!,member.email!!,member.tel!!,member.face!!)
                .map { return@map MemberViewModel.map(it) }
                .compose(ThreadFromUtils.defaultSchedulers())
                .subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditPresenter
     * @Date   2018/8/15 上午9:46
     * @Note   加载地址数据
     * @param  id 索引
     */
    override fun loadRegion(id: Int) {
        baseApi.loadRegion(id).map {
            val datas = ArrayList<RegionViewModel>()
            val result = it.getJsonString()
            if (result != ""){
                val jsonArray = JSONArray(result)
                for (i in 0..(jsonArray.length() - 1)){
                    datas.add(RegionViewModel.map(jsonArray.getJSONObject(i)))
                }
            }
            return@map datas
        } .compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditPresenter
     * @Date   2018/5/15 下午3:03
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MiddlewareDaggerComponent.component.inject(this)
    }

    override fun uploader(filePath: String) {
        val localFile = File(filePath)
        val localRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), localFile)
        val localPart = MultipartBody.Part.createFormData("file", localFile.name, localRequestBody)
        baseApi.uploader(localPart,"")
                .map { responseBody ->
                    return@map responseBody.toJsonObject().valueString("url")
                }.compose(ThreadFromUtils.defaultSchedulers()).subscribe(observer)
    }
}