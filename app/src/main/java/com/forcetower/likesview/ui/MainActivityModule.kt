package com.forcetower.likesview.ui

import com.forcetower.likesview.ui.profile.AddProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun addProfile(): AddProfileFragment
}