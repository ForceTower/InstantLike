package com.forcetower.likesview.ui

import com.forcetower.likesview.ui.addprofile.AddProfileFragment
import com.forcetower.likesview.ui.onboarding.OnboardingFragment
import com.forcetower.likesview.ui.profile.HomeProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnboardingActivityModule {
    @ContributesAndroidInjector
    abstract fun onboarding(): OnboardingFragment
    @ContributesAndroidInjector
    abstract fun addProfile(): AddProfileFragment
}