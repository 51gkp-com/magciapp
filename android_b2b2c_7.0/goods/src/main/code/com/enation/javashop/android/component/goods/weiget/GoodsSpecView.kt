package com.enation.javashop.android.component.goods.weiget

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.enation.javashop.android.component.goods.R
import com.enation.javashop.android.component.goods.databinding.GoodsInfoSpecPopBinding
import com.enation.javashop.android.lib.core.runtime.JavaShopActivityTask
import com.enation.javashop.android.lib.utils.*
import com.enation.javashop.android.lib.widget.PopWindowCompatible
import com.enation.javashop.android.middleware.model.SkuGoods
import com.enation.javashop.android.middleware.model.Spec
import com.enation.javashop.utils.base.tool.ScreenTool
import com.library.flowlayout.FlowLayoutManager
import com.library.flowlayout.SpaceItemDecoration
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 * @author LDD
 * @Date   2018/5/31 下午6:48
 * @From   com.enation.javashop.android.component.goods.weiget
 * @Note   规格视图
 */
class GoodsSpecView : PopWindowCompatible {

    /**
     * @Name  activity
     * @Type  Activity
     * @Note  页面
     */
    private var activity : Activity

    /**
     * @author LDD
     * @From   GoodsFirstMoreView
     * @Date   2018/4/4 上午10:45
     * @Note   数据绑定
     */
    lateinit var binding : GoodsInfoSpecPopBinding


    var skuList = ArrayList<SkuGoods>()

    var num = 1

    var specList = ArrayList<Any>()

    lateinit var adapter : FlowAdapter

    /**
     * @Name  confirmObserver
     * @Type  ((GoodsFilterValue?)->Unit)?
     * @Note  确定监听
     */
    var skuSelectObserver :((SkuGoods?,Int)->Unit)? = null

    /**
     * @Name  confirmObserver
     * @Type  ((GoodsFilterValue?)->Unit)?
     * @Note  确定监听
     */
    var addCartObserver :((SkuGoods,Int)->Unit)? = null



    /**
     * @Name  animIsEnd
     * @Type  Boolean
     * @Note  动画是否结束
     */
    private var animIsEnd = true

    /**
     * @author LDD
     * @Date   2018/4/19 下午3:20
     * @From   GoodsSpecView
     * @Note   伴生对象
     */
    companion object {

        fun getHeight() :Int{
            var dis  = DisplayMetrics()
            JavaShopActivityTask.instance.peekTopActivity()!!.windowManager.defaultDisplay.getRealMetrics(dis)
            return dis.heightPixels - ScreenTool.getVirtualBarHeigh(JavaShopActivityTask.instance.peekTopActivity() as AppCompatActivity?)
        }
        /**
         * @author LDD
         * @From   GoodsSpecView
         * @Date   2018/4/19 下午3:21
         * @Note   静态构建
         * @param  activity 页面
         */
        fun build(activity: Activity,skus :ArrayList<SkuGoods>,specs :ArrayList<Any>) : GoodsSpecView {
            return GoodsSpecView(activity,skus,specs)
        }
    }

    /**构造方法*/
    private constructor(activity: Activity,skus :ArrayList<SkuGoods>,specs :ArrayList<Any>): super(ScreenTool.getScreenWidth(activity).toInt(), (GoodsSpecView.getHeight() - AppTool.SystemUI.getStatusBarHeight())){
        this.activity = activity
        this.skuList = skus
        this.specList = specs
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        createUI()


    }

    private fun getSelectedSpecs() : String{
        var spec = ArrayList<Spec>()
        specList.forEach { item ->
                if (item is Spec){
                    if ((item as Spec).select){
                        spec.add(item as Spec)
                    }
                }
        }
        return SkuGoods.identifier(spec)
    }

    fun getSelectSku() : SkuGoods{
        if (skuList.count() == 1){
            return skuList[0]
        }else{
            for (i in 0..(skuList.count() - 1)){
                if (skuList[i].identifier == getSelectedSpecs()) {
                    return skuList[i]
                }
            }
        }
        return skuList[0]
    }

