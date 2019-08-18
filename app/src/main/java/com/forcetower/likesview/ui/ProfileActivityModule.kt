package com.forcetower.likesview.ui

import com.forcetower.likesview.ui.media.MediaListFragment
import com.forcetower.likesview.ui.profile.HomeProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileActivityModule {
    @ContributesAndroidInjector
    abstract fun homeProfileFragment(): HomeProfileFragment
    @ContributesAndroidInjector
    abstract fun mediaList(): MediaListFragment
}