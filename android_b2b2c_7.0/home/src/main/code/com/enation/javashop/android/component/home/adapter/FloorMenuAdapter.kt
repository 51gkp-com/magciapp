package com.enation.javashop.android.component.home.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.enation.javashop.android.component.home.R
import com.enation.javashop.android.component.home.agreement.FloorActionAgreement
import com.enation.javashop.android.component.home.databinding.FloorItemMenuLayBinding
import com.enation.javashop.android.component.home.databinding.FloorItemSingleImageLayBinding
import com.enation.javashop.android.lib.adapter.BaseDelegateAdapter
import com.enation.javashop.android.lib.utils.BaseRecyclerViewHolder
import com.enation.javashop.android.lib.utils.dpToPx
import com.enation.javashop.android.lib.utils.then
import com.enation.javashop.android.middleware.model.FloorItem
import com.enation.javashop.android.middleware.model.FloorMenuModel
import com.enation.javashop.android.middleware.model.FloorViewModel
import com.enation.javashop.imagepluin.utils.GlideUtils
import com.enation.javashop.utils.base.tool.ScreenTool

/**
 * 单个图片item
 */
class FloorMenuAdapter(val data : ArrayList<FloorMenuModel>,val agreement: FloorActionAgreement) :BaseDelegateAdapter<BaseRecyclerViewHolder<FloorItemMenuLayBinding>,FloorItem>() {

    /**
     * @author LDD
     * @From   SingleImageAdapter
     * @Date   2018/8/16 下午5:26
     * @Note   数据提供者
     */
    override fun dataProvider(): Any {
        return data
    }


    override fun getItemViewType(position: Int): Int {
        return 7
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
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<FloorItemMenuLayBinding> {
        return BaseRecyclerViewHolder.build(parent,R.layout.floor_item_menu_lay)
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
            self.setMargin(5.dpToPx(),5.dpToPx(),5.dpToPx(),5.dpToPx())
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<FloorItemMenuLayBinding>?, position: Int) {

        val isSingle = data.size <= 5
        val viewHeight = (ScreenTool.getScreenWidth(holder?.databinding?.root?.context!!)*0.4).toInt()
        val height = if(isSingle) viewHeight/2 else viewHeight

        holder?.itemView?.layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        holder?.bind { self ->
            self.root.setBackgroundResource(R.drawable.javashop_corners_common)
            self.menuB.removeAllViews()
            self.menuA.removeAllViews()
            data.forEachIndexed { index, item ->
                if (index > 4){
                    self.menuB.addView(createMenuItem(self.root.context,item))
                }else{
                    self.menuA.addView(createMenuItem(self.root.context,item))
                }
            }
            if(isSingle){
                self.menuB.visibility = View.GONE
            } else {
                self.menuB.visibility = View.VISIBLE
            }
        }
    }


    fun createMenuItem(context: Context,menuItem :FloorMenuModel) :View{

        val parent = LinearLayout(context)
        parent.orientation = LinearLayout.VERTICAL
        parent.gravity = Gravity.CENTER
        val itemSize :Int = (ScreenTool.getScreenWidth(context)/5).toInt() - 2.dpToPx()

        val layoutParams = LinearLayout.LayoutParams(itemSize,itemSize).then { self ->
            self.gravity = Gravity.CENTER
        }

        parent.layoutParams = layoutParams


        val image = ImageView(context).then { self ->
            self.scaleType = ImageView.ScaleType.FIT_XY
            self.layoutParams = LinearLayout.LayoutParams((itemSize*0.6).toInt(),(itemSize*0.6).toInt())
        }

        val tv = TextView(context).then { self ->
            self.layoutParams = LinearLayout.LayoutParams(itemSize,(itemSize*0.3).toInt())
            self.gravity = Gravity.CENTER
            self.setTextColor(Color.parseColor("#474747"))
            self.setTextSize(TypedValue.COMPLEX_UNIT_PX, (itemSize*0.15).toFloat())
        }

        parent.addView(image)
        parent.addView(tv)

        loadImage(menuItem.image,image)
        tv.text = menuItem.text

        agreement.floorHandle(parent,menuItem)
        return parent

    }

}