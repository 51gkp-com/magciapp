package com.enation.javashop.android.component.goods.adapter

import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsInfoGalleryItemBinding
import com.enation.javashop.android.component.goods.fragment.GoodsInfoFragment
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.GoodsGallery
import com.github.ielse.imagewatcher.ImageWatcher
import com.github.ielse.imagewatcher.ImageWatcherHelper
import java.lang.ref.WeakReference
import java.net.URI
import com.alipay.android.phone.mrpc.core.v
import com.enation.javashop.android.component.goods.activity.GoodsActivity


/**
 * @author LDD
 * @Date   2018/3/30 下午4:22
 * @From   com.enation.javashop.android.component.goods.adapter
 * @Note   商品相册适配器
 */
class GoodsInfoGalleryAdapter(val fragment:WeakReference<GoodsInfoFragment>,var data:ArrayList<GoodsGallery>) :BaseDelegateAdapter<BaseRecyclerViewHolder<GoodsInfoGalleryItemBinding>,ArrayList<GoodsGallery>>() {

    /**
     * @Name  initFlag
     * @Type  Boolean
     * @Note  是否已经初始化
     */
    private var initFlag = false

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:38
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:39
     * @Note   item点击过滤
     * @param  position 坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:39
     * @Note   创建Viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<GoodsInfoGalleryItemBinding> {
        return BaseRecyclerViewHolder(DataBindingUtil.bind(LayoutInflater.from(parent?.context).inflate(R.layout.goods_info_gallery_item,parent,false)))
    }

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:39
     * @Note   获取item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:41
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   GoodsInfoGalleryAdapter
     * @Date   2018/4/8 下午1:41
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<GoodsInfoGalleryItemBinding>?, position: Int) {
        holder?.bind {
            binding ->
            if (data.size == 0 || initFlag){
                return@bind
            }
            GalleryHelper.build<GoodsGallery>(binding.goodsInfoGallery.weak())
                    .setGallery(data)
                    .setDataTransform { orgData ->
                        orgData.big
                    }.setItemCallBack {
                        position , imageView ->
                        binding.root.context.to<GoodsActivity>().showGallay(data.map { Uri.parse(it.big) },position)
                    }.setScrollCallBack {
                        position ->
                        binding.goodsInfoGalleryIndexTv.text = "${position+1}/${data.size}"
                    }.autoScroll().then {
                        fragment.get()?.addLifeCycleListener {
                            state ->
                            if (state == LIFE_CYCLE_DESTORY){
                                it.offAutoScroll()
                            }
                        }
                    }.execute()
            initFlag = true
        }
    }
}