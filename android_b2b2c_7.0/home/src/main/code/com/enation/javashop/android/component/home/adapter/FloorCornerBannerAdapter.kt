package com.enation.javashop.android.component.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.agreement.FloorActionAgreement
import com.enation.javashop.android.component.home.databinding.FloorCornerItemBannerLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseControl
import com.enation.javashop.android.lib.base.LIFE_CYCLE_PAUSE
import com.enation.javashop.android.lib.base.LIFE_CYCLE_RESUME
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.BannerModel
import com.enation.javashop.utils.base.tool.ScreenTool
import com.zhouwei.mzbanner.holder.MZViewHolder
import java.lang.ref.WeakReference

/**
 * 单个图片item
 */
class FloorCornerBannerAdapter(val lifeController : WeakReference<BaseControl>,val data : ArrayList<BannerModel>,val agreement: FloorActionAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<FloorCornerItemBannerLayBinding>,ArrayList<BannerModel>>() {

    private var render = false

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:26
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   item过滤
     * @param  position Item坐标
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<FloorCornerItemBannerLayBinding> {
        return BaseRecyclerViewHolder.build(parent,R.layout.floor_corner_item_banner_lay)
    }


    override fun getItemViewType(position: Int): Int {
        return 2
    }


    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:27
     * @Note   获取Item总数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:28
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1).then { self ->
            self.setMargin(5.dpToPx(),0,5.dpToPx(),5.dpToPx())
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FloorCornerItemBannerLayBinding>?, position: Int) {
        if (render){
            return
        }
        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.getScreenWidth(holder?.databinding?.root?.context!!)*0.3).toInt())
        holder?.bind {self ->
            render = true
            self.banner.setBannerPageClickListener { view, i ->
                agreement.event(data[i])
            }
            self.banner.setPages(data.toList(),{return@setPages BannerCornerViewHolder()})
            lifeController.get()?.addLifeCycleListener { state ->
                if (state == LIFE_CYCLE_PAUSE){
                    self.banner.pause()
                }else if (state == LIFE_CYCLE_RESUME){
                    self.banner.start()
                }
            }
            self.banner.start()
        }

    }
}

class BannerCornerViewHolder : MZViewHolder<BannerModel>{

    private var imageView : ImageView? = null

    override fun onBind(p0: Context, p1: Int, p2: BannerModel) {
        Glide.with(p0)
                .load(p2.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView)
    }

    override fun createView(p0: Context): View {
        var view = LayoutInflater.from(p0).inflate(R.layout.banner_corner_image_lay,null)
        imageView = view.findViewById(R.id.image)
        return view
    }

}