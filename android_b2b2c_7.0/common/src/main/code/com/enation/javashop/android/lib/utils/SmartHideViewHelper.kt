package com.enation.javashop.android.lib.utils

import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener

/**
 * @author LDD
 * @From   com.enation.javashop.android.component.home.utils
 * @Date   2018/1/30 下午3:06
 * @Note   首页上滑topbar变幻辅助类
 */

class SmartHideViewHelper constructor(val refreshScrollStart: () -> Unit, val refreshEnd: () -> Unit) : OnMultiPurposeListener{

    /**
     * @Name  headerReleasing
     * @Type  Boolean
     * @Note  是否刷新
     */
    private  var headerReleasing =true


    override fun onFooterMoving(footer: RefreshFooter?, isDragging: Boolean, percent: Float, offset: Int, footerHeight: Int, maxDragHeight: Int) {
        refreshEnd.invoke()
    }

    override fun onHeaderStartAnimator(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {

    }

    override fun onFooterReleased(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {
        refreshEnd.invoke()
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        if (!headerReleasing){
            refreshEnd.invoke()
        }
    }

    override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
        headerReleasing = true
    }

    override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {

    }

    override fun onFooterStartAnimator(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onHeaderReleased(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {
        refreshScrollStart.invoke()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {

    }
}

//class SmartHideViewHelper : OnMultiPurposeListener {
//
//    override fun onFooterReleased(p0: RefreshFooter?, p1: Int, p2: Int) {
//
//    }
//
//    override fun onHeaderReleased(p0: RefreshHeader?, p1: Int, p2: Int) {
//
//    }
//
//    override fun onLoadMore(p0: RefreshLayout?) {
//
//    }
//
//    /**
//     * @Name  headerReleasing
//     * @Type  Boolean
//     * @Note  是否刷新
//     */
//    private  var headerReleasing =true
//
//    /**
//     * @Name  refreshScrollStart
//     * @Type  block
//     * @Note  开始刷新回调
//     */
//    private  var refreshScrollStart :()->(Unit)
//
//    /**
//     * @Name  refreshEnd
//     * @Type  block
//     * @Note  刷新结束回调
//     */
//    private  var refreshEnd :()->(Unit)
//
//
//    constructor(refreshScrollStart: () -> Unit, refreshEnd: () -> Unit) {
//        this.refreshScrollStart = refreshScrollStart
//        this.refreshEnd = refreshEnd
//    }
//
//
//    override fun onFooterPulling(footer: RefreshFooter?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
//        refreshEnd.invoke()
//    }
//
//    override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
//        headerReleasing = true
//    }
//
//    override fun onHeaderStartAnimator(header: RefreshHeader?, headerHeight: Int, extendHeight: Int) {
//    }
//
//    override fun onStateChanged(refreshLayout: RefreshLayout?, oldState: RefreshState?, newState: RefreshState?) {
//        if (!headerReleasing){
//            refreshEnd.invoke()
//        }
//    }
//
//    override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
//
//    }
//
//    override fun onFooterStartAnimator(footer: RefreshFooter?, footerHeight: Int, extendHeight: Int) {
//
//    }
//
//    override fun onFooterReleasing(footer: RefreshFooter?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
//        refreshEnd.invoke()
//    }
//
//    override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
//        refreshScrollStart.invoke()
//    }
//
//    override fun onRefresh(refreshlayout: RefreshLayout?) {
//
//    }
//
//    override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
//
//    }
//
//}