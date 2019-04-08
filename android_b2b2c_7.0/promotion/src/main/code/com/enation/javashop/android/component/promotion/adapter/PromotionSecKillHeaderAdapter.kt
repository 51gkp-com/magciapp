package com.enation.javashop.android.component.promotion.adapter

import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.enation.javashop.android.component.promotion.R
import com.enation.javashop.android.component.promotion.databinding.PromotionSeckillHeaderBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.base.BaseFragment
import com.enation.javashop.android.lib.base.LIFE_CYCLE_DESTORY
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.TimeEngine
import com.enation.javashop.android.middleware.model.SecKillHeaderViewModel
import java.lang.ref.WeakReference

/**
 * @author LDD
 * @Date   2018/5/21 下午2:36
 * @From   com.enation.javashop.android.component.promotion.adapter
 * @Note   秒杀头部
 */
class PromotionSecKillHeaderAdapter(val weakControl :WeakReference<BaseFragment<*,*>>, val data : SecKillHeaderViewModel, complete :()->Unit) : BaseDelegateAdapter<BaseRecyclerViewHolder<PromotionSeckillHeaderBinding>,SecKillHeaderViewModel>() {

    /**
     * @Name  timer
     * @Type  TimeEngine
     * @Note  倒计时引擎
     */
    private val timer by lazy {
        TimeEngine.build(data.time.toLong())
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
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:36
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }

    /**
     * @author LDD
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:36
     * @Note   点击过滤
     */
    override fun itemFilter(position: Int): Boolean {
        return true
    }

    /**
     * @author LDD
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:37
     * @Note   创建VM
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<PromotionSeckillHeaderBinding> {
        return BaseRecyclerViewHolder.build(parent, R.layout.promotion_seckill_header)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:37
     * @Note   item个数
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * @author LDD
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:37
     * @Note   创建LayoutHelper
     */
    override fun onCreateLayoutHelper(): LayoutHelper {
        return LinearLayoutHelper(0,1)
    }

    /**
     * @author LDD
     * @From   PromotionSecKillHeaderAdapter
     * @Date   2018/5/21 下午2:38
     * @Note   绑定ViewHolder
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<PromotionSeckillHeaderBinding>?, position: Int) {
        holder?.bind {
            binding ->
            binding.data = data
            timer.execute(call = { _, hour, min, sec ->
                data.hour.set(if (hour < 10){ "0$hour"  } else { "$hour" })
                data.min.set(if (min < 10){ "0$min"  } else { "$min" })
                data.sec.set(if (sec < 10){ "0$sec"  } else { "$sec" })
            })
            if (data.type == "当天24:00结束"){
                binding.promotionSeckillHeaderHourTv.visibility = View.GONE
                binding.promotionSeckillHeaderMinTv.visibility = View.GONE
                binding.promotionSeckillHeaderSecTv.visibility = View.GONE
                binding.promotionSeckillHeaderColonA.visibility = View.GONE
                binding.promotionSeckillHeaderColonB.visibility = View.GONE
            }else{
                binding.promotionSeckillHeaderHourTv.visibility = View.VISIBLE
                binding.promotionSeckillHeaderMinTv.visibility = View.VISIBLE
                binding.promotionSeckillHeaderSecTv.visibility = View.VISIBLE
                binding.promotionSeckillHeaderColonA.visibility = View.VISIBLE
                binding.promotionSeckillHeaderColonB.visibility = View.VISIBLE
            }
        }
    }
}