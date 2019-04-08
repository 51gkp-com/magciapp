package com.enation.app.javashop.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.enation.javashop.android.lib.utils.getEventCenter
import com.enation.javashop.android.middleware.config.JavaShopConfigCenter
import com.enation.javashop.android.middleware.event.PayEvent
import com.enation.javashop.android.middleware.event.PayState
import com.tencent.mm.sdk.modelbase.BaseReq
import com.tencent.mm.sdk.modelbase.BaseResp
import com.tencent.mm.sdk.openapi.IWXAPI
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler
import com.tencent.mm.sdk.openapi.WXAPIFactory

/**
 * Created by LDD on 2018/10/23.
 */
class WXPayEntryActivity :Activity() , IWXAPIEventHandler {

    private val TAG = "WXPayEntryActivity"
    private val api by lazy { WXAPIFactory.createWXAPI(this, JavaShopConfigCenter.INSTANCE.WECHAT_APP_ID); }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.api.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.api.handleIntent(intent, this)
    }

    override fun onResp(paramBaseResp: BaseResp) {
        Log.d("WXPayEntryActivity", "onPayFinish, errCode = " + paramBaseResp.errCode)
        if (paramBaseResp.type == 5){
            when (paramBaseResp.errCode) {
                -1 -> {
                    getEventCenter().post(PayEvent(PayState.FAILD))
                    return
                }
                0 -> {
                    getEventCenter().post(PayEvent(PayState.SUCCESS))
                    return
                }
                else -> {
                    getEventCenter().post(PayEvent(PayState.FAILD))
                    return
                }
            }
        }
        getEventCenter().post(PayEvent(PayState.CANCLE))
    }

    override fun onReq(p0: BaseReq?) {

    }
}