package com.enation.javashop.android.component.home.adapter

import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StrikethroughSpan
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.databinding.FloorItemSeckillLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.middleware.model.GoodsItemViewModel
import com.enation.javashop.android.middleware.model.SecKillListViewModel
import com.enation.javashop.utils.base.tool.BaseToolActivity
import com.enation.javashop.utils.base.tool.ScreenTool
import java.lang.ref.WeakReference
import java.util.*

/**
 * 单个图片item
 */
class FloorSecKillAdapter(val weakControl :WeakReference<BaseFragment<*, *>>, complete :()->Unit,val data : ArrayList<GoodsItemViewModel>, val time :SecKillListViewModel , val next :SecKillListViewModel?) :BaseDelegateAdapter<BaseRecyclerViewHolder<FloorItemSeckillLayBinding>,ArrayList<GoodsItemViewModel>>() {

    /**
     * @Name  timer
     * @Type  TimeEngine
     * @Note  倒计时引擎
     */
    var timer :TimeEngine  = TimeEngine.build(if(next == null){((TimeHandle.getCurrentDay24Mill() - System.currentTimeMillis()) / 1000)}else{next.distanceTime.toLong()}).needSingleProcess()


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        timer.destory()
    }


    override fun getItemViewType(position: Int): Int {
        return 9
    }


    /**初始化*/
    init {

        timer.setComplete(complete)

        weakControl.get()?.addLifeCycleListener {
            state ->
            if (state == LIFE_CYCLE_DESTORY){
                timer.destory()
            }
        }

    }

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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<FloorItemSeckillLayBinding> {
        return BaseRecyclerViewHolder.build(parent,R.layout.floor_item_seckill_lay)
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

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FloorItemSeckillLayBinding>?, position: Int) {
        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenTool.getScreenWidth(holder?.databinding?.root?.context!!)*0.4).toInt())
        holder?.bind { self ->
            self.root.setOnClickListener {
                self.root.context.to<BaseToolActivity>().push("/promotion/seckill/main")
            }
                timer.execute(call = { _, hour, min, sec ->
                    weakControl.get()!!.activity.runOnUiThread {
                        self.hourTv.text = (if (hour < 10){ "0$hour"  } else { "$hour" })
                        self.minTv.text = (if (min < 10){ "0$min"  } else { "$min" })
                        self.secTv.text = (if (sec < 10){ "0$sec"  } else { "$sec" })
                    }
                })

           self.title.text = String.format("%2d点场",time.text.toInt()!!)
            if (data.getOrNull(0) != null){
                self.goods1.setOnClickListener {
                    self.root.context.to<BaseToolActivity>(). push("/goods/detail",{
                        it.withInt("goodsId",data[0].goodsId)
                    })
                }
                self.goods1.visable()
                self.orgPrice1.text = SpannableStringBuilder(String.format("￥%.0f",data[0].orginPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(5.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    self.setSpan(StrikethroughSpan(), 0, String.format("￥%.0f",data[0].orginPrice).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                self.price1.text = SpannableStringBuilder(String.format("￥%.0f",data[0].goodsPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(8.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                loadImage(data[0].goodsImage,self.image1)
            }else{
                self.goods1.invisable()
            }
            if (data.getOrNull(1) != null){
                self.goods2.visable()
                self.goods2.setOnClickListener {
                    self.root.context.to<BaseToolActivity>(). push("/goods/detail",{
                        it.withInt("goodsId",data[1].goodsId)
                    })
                }
                self.orgPrice2.text = SpannableStringBuilder(String.format("￥%.0f",data[1].orginPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(5.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    self.setSpan(StrikethroughSpan(), 0, String.format("￥%.0f",data[1].orginPrice).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                self.price2.text = SpannableStringBuilder(String.format("￥%.0f",data[1].goodsPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(8.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                loadImage(data[1].goodsImage,self.image2)
            }else{
                self.goods2.invisable()
            }
            if (data.getOrNull(2) != null){
                self.goods3.visable()
                self.goods3.setOnClickListener {
                    self.root.context.to<BaseToolActivity>(). push("/goods/detail",{
                        it.withInt("goodsId",data[2].goodsId)
                    })
                }
                self.orgPrice3.text = SpannableStringBuilder(String.format("￥%.0f",data[2].orginPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(5.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    self.setSpan(StrikethroughSpan(), 0, String.format("￥%.0f",data[2].orginPrice).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                self.price3.text = SpannableStringBuilder(String.format("￥%.0f",data[2].goodsPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(8.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                loadImage(data[2].goodsImage,self.image3)
            }else{
                self.goods3.invisable()
            }
            if (data.getOrNull(3) != null){
                self.goods4.visable()
                self.goods4.setOnClickListener {
                    self.root.context.to<BaseToolActivity>(). push("/goods/detail",{
                        it.withInt("goodsId",data[3].goodsId)
                    })
                }
                self.orgPrice4.text = SpannableStringBuilder(String.format("￥%.0f",data[3].orginPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(5.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    self.setSpan(StrikethroughSpan(), 0, String.format("￥%.0f",data[3].orginPrice).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                self.price4.text = SpannableStringBuilder(String.format("￥%.0f",data[3].goodsPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(8.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                loadImage(data[3].goodsImage,self.image4)
            }else{
                self.goods4.invisable()
            }
            if (data.getOrNull(4) != null){
                self.goods5.visable()
                self.goods5.setOnClickListener {
                    self.root.context.to<BaseToolActivity>(). push("/goods/detail",{
                        it.withInt("goodsId",data[4].goodsId)
                    })
                }
                self.orgPrice5.text = SpannableStringBuilder(String.format("￥%.0f",data[4].orginPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(5.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    self.setSpan(StrikethroughSpan(), 0, String.format("￥%.0f",data[4].orginPrice).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                self.price5.text = SpannableStringBuilder(String.format("￥%.0f",data[4].goodsPrice)).then { self ->
                    self.setSpan(AbsoluteSizeSpan(8.dpToPx()), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                loadImage(data[4].goodsImage,self.image5)
            }else{
                self.goods5.invisable()
            }
        }
    }
}