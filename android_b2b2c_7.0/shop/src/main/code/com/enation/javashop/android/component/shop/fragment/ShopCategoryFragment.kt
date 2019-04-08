package com.enation.javashop.android.component.shop.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.shop.R
import com.enation.javashop.android.component.shop.R.id.shop_category_frag_rv
import com.enation.javashop.android.component.shop.adapter.ShopCategoryAdapter
import com.enation.javashop.android.component.shop.agreement.ShopCategoryAgreement
import com.enation.javashop.android.component.shop.databinding.ShopCategoryFragLayBinding
import com.enation.javashop.android.lib.utils.more
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.ShopCategoryViewModel
import kotlinx.android.synthetic.main.shop_category_frag_lay.*

/**
 * @author LDD
 * @Date   2018/4/12 上午9:58
 * @From   com.enation.javashop.android.component.shop.fragment
 * @Note   店铺
 */
class ShopCategoryFragment :Fragment() {

    /**
     * @Name  virtualLayoutManager
     * @Type  VirtualLayoutManager
     * @Note  VLayoutManager
     */
    private lateinit var virtualLayoutManager: VirtualLayoutManager

    /**
     * @Name  delegateAdapter
     * @Type  DelegateAdapter
     * @Note  七巧板适配器
     */
    private lateinit var delegateAdapter: DelegateAdapter

    /**
     * @Name  data
     * @Type  ArrayList<ShopCategoryViewModel>
     * @Note  分类数据
     */
    var data :ArrayList<ShopCategoryViewModel>? = null

    /**
     * @Name  binding
     * @Type  ShopCategoryFragLayBinding
     * @Note  试图绑定
     */
    private val binding by lazy {
        DataBindingUtil.bind<ShopCategoryFragLayBinding>(layoutInflater.inflate(R.layout.shop_category_frag_lay,null))
    }

    /**
     * @author LDD
     * @From   ShopCategoryFragment
     * @Date   2018/4/24 下午3:18
     * @Note   创建视图
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return binding.root
    }

    /**
     * @author LDD
     * @From   ShopCategoryFragment
     * @Date   2018/4/24 下午3:18
     * @Note   创建视图完毕
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    /**
     * @author LDD
     * @From   ShopCategoryFragment
     * @Date   2018/4/24 下午3:18
     * @Note   绑定视图信息
     */
    fun bind(){
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)

        data?.more {
            self ->
            delegateAdapter.addAdapter(ShopCategoryAdapter(self).then {
                self ->
                self.setOnItemClickListener { data, position ->
                    if (data.child == null || data.child?.size == 0){
                        (activity as ShopCategoryAgreement).pushActivity(data)
                    }else{
                        (activity as ShopCategoryAgreement).pushFragment(data.child!!)
                    }
                }
            })
        }
        /**配置到RecycleView*/
        shop_category_frag_rv.layoutManager = virtualLayoutManager
        shop_category_frag_rv.adapter = delegateAdapter

    }
}