package com.forcetower.likesview.di.module

import com.forcetower.likesview.ui.LauncherActivity
import com.forcetower.likesview.ui.OnboardingActivity
import com.forcetower.likesview.ui.OnboardingActivityModule
import com.forcetower.likesview.ui.ProfileActivity
import com.forcetower.likesview.ui.ProfileActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun launcherActivity(): LauncherActivity
    @ContributesAndroidInjector(modules = [OnboardingActivityModule::class])
    abstract fun mainActivity(): OnboardingActivity
    @ContributesAndroidInjector(modules = [ProfileActivityModule::class])
    abstract fun profileActivity(): ProfileActivity
}