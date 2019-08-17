package com.forcetower.likesview.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.di.annotation.ViewModelKey
import com.forcetower.likesview.ui.LaunchViewModel
import com.forcetower.likesview.ui.addprofile.AddProfileViewModel
import com.forcetower.likesview.ui.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(viewModel: LaunchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddProfileViewModel::class)
    abstract fun bindAddProfileViewModel(viewModel: AddProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}