    fun initInfo(){
        skuList.forEach { item ->
                if (item.identifier == getSelectedSpecs()) {
                    binding.goodsInfoSpecPopPriceTv.text = String.format("￥%.2f", item.price)
                    binding.goodsInfoSpecPopNumTv.text = "商品编号:${item.sn}"
                    var imageUrl = item.image
                    for (spec in item.specList) {
                        if (spec.specImage.isNotEmpty()){
                            imageUrl = spec.specImage
                        }
                    }
                    Glide.with(activity).load(imageUrl).centerCrop().bitmapTransform(RoundedCornersTransformation(activity,5.dpToPx(),2.dpToPx())).into(binding.goodsInfoSpecPopIv)
                    skuSelectObserver?.invoke(item, num)
                }
        }
    }

    /**
     * @author LDD
     * @From   GoodsSpecView
     * @Date   2018/4/19 下午3:21
     * @Note   构建UI
     */
    private fun createUI(){
        contentView = activity.layoutInflater.inflate(R.layout.goods_info_spec_pop,null)
        binding = DataBindingUtil.bind(contentView)
        val bg = GradientDrawable().then {
            self ->
            self.cornerRadius = 5.dpToPx().toFloat()
            self.setColor(Color.WHITE)
            self.setStroke(1, Color.parseColor("#d8d7dc"))
        }
        binding.goodsInfoSpecPopIv.background = bg
        binding.goodsInfoSpecPopBg.setOnTouchListener{ _ , _ -> true }
        Glide.with(activity).load(skuList[0].image).bitmapTransform(CropSquareTransformation(activity), RoundedCornersTransformation(activity,5.dpToPx(),1.dpToPx())).into(binding.goodsInfoSpecPopIv)
        binding.goodsInfoSpecPopPriceTv.text = String.format("￥%.2f",skuList[0].price)
        binding.goodsInfoSpecPopNumTv.text = "商品编号:${skuList[0].sn}"
        binding.goodsInfoSpecPopMask.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN ){
                dismiss()
            }
            return@setOnTouchListener true
        }

        binding.goodsInfoSpecPopClose.setOnClickListener(OnClickListenerAntiViolence({
            dismiss()
        }))
        binding.goodsInfoSpecPopAddCart.setOnClickListener {
            addCartObserver?.invoke(getSelectSku(),num)
        }
        val flowLayoutManager = FlowLayoutManager()
        adapter = FlowAdapter(this,activity,specList)
        binding.goodsInfoSpecPopRv.addItemDecoration(SpaceItemDecoration(5.dpToPx()))
        binding.goodsInfoSpecPopRv.layoutManager = flowLayoutManager
        binding.goodsInfoSpecPopRv.adapter = adapter
    }

   fun setSkuSelectObserver(obser :(SkuGoods?,Int) ->(Unit)) :GoodsSpecView{
       skuSelectObserver = obser
       return this
   }

    fun setAddCart(obser :(SkuGoods,Int) ->(Unit)) :GoodsSpecView{
        addCartObserver = obser
        return this
    }

    /**
     * @author LDD
     * @From   GoodsSpecView
     * @Date   2018/4/19 下午3:25
     * @Note   显示与该View之下
     * @param  atView view
     */
    fun show(atView : View){
        /**当动画未执行完毕 又或者筛选条件数量为0时 不显示*/
        if (!animIsEnd){
            return
        }

        /**mask先隐藏掉*/
        binding.goodsInfoSpecPopMask.visibility = View.GONE

        /**将该View加载到页面之上*/
        showAsDropDown(atView)

        /**执行动画 起始y点为 -View.height 减去 Recly.height -button.height  */
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopInAnimation(activity, height.toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画结束 设置动画标记结束 显示Mask*/
            if (state == 2){
                animIsEnd = true
                binding.goodsInfoSpecPopMask.visibility = View.VISIBLE
            }

            /**动画开始 重置动画标记*/
            if (state == 3){
                animIsEnd = false
            }

            /**当返回true时 代表拦截当前动画 不再往下执行*/
            return@animSequentialStart false
        })
    }

    /**
     * @author LDD
     * @From   GoodsSpecView
     * @Date   2018/4/19 下午3:26
     * @Note   销毁退出
     */
    override fun dismiss() {

        /**动画未执行完毕 不执行下方代码*/
        if (!animIsEnd){
            return
        }

        /**隐藏Mask*/
        binding.goodsInfoSpecPopMask.visibility = View.GONE

        /**执行动画 终点y轴为 -View.height*/
        contentView.animSequentialStart(arrayListOf(AppTool.Anim.createPopOutAnimation(activity, height.toFloat(),300)),interceptor = {
            index: Int, state: Int ->

            /**动画执行完毕 设置标记 隐藏View*/
            if (state == 2){
                animIsEnd = true
                super.dismiss()
            }

            /**动画开始执行 还原标记*/
            if (state == 3){
                animIsEnd = false
            }

            /**当返回true时 代表拦截当前动画 不再往下执行*/
            return@animSequentialStart false
        })
    }

}


