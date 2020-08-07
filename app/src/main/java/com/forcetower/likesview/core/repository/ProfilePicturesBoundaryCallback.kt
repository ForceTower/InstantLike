package com.forcetower.likesview.core.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.forcetower.likesview.core.database.LikeDatabase
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.service.InstagramAPI
import com.google.gson.Gson
import timber.log.Timber
import java.util.concurrent.Executors

class ProfilePicturesBoundaryCallback(
    private val database: LikeDatabase,
    private val service: InstagramAPI,
    private val desiredWidth: Int
) : PagedList.BoundaryCallback<InstagramMedia>() {
    var loadingZero = false
    var loadingAfter = false
    private var userId: Long? = null
    private val executor = Executors.newSingleThreadExecutor()

    @MainThread
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        if (loadingZero) return
        loadingZero = true
        Timber.d("On zero items loaded")

        executor.execute {
            val id = userId ?: database.instagramProfiles().getSelectedProfileDirect()?.id ?: 0
            Timber.d("Id resolved to $id")
            requestPage(id)
            loadingZero = false
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: InstagramMedia) {
        super.onItemAtEndLoaded(itemAtEnd)
        if (loadingAfter) return
        loadingAfter = true
        val nextPage = itemAtEnd.nextPage
        Timber.d("On item at end loaded $nextPage")
        nextPage ?: return
        executor.execute {
            val id = userId ?: database.instagramProfiles().getSelectedProfileDirect()?.id ?: 0
            requestPage(id, nextPage)
            Timber.d("Completed inserting end items")
            loadingAfter = false
        }
    }

    private fun requestPage(id: Long, maxId: String? = null) {
        try {
            val pageCall = service.getProfilePage(createQueryMap(id, maxId = maxId))
            val page = pageCall.execute().body()!!
            val user = page.data?.user ?: page.graph?.user
            val pageInfo = user?.edgeMedia?.pageInfo
            val nextPage = pageInfo?.endCursor
            val hasNextPage = pageInfo?.hasNextPage ?: false
            database.instagramProfiles().updateNextPage(id, nextPage, hasNextPage)
            val medias = InstagramMedia.getMediaListFromProfileFetch(page, desiredWidth, nextPage)
            Timber.d("New medias ${medias.size}")
            database.instagramMedia().insertNonBlock(medias)
            Timber.d("Completed zero items")
            userId = id
        } catch (error: Throwable) {

        }
    }

    private fun createQueryMap(
        userId: Long,
        amount: Int = 12,
        maxId: String? = null
    ): Map<String, String> {
        val gson = Gson()
        val variables = gson.toJson(
            mutableMapOf(
                "id" to userId,
                "first" to amount,
                "after" to maxId
            )
        )

        Timber.d("Variables: $variables")
        return mutableMapOf(
            "variables" to variables
        )
    }
}