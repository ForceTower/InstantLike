package com.forcetower.likesview

import com.forcetower.likesview.di.AppComponent
import com.forcetower.likesview.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class LikesViewApp : DaggerApplication() {
    private lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()
        return component
    }
}