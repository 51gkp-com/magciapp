package com.enation.javashop.android.lib.utils

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enation.javashop.android.lib.base.BaseApplication
import com.enation.javashop.imagepluin.R
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.dataparser.concrete.Card
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator
import com.tmall.wireless.tangram.support.ExposureSupport
import com.tmall.wireless.tangram.support.SimpleClickSupport
import com.tmall.wireless.tangram.support.async.AsyncLoader
import com.tmall.wireless.tangram.support.async.AsyncPageLoader
import com.tmall.wireless.tangram.support.async.CardLoadSupport
import com.tmall.wireless.tangram.util.IInnerImageSetter
import org.json.JSONArray

/**
 * 七巧板辅助类
 */
class TangramPlugin :TangramIInter{

    private lateinit var builder:TangramBuilder.InnerBuilder

    private var engine:TangramEngine? = null

    companion object {
        fun initApplication(){
            TangramBuilder.init(BaseApplication.appContext,object : IInnerImageSetter {
                override fun <IMAGE : ImageView?> doLoadImageUrl(view: IMAGE, url: String?) {
                    if (view != null) {
                        Glide.with(view.context)
                                .load(url)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.image_loading)
                                .error(R.drawable.image_error)
                                .dontAnimate()
                                .dontTransform()
                                .into(view)
                    }
                }
            }, ImageView::class.java)
        }
        fun prepare(context :Activity):TangramPlugin{
            return TangramPlugin().build(context)
        }
    }

    private fun build(context :Activity):TangramPlugin{
        builder = TangramBuilder.newInnerBuilder(context)
        return this
    }

    override fun <T : Card> registerCard(type :Int, cardClz :Class<T> ):TangramPlugin{
        builder.registerCard(type,cardClz)
        return this
    }

    override fun <T : View> registerCell(type :Int, view :Class<T> ):TangramPlugin{
        builder.registerCell(type,view)
        return this
    }

    override fun <V :View,C:BaseCell<V>>registerCell(type:Int,cellCls:Class<C>,viewCls:Class<V>):TangramPlugin{
        builder.registerCell(type,cellCls,viewCls)
        return this
    }

    override fun <V :View,C:BaseCell<V>,H: ViewHolderCreator.ViewHolder>registerCell(type:Int,cellCls:Class<C>,viewHolderCreator: ViewHolderCreator<H,V>):TangramPlugin{
        builder.registerCell<V>(type,cellCls,viewHolderCreator)
        return this
    }

    override fun setLoadListener(asyncLoader: AsyncLoader, asyncPageLoader: AsyncPageLoader): TangramPlugin {
        initEngine()
        engine?.addCardLoadSupport(CardLoadSupport(asyncLoader,asyncPageLoader))
        return this
    }

    override fun getEngine(): TangramEngine {
        initEngine()

        return this!!.engine!!
    }

    override fun addClickSupport(support:SimpleClickSupport):TangramPlugin{
        initEngine()
        engine?.addSimpleClickSupport(support)
        return this
    }

    override fun addExposureSupport(support: ExposureSupport):TangramPlugin{
        initEngine()
        engine?.addExposureSupport(support)
        return this
    }

    override fun enableAutoLoadMore():TangramPlugin{
        initEngine()
        engine?.enableAutoLoadMore(true)
        return this
    }

    override fun bindRecyclerView(view:RecyclerView):TangramPlugin{
        initEngine()
        engine?.bindView(view)
        return this
    }

    override fun onScrolled():TangramPlugin{
        initEngine()
        engine?.contentView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                engine?.onScrolled()
            }
        })
        return this
    }

    override fun addDataSource(data :JSONArray):TangramPlugin{
        initEngine()
        engine?.setData(data)
        return this
    }
    override fun addDataSource(data :List<Card>):TangramPlugin{
        initEngine()
        engine?.setData(data)
        return this
    }

    override fun destory() {
        engine?.destroy()
    }

    private fun initEngine(){
        engine.haventDo {
            engine = builder.build()
        }
    }
}

interface TangramIInter{
    fun <T : Card> registerCard(type :Int, cardClz :Class<T> ):TangramPlugin
    fun <T : View> registerCell(type :Int, view :Class<T> ):TangramPlugin
    fun <V :View,C:BaseCell<V>>registerCell(type:Int,cellCls:Class<C>,viewCls:Class<V>):TangramPlugin
    fun <V :View,C:BaseCell<V>,H: ViewHolderCreator.ViewHolder>registerCell(type:Int,cellCls:Class<C>,viewHolderCreator: ViewHolderCreator<H,V>):TangramPlugin
    fun setLoadListener(asyncLoader: AsyncLoader,asyncPageLoader: AsyncPageLoader):TangramPlugin
    fun getEngine ():TangramEngine
    fun addClickSupport(support:SimpleClickSupport):TangramPlugin
    fun addExposureSupport(support: ExposureSupport):TangramPlugin
    fun enableAutoLoadMore():TangramPlugin
    fun bindRecyclerView(view:RecyclerView):TangramPlugin
    fun onScrolled():TangramPlugin
    fun addDataSource(data :JSONArray):TangramPlugin
    fun addDataSource(data :List<Card>):TangramPlugin
    fun destory()
}