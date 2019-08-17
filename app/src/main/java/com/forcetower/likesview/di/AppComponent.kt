package com.forcetower.likesview.di

import com.forcetower.likesview.LikesViewApp
import com.forcetower.likesview.di.module.ActivityModule
import com.forcetower.likesview.di.module.ApplicationModule
import com.forcetower.likesview.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface AppComponent: AndroidInjector<LikesViewApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: LikesViewApp): Builder
        fun build(): AppComponent
    }
}
