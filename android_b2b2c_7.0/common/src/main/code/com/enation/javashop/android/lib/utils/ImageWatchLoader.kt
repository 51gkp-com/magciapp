package com.enation.javashop.android.lib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.enation.javashop.net.engine.utils.ThreadFromUtils
import com.enation.javashop.net.engine.utils.ThreadUtils
import com.github.ielse.imagewatcher.ImageWatcher
import com.tmall.wireless.tangram.util.ImageUtils

class ImageWatchLoader : ImageWatcher.Loader{
    override fun load(p0: Context, p1: Uri, call: ImageWatcher.LoadCallback) {
        call.onLoadStarted(null)
        Glide.with(p0).load(p1).listener(object : RequestListener<Uri, GlideDrawable> {
            override fun onException(p0: Exception, p1: Uri, p2:         com.bumptech.glide.request.target.Target
            <GlideDrawable>, p3: Boolean): Boolean {
                call.onLoadFailed(null)
                return true
            }

            override fun onResourceReady(p0: GlideDrawable, p1: Uri, p2:         com.bumptech.glide.request.target.Target
            <GlideDrawable>, p3: Boolean, p4: Boolean): Boolean {
               call.onResourceReady(p0)
                return true
            }
        }).into(1000,1000)
    }
}