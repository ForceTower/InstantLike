package com.forcetower.likesview.di.module

import com.forcetower.likesview.ui.MainActivity
import com.forcetower.likesview.ui.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity
}