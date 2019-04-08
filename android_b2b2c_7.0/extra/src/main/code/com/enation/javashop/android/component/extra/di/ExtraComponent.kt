package com.enation.javashop.android.component.extra.di

import com.enation.javashop.android.component.extra.activity.ScanActivity
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * Created by LDD on 2018/11/5.
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ExtraComponent {

    fun inject(activity:ScanActivity)

}