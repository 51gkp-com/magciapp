package com.enation.javashop.android.lib.base

import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.enation.javashop.photoutils.app.TakePhoto
import com.enation.javashop.photoutils.app.TakePhotoImpl
import com.enation.javashop.photoutils.model.InvokeParam
import com.enation.javashop.photoutils.model.TContextWrap
import com.enation.javashop.photoutils.model.TResult
import com.enation.javashop.photoutils.permission.InvokeListener
import com.enation.javashop.photoutils.permission.PermissionManager
import com.enation.javashop.photoutils.permission.TakePhotoInvocationHandler
import com.enation.javashop.photoutils.uitl.TakePhotoinf

/**
 * @author  LDD
 * @Data   2017/12/26 下午12:07
 * @From   com.enation.javashop.android.lib.base
 * @Note   带有相册功能的Fragment基类
 */
 abstract class GalleryFragment<PresenterType : BaseContract.BasePresenter, DataBindingType : ViewDataBinding> : BaseFragment<PresenterType,DataBindingType>(), TakePhoto.TakeResultListener, InvokeListener, TakePhotoinf {

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
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
     * @Data   2017/12/26 下午12:09
     * @Note   Activity创建时调用 相册对象注册初始化生命周期
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        getTakePhoto()!!.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
     * @Data   2017/12/26 下午12:12
     * @Note   保存参数，防止丢失
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        getTakePhoto()!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
     * @Data   2017/12/26 下午12:12
     * @Note   获取相机返回数据
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getTakePhoto()!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
     * @Data   2017/12/26 下午12:12
     * @Note   获取相机返回数据
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeparam, this)
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
     * @Data   2017/12/26 下午12:13
     * @Note   Activity销毁时调用
     */
    override fun onDestroyView() {
        takePhoto=null
        super.onDestroyView()
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
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
     * @author  LDD
     * @From   com.enation.javashop.android.lib.base.GalleryFragment
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
     * 实现获取照片成功接口 子类自由复写
     */
    override fun takeSuccess(result: TResult?) {}

    /**
     * 实现获取照片取消接口 子类自由复写
     */
    override fun takeCancel() {}

    /**
     * 实现获取照片失败接口 子类自由复写
     */
    override fun takeFail(result: TResult?, msg: String?) {}
}