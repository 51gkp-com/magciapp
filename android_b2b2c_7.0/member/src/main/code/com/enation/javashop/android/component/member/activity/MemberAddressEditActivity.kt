package com.enation.javashop.android.component.member.activity

import android.graphics.Color
import android.view.View
import com.enation.javashop.android.component.member.R
import com.enation.javashop.android.component.member.databinding.MemberAddressEditLayBinding
import com.enation.javashop.android.component.member.launch.MemberLaunch
import com.enation.javashop.android.jrouter.external.annotation.Autowired
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.enum.GlobalState
import com.enation.javashop.android.middleware.event.AddressListDataChange
import com.enation.javashop.android.middleware.event.OrderListDataChange
import com.enation.javashop.android.middleware.logic.contract.member.MemberAddressEditContract
import com.enation.javashop.android.middleware.logic.presenter.member.MemberAddressEditPresenter
import com.enation.javashop.android.middleware.model.MemberAddressViewModel
import com.enation.javashop.android.middleware.model.RegionViewModel
import com.enation.javashop.districtselectorview.widget.DistrictSelectorView
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.member_address_edit_lay.*

/**
 * @author LDD
 * @Date   2018/5/8 上午10:48
 * @From   com.enation.javashop.android.component.member.activity
 * @Note   地址修改添加页面
 */
@Router(path = "/member/address/edit")
class MemberAddressEditActivity : BaseActivity<MemberAddressEditPresenter,MemberAddressEditLayBinding>(),MemberAddressEditContract.View {

    @Autowired(name = "order",required = false)
    @JvmField var isOrder :Boolean = false


    /**
     * @author LDD
     * @Date   2018/5/8 上午11:08
     * @From   MemberAddressEditActivity
     * @Note   伴生对象
     */
    companion object {

        /**
         * @Name  com.enation.javashop.android.component.member.activity.MemberAddressEditActivity.Companion.ADDRESS_ADD
         * @Type  String
         * @Note  添加
         */
        val ADDRESS_ADD = "add"

        /**
         * @Name  com.enation.javashop.android.component.member.activity.MemberAddressEditActivity.Companion.ADDRESS_EDIT
         * @Type  String
         * @Note  编辑
         */
        val ADDRESS_EDIT = "edit"

    }

    /**
     * @Name  type
     * @Type  String
     * @Note  类型（自动注入）
     */
    @Autowired(name= "type",required = true)
    @JvmField var type: String = ADDRESS_ADD

    /**
     * @Name  address
     * @Type  MemberAddressViewModel
     * @Note  地址信息（自动注入）
     */
    @Autowired(name= "address",required = false)
    @JvmField var address: MemberAddressViewModel = MemberAddressViewModel(1,"","","",-1,"",-1,"",-1,"",-1,"","",false)

