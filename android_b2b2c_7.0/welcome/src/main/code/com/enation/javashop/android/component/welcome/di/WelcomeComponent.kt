package com.enation.javashop.android.component.welcome.di

import com.enation.javashop.android.component.welcome.activity.WelcomeActivity
import com.enation.javashop.android.middleware.di.ApplicationComponent
import dagger.Component

/**
 * Created by LDD on 2017/8/15.
 */
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface WelcomeComponent {
    fun inject(activity: WelcomeActivity)
}