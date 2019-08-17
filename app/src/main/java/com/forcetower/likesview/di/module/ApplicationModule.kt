package com.forcetower.likesview.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.forcetower.likesview.BuildConfig
import com.forcetower.likesview.LikesViewApp
import com.forcetower.likesview.core.service.InstagramAPI
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: LikesViewApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providePreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("logging") logging: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideInstagramAPI(client: OkHttpClient): InstagramAPI {
        return Retrofit.Builder()
            .baseUrl("https://www.instagram.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstagramAPI::class.java)
    }

    @Provides
    @Singleton
    @Named("logging")
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
    }
}