    /**
     * @Name  addressSelectorView
     * @Type  DistrictSelectorView
     * @Note  地址选择器
     */
    private val addressSelectorView  by lazy {  DistrictSelectorView<RegionViewModel>(this).setRegionListener(object :DistrictSelectorView.RegionListener<RegionViewModel>{

        override fun getResult(p0: RegionViewModel?, p1: RegionViewModel?, p2: RegionViewModel?, p3: RegionViewModel?) {
            if (p0 != null){
                address.proId = p0.id
                address.proName = p0.name
            }
            if (p1 != null){
                address.cityId = p1.id
                address.cityName = p1.name
            }
            if (p2 != null){
                address.counId = p2.id
                address.counName = p2.name
            }
            if (p3 != null){
                address.townId = p3.id
                address.townName = p3.name

            }
            address.addressRegionObser.set("${address.proName}${address.cityName}${address.counName}${address.townName}")
            address.addressInfoObser.set("${address.proName}${address.cityName}${address.counName}${address.townName}  ${address.addressDetailObser.get()}")
        }

        override fun setPickData(p0: RegionViewModel?) {
            if (p0 == null){
                presenter.getRegion(0)
            }else{
                if(p0!!.typeId != 4){
                    presenter.getRegion(p0.id)
                }
            }
        }
    })}

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:50
     * @Note   提供LayoutId
     */
    override fun getLayId(): Int {
        return R.layout.member_address_edit_lay
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:53
     * @Note   依赖注入
     */
    override fun bindDagger() {
        /**注入参数*/
        MemberLaunch.component.inject(this)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:54
     * @Note   初始化
     */
    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        if (type == ADDRESS_ADD){
            member_address_edit_topbar.setTitleText("添加收货地址")
            member_address_edit_topbar.setRightVisibility(false)
        }else{
            member_address_edit_topbar.setRightVisibility(true)
        }
        mViewBinding.data = address
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:54
     * @Note   绑定事件
     */
    override fun bindEvent() {


        /**标签选择*/
        val tagSelectedListener = View.OnClickListener {

            /**把全部标签转换为一个array 并for循环*/
            arrayOf(member_address_edit_tag_company, member_address_edit_tag_home, member_address_edit_tag_school, member_address_edit_tag_new).forEach { item ->
                /**当点击的View等于Item*/
                if (item == it) {
                    /**如果该Item已选中 重置为未选中状态*/
                    if (item.isSelected) {
                        /**如果该Item是新增标签 单独设置背景*/
                        if (item == member_address_edit_tag_new) {
                            item.setBackgroundResource((member_address_edit_tag_new.text == "＋").judge(R.drawable.javashop_fillet_gray_bg, R.drawable.javashop_fillet_gray_left_bg))
                        } else {
                            item.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                        }
                        item.setTextColor(getColorCompatible(R.color.javashop_color_nomal_color_gray))
                        address.tag = ""
                        /**如果该Item未选中 设置为已选中状态*/
                    } else {
                        /**如果Item是新增标签,设置单独的背景*/
                        if (item == member_address_edit_tag_new) {
                            item.setBackgroundResource((member_address_edit_tag_new.text == "＋").judge(R.drawable.javashop_fillet_red_bg, R.drawable.javashop_fillet_red_left_bg))
                        } else {
                            item.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
                        }
                        item.setTextColor(getColorCompatible(R.color.javashop_color_select_color_red))
                        address.tag = item.text.toString().trim()
                    }
                    item.isSelected = !item.isSelected
                } else {
                    /**如果是循环中其他的View,会全部设置成未选中状态*/
                    item.isSelected = false
                    if (item == member_address_edit_tag_new) {
                        item.setBackgroundResource((member_address_edit_tag_new.text == "＋").judge(R.drawable.javashop_fillet_gray_bg, R.drawable.javashop_fillet_gray_left_bg))
                    } else {
                        item.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                    }
                    item.setTextColor(getColorCompatible(R.color.javashop_color_nomal_color_gray))
                }
            }
        }

        /**更改标签事件*/
        val editContentListener = View.OnClickListener {
            member_address_edit_tag_new_et.visibility = View.VISIBLE
        }

        /**设置新增标签保存事件*/
        member_address_edit_tag_new_save.setOnClickListener {
            member_address_edit_tag_new_et.visibility = View.GONE
            member_address_edit_tag_new.text = member_address_edit_tag_new_et.text

            /**当新增标签不为空时 设置新增标签为选中状态*/
            if (member_address_edit_tag_new_et.text.toString().isNotEmpty()) {
                member_address_edit_new_edit.visibility = View.VISIBLE
                member_address_edit_tag_new.isSelected = false
                member_address_edit_tag_new.setOnClickListener(tagSelectedListener)
                tagSelectedListener.onClick(member_address_edit_tag_new)
            } else {
                /**当新增标签为空时 设置新增标签为未选中状态*/
                member_address_edit_new_edit.visibility = View.GONE
                member_address_edit_tag_new.text = "＋"
                if (member_address_edit_tag_new.isSelected) {
                    address.tag = ""
                }
                member_address_edit_tag_new.setBackgroundResource(R.drawable.javashop_fillet_gray_bg)
                member_address_edit_tag_new.setTextColor(getColorCompatible(R.color.javashop_color_nomal_color_gray))
                member_address_edit_tag_new.isSelected = false
                member_address_edit_tag_new.setOnClickListener(editContentListener)
            }
        }

        /**循环设置标签事件*/
        arrayOf(member_address_edit_tag_company, member_address_edit_tag_home, member_address_edit_tag_school, member_address_edit_tag_new).forEach {
            it.setOnClickListener(tagSelectedListener)
        }

        if (address.tag == "学校") {
            member_address_edit_tag_school.isSelected = true
            member_address_edit_tag_school.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            member_address_edit_tag_school.setTextColor(getColorCompatible(R.color.javashop_color_select_color_red))
            member_address_edit_tag_new.setOnClickListener(editContentListener)
        } else if (address.tag == "公司") {
            member_address_edit_tag_company.isSelected = true
            member_address_edit_tag_company.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            member_address_edit_tag_company.setTextColor(getColorCompatible(R.color.javashop_color_select_color_red))
            member_address_edit_tag_new.setOnClickListener(editContentListener)
        } else if (address.tag == "家") {
            member_address_edit_tag_home.isSelected = true
            member_address_edit_tag_home.setBackgroundResource(R.drawable.javashop_fillet_red_bg)
            member_address_edit_tag_home.setTextColor(getColorCompatible(R.color.javashop_color_select_color_red))
            member_address_edit_tag_new.setOnClickListener(editContentListener)
        } else {
            if (address.tag != "") {
                member_address_edit_tag_new.setOnClickListener(tagSelectedListener)
                member_address_edit_new_edit.visibility = View.VISIBLE
                member_address_edit_tag_new.isSelected = true
                member_address_edit_tag_new.setBackgroundResource(R.drawable.javashop_fillet_red_left_bg)
                member_address_edit_tag_new.text = address.tag
                member_address_edit_tag_new.setTextColor(getColorCompatible(R.color.javashop_color_select_color_red))
            }else{
                member_address_edit_tag_new.setOnClickListener(editContentListener)
            }
        }

        /**保存地址信息*/
        member_address_edit_save.setOnClickListener {
            if (address.nameObser.get() == "") {
                showMessage("请输入姓名")
                return@setOnClickListener
            }
            if (address.phoneNumObser.get() == "") {
                showMessage("请输入手机号")
                return@setOnClickListener
            }
            if (address.proId == -1 || address.cityId == -1 || address.counId == -1) {
                showMessage("地区选择不完整，最低三级！")
                return@setOnClickListener
            }
            if (address.addressDetailObser.get() == "") {
                showMessage("请输入详细地址")
                return@setOnClickListener
            }
            when (type) {
                ADDRESS_ADD -> {
                    presenter.addAddress(address)
                }
                ADDRESS_EDIT -> {
                    presenter.editAddress(address)
                }
            }
        }

        /**设置返回事件和删除事件*/
        member_address_edit_topbar.setLeftClickListener {
            pop()
        }.setRightClickListener {
            presenter.deleteAddress(address.id)
            pop()
        }

        /**设置地区选择事件*/
        val regionListener = View.OnClickListener {
            addressSelectorView.show()
        }

        /**地区事件设置*/
        member_address_edit_region_tv.setOnClickListener(regionListener)
        member_address_edit_region_header_tv.setOnClickListener(regionListener)
        member_address_edit_region_iv.setOnClickListener(regionListener)

        /**设置新增标签点击事件*/
        member_address_edit_new_edit.setOnClickListener(editContentListener)
    }
    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/9 上午9:22
     * @Note   渲染地区数据
     * @param  data 数据
     */
    override fun renderRegion(data: ArrayList<RegionViewModel>) {
        addressSelectorView.setData(data)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:55
     * @Note   错误回调
     * @param  message 错误信息
     */
    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:56
     * @Note   完成回调
     * @param  message 信息
     */
    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
        if (message == "操作完成" && isOrder){
            setResult(GlobalState.ORDER_CREATE_ADDRESS,intent)
            pop()
        }
        if (type == 1){
            pop()
            getEventCenter().post(AddressListDataChange())
        }
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:57
     * @Note   开始回调
     */
    override fun start() {
        showDialog()
    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:57
     * @Note   网络状态
     * @param  state 网络状态
     */
    override fun networkMonitor(state: NetState) {

    }

    /**
     * @author LDD
     * @From   MemberAddressEditActivity
     * @Date   2018/5/8 上午10:57
     * @Note   销毁
     */
    override fun destory() {

    }
}