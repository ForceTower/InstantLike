package com.forcetower.likesview.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.forcetower.likesview.core.database.LikeDatabase
import com.forcetower.likesview.core.model.database.InstagramProfile
import com.forcetower.likesview.core.service.InstagramAPI
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class InstagramProfileRepository @Inject constructor(
    private val service: InstagramAPI,
    private val database: LikeDatabase
) {
    suspend fun getProfile(username: String): LiveData<InstagramProfile> {
        return liveData {
            emitSource(database.instagramProfiles().getProfile(username))
            try {
                val result = service.getUser(username)
                val profile = InstagramProfile.createFromFetch(result)
                if (profile != null) database.instagramProfiles().insert(profile)
            } catch (exception: HttpException) {
                Timber.d("A exception was raised")
                Timber.e(exception)
            }
        }
    }

    fun getUsernames() = database.instagramProfiles().getUsernames()
}