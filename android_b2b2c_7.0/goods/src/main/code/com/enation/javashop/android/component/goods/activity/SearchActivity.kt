package com.enation.javashop.android.component.goods.activity

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.adapter.*
import com.enation.javashop.android.component.goods.agreement.HistoryAgreement
import com.enation.javashop.android.component.goods.databinding.SearchAcyLayBinding
import com.enation.javashop.android.component.goods.launch.GoodsLaunch
import com.enation.javashop.android.jrouter.external.annotation.Router
import com.enation.javashop.android.lib.base.BaseActivity
import com.enation.javashop.android.lib.utils.AppTool
import com.enation.javashop.android.lib.utils.pop
import com.enation.javashop.android.lib.utils.push
import com.enation.javashop.android.lib.utils.showMessage
import com.enation.javashop.android.lib.vo.filter
import com.enation.javashop.android.middleware.logic.contract.goods.SearchContract
import com.enation.javashop.android.middleware.logic.presenter.goods.SearchPresenter
import com.enation.javashop.android.middleware.model.HistoryModel
import com.enation.javashop.net.engine.model.NetState
import kotlinx.android.synthetic.main.search_acy_lay.*

@Router(path = "/search/main")
class SearchActivity : BaseActivity<SearchPresenter, SearchAcyLayBinding>(), SearchContract.View,TextWatcher,HistoryAgreement,HotKeyAgreement {

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
     * @Name  adapters
     * @Type  ArrayList<DelegateAdapter.Adapter<*>>
     * @Note  适配器列表
     */
    private var adapters = ArrayList<DelegateAdapter.Adapter<*>>()



    override fun getLayId(): Int {
        return R.layout.search_acy_lay
    }

    override fun bindDagger() {
        GoodsLaunch.component.inject(this)
    }

    override fun init() {
        AppTool.SystemUI.ImmersiveWithBottomBarColor(this, Color.BLACK)
        /**初始化LayoutMannager*/
        virtualLayoutManager = VirtualLayoutManager(this.activity)

        /**初始化适配器*/
        delegateAdapter = DelegateAdapter(virtualLayoutManager)
        /**配置到RecycleView*/
        search_list.layoutManager = virtualLayoutManager
        search_list.adapter = delegateAdapter
        presenter.loadData()
    }

    override fun bindEvent() {

        search_back_tv.setOnClickListener {
            pop()
        }

        search_input_tv.addTextChangedListener(this)

        search_input_tv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                if(search_input_tv.text.isNotEmpty()){
                    push("/goods/list",{
                        it.withString("keyword",search_input_tv.text.toString())
                        it.withString("hint",search_input_tv.text.toString())
                    })
                    search_shop_tv.text = ""
                    search_shop_lay.visibility = View.GONE
                    presenter.addHistory(HistoryModel(true,search_input_tv.text.toString()))
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener true
            }
           return@setOnEditorActionListener false
        }

        search_shop_tv.setOnClickListener {
            if(search_input_tv.text.isNotEmpty()){
                push("/shop/list",{
                    it.withString("keyword",search_input_tv.text.toString())
                })
                search_shop_tv.text = ""
                search_shop_lay.visibility = View.GONE
                presenter.addHistory(HistoryModel(false,search_input_tv.text.toString()))
            }
        }
    }

    override fun destory() {

    }

    override fun render(data: ArrayList<Any>) {
        adapters.clear()
        data.forEachIndexed { index, item ->
            if (index == 0){
                adapters.add(SearchHotKeyAdapter(item as ArrayList<String>,this))
            }else{
                when(item){
                    is ArrayList<*> ->{
                        adapters.add(SearchHistoryAdapter(item as ArrayList<HistoryModel>,this))
                    }
                    is Int ->{
                        if (item == 0){
                            adapters.add(SearchHistoryHeaderAdapter())
                        }else{
                            adapters.add(SearchHistoryClearAdapter(this))
                        }
                    }
                }
            }
        }
        delegateAdapter.clear()
        delegateAdapter.addAdapters(adapters)
        delegateAdapter.notifyDataSetChanged()
    }

    override fun delete(item: HistoryModel) {
        presenter.deleteHistory(item)
    }

    override fun add(item: HistoryModel) {
        if (item.searchMode){
            push("/goods/list",{
                it.withString("keyword",item.text)
                it.withString("hint",item.text)
            })
        }else{
            push("/shop/list",{
                it.withString("keyword",item.text)
            })
        }
        presenter.addHistory(item)
    }

    override fun clear() {
        presenter.clearHistory()
    }

    override fun hotkeySelect(key: String) {
        push("/goods/list",{
            it.withString("keyword",key)
            it.withString("hint",key)
        })
        presenter.addHistory(HistoryModel(true,key))
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (search_input_tv.text.count()>0){
            search_shop_lay.visibility = View.VISIBLE
            search_shop_tv.text = "搜索\"${search_input_tv.text}\"店铺"
        }else{
            search_shop_tv.text = ""
            search_shop_lay.visibility = View.GONE
        }
    }

    override fun onError(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun complete(message: String, type: Int) {
        dismissDialog()
        showMessage(message)
    }

    override fun start() {
        showDialog()
    }

    override fun networkMonitor(state: NetState) {
        state.filter(onWifi = {

        },onMobile = {

        },offline ={

        })
    }
}