class FlowAdapter(val view :GoodsSpecView,val context :Context,private val list: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SPEC = 1

    private val TITLE = 2

    private val NUM = 3

    override fun getItemViewType(position: Int): Int {

        val item = list[position]

        if (item is Spec){
            return SPEC
        }else if (item is String) {
            return TITLE
        }else {
            return NUM
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SPEC){
            SpecHolder(View.inflate(context, R.layout.goods_spec_item_lay, null))
        }else if (viewType == TITLE){
            TitleHolder(View.inflate(context, R.layout.goods_spec_item_title_lay, null))
        }else{
            NumHolder(View.inflate(context, R.layout.goods_spec_num_item_lay, null))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SpecHolder){
            if ((list[position] as Spec).select){
                holder.text.setTextColor(Color.parseColor("#ED2219"))
                holder.text.setBackgroundResource(R.drawable.javashop_goods_spec_select)
            }else{
                holder.text.setTextColor(Color.parseColor("#2b2b2b"))
                holder.text.setBackgroundResource(R.drawable.javashop_goods_spec_nomal)
            }
            holder.text.text = (list[position] as Spec).specValue
            holder.text.setOnClickListener {
                if (list[position] is Spec && !(list[position] as Spec).select) {
                    val selectSpec = list[position] as Spec
                            val selectParentId = selectSpec.specId
                            val selectValueId = selectSpec.specValueId
                            for (index in 0..(list.count() - 1)){
                    if (list[index] is Spec){
                        var item = list[index] as Spec
                                if (item.specId == selectParentId){
                                    item.select = (item.specValueId == selectValueId)
                                    list[index] = item
                                }
                    }
                }
                    view.initInfo()
                    view.adapter.notifyDataSetChanged()
                }
            }
        }else if(holder is NumHolder){
            holder.num.text = "${list[position] as Int}"
            holder.add.setOnClickListener {
                list[position] = list[position] as Int + 1
                holder.num.text = "${list[position] as Int}"
                view.num = list[position] as Int
                view.initInfo()
            }
            holder.reduce.setOnClickListener {
                if((list[position] as Int - 1) >= 1){
                    list[position] = list[position] as Int - 1
                }else{
                    return@setOnClickListener
                }
                holder.num.text = "${list[position] as Int}"
                view.num = list[position] as Int
                view.initInfo()
            }

        }else if(holder is TitleHolder) {
            holder.text.text = list[position] as String
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal inner class SpecHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text: TextView

        init {
            text = itemView.findViewById(R.id.text) as TextView
        }
    }

    internal inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text: TextView

        init {
            text = itemView.findViewById(R.id.text) as TextView
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,30.dpToPx())
            itemView.layoutParams = layoutParams
        }
    }

    internal inner class NumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reduce: TextView

        val add: TextView

        val num: TextView


        init {
            reduce = itemView.findViewById(R.id.reduce) as TextView
            add = itemView.findViewById(R.id.add) as TextView
            num = itemView.findViewById(R.id.num) as TextView
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,30.dpToPx())
            itemView.layoutParams = layoutParams
        }
    }

}