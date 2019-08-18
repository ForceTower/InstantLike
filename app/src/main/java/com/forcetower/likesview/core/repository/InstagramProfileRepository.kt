package com.forcetower.likesview.core.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.room.withTransaction
import com.forcetower.likesview.core.database.LikeDatabase
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.service.InstagramAPI
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramProfileRepository @Inject constructor(
    private val service: InstagramAPI,
    private val database: LikeDatabase,
    private val preferences: SharedPreferences,
    context: Context
) {
    private val deviceWidth = context.resources.displayMetrics.widthPixels
    init {
        Timber.d("The device width $deviceWidth")
    }

    fun getSelectedProfile(): LiveData<InstagramProfile?> {
        return database.instagramProfiles().getSelectedProfile()
    }

    suspend fun getProfile(username: String): LiveData<InstagramProfile> {
        updateProfile(username)
        return database.instagramProfiles().getProfile(username)
    }

    private suspend fun updateProfile(username: String) {
        try {
            val result = service.getUser(username)
            val profile = InstagramProfile.createFromFetch(result)
            val medias = InstagramMedia.getMediaListFromProfileFetch(result, deviceWidth / 3, profile?.nextCachedPage)
            if (medias.isNotEmpty()) {
                val mean = medias.sumBy { it.likes } / medias.size
                profile?.apply { meanLikes = mean }
            }
            if (profile != null) {
                val old = database.instagramProfiles().getProfileDirect(username)
                val merged = profile.merge(old)
                database.withTransaction {
                    database.instagramProfiles().insert(merged)
                    database.instagramMedia().insert(medias)
                }
            }
        } catch (exception: Throwable) {
            Timber.d("A exception was raised")
            Timber.e(exception)
        }
    }

    fun getAvailableProfiles() = database.instagramProfiles().getUsernames()

    suspend fun insertProfile(profile: InstagramProfile) {
        profile.apply {
            insertedAt = Calendar.getInstance().timeInMillis
        }
        database.instagramProfiles().insertSelecting(profile)
        val amount = preferences.getInt("profiles_added", 0) + 1
        preferences.edit().putInt("profiles_added", amount).apply()
    }

    fun getMedias(username: String): LiveData<List<InstagramMedia>> {
        return database.instagramMedia().getMediasOfProfile(username)
    }

    fun getSelectedProfileMedias(): LiveData<List<InstagramMedia>> {
        return database.instagramMedia().getMediasOfSelectedProfile()
    }

    suspend fun setSelectedProfile(username: String) {
        database.instagramProfiles().setCurrentProfile(username)
        updateProfile(username)
    }

    fun getSelectedProfileMediaSource(pageSize: Int = 20): LiveData<PagedList<InstagramMedia>> {
        val boundary = ProfilePicturesBoundaryCallback(database, service, 120)

        return database.instagramMedia().getMediasSourceOfSelectedProfile().toLiveData(
            pageSize = pageSize,
            initialLoadKey = null,
            fetchExecutor = Executors.newSingleThreadExecutor(),
            boundaryCallback = boundary
        )
    }
}