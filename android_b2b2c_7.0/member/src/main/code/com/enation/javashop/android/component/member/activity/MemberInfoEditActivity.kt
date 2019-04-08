package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberEditInfoLayBinding
import com.enation.javashop.android.component.member.fragment.MemberInfoEditFragment
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.GalleryActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.api.MemberState
import com.enation.javashop.android.middleware.event.LoginEvent
import com.enation.javashop.android.middleware.logic.contract.member.MemberInfoEditContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberInfoEditPresenter
import com.enation.javashop.android.middleware.model.MemberViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.districtselectorview.widget.DistrictSelectorView
import com.enation.javashop.net.engine.model.NetState
import com.enation.javashop.photoutils.model.TResult
import com.enation.javashop.photoutils.uitl.RxGetPhotoUtils
import com.enation.javashop.utils.base.tool.CommonTool
import kotlinx.android.synthetic.main.member_edit_info_lay.*

/**
 * @author LDD
 * @Date   2018/5/15 下午3:08
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   会员修改信息页面
 */
@Router(path = "/member/privacy/info/edit")
class MemberInfoEditActivity : GalleryActivity<MemberInfoEditPresenter, MemberEditInfoLayBinding>(),MemberInfoEditContract.View {

    /**
     * @Name  renderFlag
     * @Type  Boolean
     * @Note  渲染标记
     */
    private var renderFlag = false

    /**
     * @Name  editFragment
     * @Type  MemberInfoEditFragment
     * @Note  更改信息fragment
     */
    private val editFragment by lazy { MemberInfoEditFragment.build(this,R.id.member_edit_info_root) }

    /**
     * @Name  addressSelector
     * @Type  DistrictSelectorView
     * @Note  地址选择器
     */
    private val addressSelector by lazy {
        DistrictSelectorView<RegionViewModel>(activity).setRegionListener(object :DistrictSelectorView.RegionListener<RegionViewModel>{

            override fun getResult(p0: RegionViewModel?, p1: RegionViewModel?, p2: RegionViewModel?, p3: RegionViewModel?) {
                var member = mViewBinding.data

                if ( p2 == null){
                    return
                }

                p0?.more {
                    member.pro = it.name
                    member.proId = it.id
                }
                p1?.more {
                    member.city = it.name
                    member.cityId = it.id
                }
                p2?.more {
                    member.county = it.name
                    member.countyId = it.id
                }
                (p3 != null).judge({
                    member.town = p3?.name
                    member.townId = p3?.id
                },{
                    member.town = ""
                    member.townId = 0
                })

                mViewBinding.data = member
            }

            override fun setPickData(p0: RegionViewModel?) {
                (p0==null).judge({
                    presenter.loadRegion(0)
                },{
                    if(p0!!.typeId != 4){
                        presenter.loadRegion(p0.id)
                    }
                })
            }
        })
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:11
     * @Note   获取布局ID
     */
    override fun getLayId(): Int {
        return R.layout.member_edit_info_lay
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:12
     * @Note   依赖注入
     */
    override fun bindDagger() {
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/8/15 上午10:16
     * @Note   渲染地址
     * @param  datas 数据
     */
    override fun renderRegion(datas: ArrayList<RegionViewModel>) {
        addressSelector.setData(datas)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:12
     * @Note   初始化
=     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        mViewBinding.memberEditInfoSave.isSelected = true
        presenter.loadMember()
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:13
     * @Note   初始化
     */
    override fun bindEvent() {
        mViewBinding.memberEditInfoNikenameTv.setOnClickListener {
            editFragment.setContentText(mViewBinding.data.nikename!!)
                    .setEvent {
                        contentText ->
                        var member = mViewBinding.data
                        member.nikename = contentText
                        mViewBinding.data = member
                    }.show()
        }
        mViewBinding.memberEditInfoSexTv.setOnClickListener {
            CommonTool.createVerifyDialog("请选择您的性别！","男","女",activity, object : CommonTool.DialogInterface{
                override fun yes() {
                    var member = mViewBinding.data
                    member.sex = 0
                    mViewBinding.data = member
                }

                override fun no() {
                    var member = mViewBinding.data
                    member.sex = 1
                    mViewBinding.data = member
                }
            }).show()
        }
        mViewBinding.memberEditInfoAddressTv.setOnClickListener {
            editFragment.setContentText(mViewBinding.data.address!!)
                    .setEvent {
                        contentText ->
                        var member = mViewBinding.data
                        member.address = contentText
                        mViewBinding.data = member
                    }.show()
        }
        mViewBinding.memberEditInfoEmailTv.setOnClickListener {
            editFragment.setContentText(mViewBinding.data.email!!)
                    .setEvent {
                        contentText ->
                        var member = mViewBinding.data
                        member.email = contentText
                        mViewBinding.data = member
                    }.show()
        }
        mViewBinding.memberEditInfoTelTv.setOnClickListener {
            editFragment.setContentText(mViewBinding.data.tel!!)
                    .setEvent {
                        contentText ->
                        var member = mViewBinding.data
                        member.tel = contentText
                        mViewBinding.data = member
                    }.show()
        }
        mViewBinding.memberEditInfoSave.setOnClickListener {
            presenter.editMember(mViewBinding.data)
        }
        mViewBinding.memberEditInfoBirthdayTv.setOnClickListener {
            TimePickerBuilder(activity) { date, view ->
                var member = mViewBinding.data
                member.birthday = date.time
                mViewBinding.data = member
            }.build().show(true)
        }
        mViewBinding.memberEditInfoRegionTv.setOnClickListener {
            addressSelector.show()
        }
        member_edit_info_topbar.setLeftClickListener {
            pop()
        }
        mViewBinding.memberEditInfoUfaceIv.setOnClickListener {
            RxGetPhotoUtils.init(this).configCompress(true,true,true,102400,800,800).getPhotoFromGallery(false)
        }
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:13
     * @Note   渲染UI
     * @param  member 会员
     */
    override fun renderMemberUi(member: MemberViewModel) {
        mViewBinding.data = member
        if (!renderFlag){
            renderFlag = true
        }else{
            MemberState.manager.updateMember(member)
            getEventCenter().post(LoginEvent())
        }
        Glide.with(this).load(member.face).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.member_login_userface).into(member_edit_info_uface_iv)
    }

    override fun refreshUserface(face: String) {
        var member = mViewBinding.data
        member.face = face
        presenter.editMember(member)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:14
     * @Note   错误回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun onError(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }


    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:14
     * @Note   完成回调
     * @param  message 信息
     * @param  type 类型
     */
    override fun complete(message: String, type: Int) {
        showMessage(message)
        dismissDialog()
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:14
     * @Note   开始回调
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:15
     * @Note   网络监听
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/5/15 下午3:15
     * @Note   销毁
     */
    override fun destory() {

    }

    override fun takeSuccess(result: TResult) {
        val filePath = result.image.compressPath
        presenter.uploader(filePath)
    }

    /**
     * @author LDD
     * @From   MemberInfoEditActivity
     * @Date   2018/4/24 下午2:42
     * @Note   重写退出键监听
     */
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 0) {
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

}